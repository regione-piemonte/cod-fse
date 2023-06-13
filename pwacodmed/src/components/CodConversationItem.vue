<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-card
    :class="classes"
    class="cod-conversation-item cursor-pointer"
    @click="onClick"
  >
    <q-card-section>
      <div class="row q-col-gutter-md">
        <div class="col-auto gt-sm self-center">
          <q-icon :name="patientIcon" size="lg" />
        </div>
        <div class="col column q-col-gutter-y-xs">
          <div class="row justify-between">
            <div v-if="patient" class="col-12 col-md">
              {{ patient.nome }} {{ patient.cognome }}
            </div>
          </div>

          <div class="text-h5">
            {{ conversation.argomento }}
          </div>
          <template v-if="lastMessage">
            <div class="text-italic cod-conversation-item--msg-text">
              {{ lastMessage.testo }}
            </div>
            <div class="text-caption text-accent q-mt-xs lt-md">
              <div class="">{{ lastMessage.data_creazione | datetime }}</div>
            </div>
          </template>
          <div v-else class="text-caption lt-md">Nessun messaggio</div>
        </div>
        <div class="col-auto column items-end gt-sm q-col-gutter-y-sm">
          <div class="col-auto" v-if="unSeenMessages">
            <q-badge class="text-bold q-px-sm q-py-xs" color="csi-pink" rounded>
              {{ unSeenMessages }} &nbsp;
              <span v-if="unSeenMessages === 1">messaggio non letto</span>
              <span v-else>messaggi non letti</span>
            </q-badge>
          </div>
          <div v-if="blocked">
            <div>
              <q-badge color="black" class="text-bold" rounded outline>
                <q-icon name="lock" size="xs" class="q-mr-xs" />
                Conversazione in sola lettura
              </q-badge>
            </div>
          </div>
        </div>
        <div class="col-auto row items-center q-col-gutter-x-md">
          <div v-if="blocked" class="lt-md">
            <div>
              <q-icon name="lock" size="xs" class="q-mr-xs">
                <q-tooltip> Messaggio in sola lettura </q-tooltip>
              </q-icon>
            </div>
          </div>
          <div class="lt-md" v-if="unSeenMessages">
            <q-badge class="text-bold text-h6" color="csi-pink" rounded
              >{{ unSeenMessages }}
            </q-badge>
          </div>
          <div>
            <q-icon :name="senderIcon" size="sm"></q-icon>
          </div>
          <div class="column q-col-gutter-y-xs text-caption gt-sm">
            <template v-if="lastMessage">
              <div class="">Ultimo messaggio</div>
              <div class="text-bold">
                {{ lastMessage.data_creazione | datetime }}
              </div>
            </template>
            <template v-else>
              <div class="text-caption">Nessun messaggio</div>
            </template>
          </div>
        </div>
        <div
          class="col-auto q-pl-md"
          v-if="doctorInfo && !doctorInfo?.data_fine_adesione"
        >
          <q-btn
            aria-label="Menu altre azioni"
            dense
            flat
            icon="more_vert"
            round
            @click.stop=""
          >
            <q-menu anchor="bottom right" self="top right">
              <q-list style="min-width: 180px">
                <q-item
                  v-if="isPatientBlocked"
                  clickable
                  @click="enablePatient"
                  :disabled="isEnabling"
                >
                  <q-item-section> Abilita assistito </q-item-section>
                  <q-item-section side v-if="isEnabling">
                    <q-spinner :thickness="2" color="primary" />
                  </q-item-section>
                </q-item>
                <q-item
                  v-else
                  v-close-popup
                  clickable
                  @click="showPatientsBlockDialog = true"
                >
                  <q-item-section>Disabilita assistito</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </div>
      </div>
    </q-card-section>

    <cod-patients-block-dialog
      v-model="showPatientsBlockDialog"
      :patients="[patient]"
      :enablement="blocked"
      @on-block="onBlock"
      @refreshList="onBlock"
    />
  </q-card>
</template>

<script>
import { MSG_AUTHOR_MAP } from "src/services/config";
import { CONVERSATION_DETAIL } from "src/router/routes";
import CodPatientsBlockDialog from "components/CodPatientsBlockDialog";
import { getGender } from "src/services/tax-code";
import { managePatients } from "src/services/api";
import { apiErrorNotify } from "src/services/utils";

export default {
  name: "CodConversationItem",
  components: { CodPatientsBlockDialog },
  props: {
    conversation: { type: Object, required: true, default: null },
  },
  data() {
    return {
      showPatientsBlockDialog: false,
      isEnabling: false,
    };
  },
  computed: {
    classes() {
      return {
        "cod-conversation-item--blocked": this.blocked,
        "cod-conversation-item--unseen":
          this.unSeenMessages > 0 && !this.blocked,
      };
    },
    blocked() {
      return this.conversation?.sola_lettura;
    },
    lastMessage() {
      return this.conversation?.ultimo_messaggio;
    },
    unSeenMessages() {
      return this.conversation?.n_messaggi_non_letti;
    },
    senderIcon() {
      let isMsgReceived =
        this.lastMessage?.autore?.tipo === MSG_AUTHOR_MAP.DOCTOR;
      return isMsgReceived ? "south" : "north";
    },
    isPatientBlocked() {
      return this.blocked;
    },
    patient() {
      return this.conversation?.assistito;
    },
    author() {
      return this.conversation?.autore;
    },
    patientGender() {
      return getGender(this.patientTaxCode);
    },
    patientTaxCode() {
      return this.patient?.codice_fiscale;
    },
    patientIcon() {
      let gender = this.patientGender === "F" ? "donna" : "uomo";
      return `img:icone/avatar-${gender}.svg`;
    },
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
  },
  methods: {
    onClick() {
      let route = {
        name: CONVERSATION_DETAIL.name,
        params: {
          id: this.conversation.id,
          conversation: this.conversation,
        },
      };

      this.$router.push(route);
    },
    async enablePatient() {
      this.isEnabling = true;

      let payload = {
        abilitazione: true,
        assistiti: [this.patientTaxCode],
        motivazione_medico: null,
      };

      try {
        let response = await managePatients(payload);
        this.$emit("manage-patient");
      } catch (error) {
        let message = "Non Ã¨ stato possibile abilitare l'assistito";
        apiErrorNotify({ error, message });
      }

      this.isEnabling = false;
    },
    onBlock() {
      this.$emit("manage-patient");
    },
  },
};
</script>

<style lang="sass">
.cod-conversation-item
  &.cod-conversation-item--blocked
    background-color: $grey-1
    box-shadow: none !important
    border: 1px solid rgba(0, 0, 0, 0.12)

  &--msg-text
    padding-right: 24px
    max-width: 100%
    text-overflow: ellipsis
    white-space: nowrap
    overflow: hidden
</style>
