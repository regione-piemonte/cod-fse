<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="q-pb-xl">
    <!--    VISUALIZZAZIONE SE HA ADERITO IN PASSATO E VUOLE RIADERIRE-->
    <template v-if="doctorInfo && doctorInfo.data_fine_adesione">
      <!--    RIABILITA ADESIONE-->
      <div class="page-settings">
        <q-card class="q-mb-lg q-mt-xl">
          <q-card-section>
            <div class="text-h1 q-mb-sm">Riabilita adesione</div>
            <p class="no-margin">
              Aderendo al servizio âContatto digitaleâ Ã¨ possibile essere contattati esclusivamente dai pazienti abilitati.  L'adesione comporta l'impegno a rispondere a messaggi dei propri pazienti e a consultare gli eventuali referti inviati.
              <br>
              Se si abilita la notifica di lettura da parte dei pazienti, gli assistiti potranno vedere quando i loro messaggi sono stati visualizzati.<br>
              Si ricorda che in qualunque momento Ã¨ possibile:
            </p>
            <ul>
              <li>
                disabilitare dal servizio i pazienti con cui non si ritiene piÃ¹ opportuno dialogare
              </li>
              <li>eliminare le notifiche di lettura</li>
              <li>revocare l'adesione al servizio</li>
            </ul>
            <div class="row q-col-gutter-lg items-center q-mt-xs" :class="[$q.screen.lt.md ? 'justify-start' : 'justify-end']">

              <div class="text-body1 ">
                <b>Adesione conclusa il
                {{ doctorInfo.data_fine_adesione | date("DD/MM/YYYY") }}</b>
              </div>
              <div class="col-12 col-md-auto">
                <q-btn
                  color="primary"
                  class="full-width"
                  @click="showAdhesionDialog = true"
                  >Aderisci</q-btn
                >
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
    </template>
    <template v-else>
      <div class="page-settings" v-if="doctorInfo">
        <!--    ABILITATI-->
        <q-card class="q-mb-lg q-mt-xl">
          <q-card-section>
            <div class="text-h1 q-mb-lg">I tuoi assistiti abilitati</div>
            <div class="row q-col-gutter-lg items-center justify-between">
              <div class="text-h6 col-12 col-sm">
                Ci sono
                <strong>{{ doctorInfo.n_pazienti_abilitati }}</strong> assistiti
                abilitati
              </div>
              <div class="col-12 col-sm text-right ">
                <div
                  class="row  items-center q-col-gutter-sm self-end"
                  :class="{ reverse: $q.screen.gt.xs }"
                >
                  <div class="col-12 col-sm-auto">
                    <q-btn
                      v-if="hasEnablingRequest"
                      class="full-width"
                      color="primary"
                      @click="showPatientsSearchDialog = true"
                      >Cerca assistiti</q-btn
                    >
                    <q-btn-dropdown
                      label="Abilita assistiti"
                      color="primary"
                      v-else
                    >
                      <q-list>
                        <q-item
                          clickable
                          v-close-popup
                          @click="showPatientsSearchDialog = true"
                        >
                          <q-item-section>Cerca assistiti</q-item-section>
                        </q-item>
                        <q-item
                          clickable
                          v-close-popup
                          @click="showPatientsEnableAllDialog = true"
                          :disable="getEnableAllModalRunningBlock"
                        >
                          <q-item-section
                            >Aggiungi tutti i tuoi assistiti</q-item-section
                          >
                        </q-item>
                      </q-list>
                    </q-btn-dropdown>
                  </div>
                  <div class="col-12 col-sm-auto">
                    <q-btn
                      class="full-width"
                      outline
                      color="primary"
                      @click="showPatientList(true)"
                      >Vedi tutti</q-btn
                    >
                  </div>
                </div>
              </div>
            </div>
          </q-card-section>
        </q-card>
        <!--    DISABILITATI MASCHERA NON VOLUTA BAH-->
        <!-- <q-card class="q-my-lg">
          <q-card-section>
            <div class="text-h1 q-mb-lg">I tuoi assistiti bloccati</div>
            <div class="row q-col-gutter-lg items-center">
              <div class="text-h6 col-12 col-md ">
                Ci sono
                <strong>{{ doctorInfo.n_pazienti_bloccati }}</strong> assistiti
                bloccati
              </div>
              <div class="col-12 col-md-auto">
                <csi-buttons>
                  <csi-button
                    outline
                    no-min-width
                    @click="showPatientList(false)"
                    >Vedi tutti</csi-button
                  >
                </csi-buttons>
              </div>
            </div>
          </q-card-section>
        </q-card> -->
        <!--    NOTIFICHE LETTURA-->
        <q-card class="q-mb-lg">
          <q-card-section>
            <div class="text-h1 ">Notifiche di lettura</div>
            <q-item class="q-pl-none">
              <q-item-section>
                <q-item-label class="text-body1"
                  >Mostra agli assistiti quando i messaggi vengono
                  letti</q-item-label
                >
              </q-item-section>
              <q-item-section avatar>
                <q-toggle
                  @input="setReadNotifications"
                  v-model="activeNotifications"
                  color="primary"
                  :size="$q.screen.gt.sm ? 'xl' : 'lg'"
                  :disable="isLoadingSettings"
                />
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
        <!--    REVOCA ADESIONE-->
        <q-card class="q-mb-lg">
          <q-card-section>
            <div class="text-h1 q-mb-lg">Adesione</div>
            <div class="row q-col-gutter-lg items-center">
              <div class="text-body1 col-12 col-md">
                Adesione manifestata il
                {{ doctorInfo.data_inizio_adesione | date("DD/MM/YYYY") }}
              </div>
              <div class="col-12 col-md-auto">
                <q-btn
                  color="primary"
                  class="full-width"
                  @click="showRevokeDialog = true"
                  >Revoca adesione</q-btn
                >
              </div>
            </div>
          </q-card-section>
        </q-card>
      </div>
    </template>
    <cod-patients-search-dialog v-model="showPatientsSearchDialog" v-if="showPatientsSearchDialog" @refreshList="refreshList"/>
    <cod-patient-enable-all-dialog v-model="showPatientsEnableAllDialog" />
    <cod-doctor-revoke-dialog v-model="showRevokeDialog" />
    <cod-doctor-adhesion-dialog v-model="showAdhesionDialog" @refreshList="refreshList" />
  </div>
</template>

<script>
import {
  PATIENT_LIST
} from "src/router/routes";
import CodPatientsSearchDialog from "components/CodPatientsSearchDialog";
import CodPatientEnableAllDialog from "components/CodPatientEnableAllDialog";
import CodDoctorRevokeDialog from "components/CodDoctorRevokeDialog";
import CodDoctorAdhesionDialog from "components/CodDoctorAdhesionDialog";
import { setReadNotifications, getDoctorInfo } from "src/services/api";
import { apiErrorNotify } from "src/services/utils";

export default {
  name: "PageSettings",
  components: {
    CodDoctorAdhesionDialog,
    CodDoctorRevokeDialog,
    CodPatientEnableAllDialog,
    CodPatientsSearchDialog
  },
  data() {
    return {
      isLoadingSettings: false,
      activeNotifications: false,
      showPatientsSearchDialog: false,
      showPatientsEnableAllDialog: false,
      showRevokeDialog: false,
      showAdhesionDialog: false
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },

    getEnableAllModalRunningBlock() {
      return this.$store.getters["getEnableAllModalRunningBlock"];
    },

    hasEnablingRequest() {
      return this.doctorInfo?.abilitazione_assistiti;
    }

  },
  async created() {
    await this.refreshList()
    this.activeNotifications = this.doctorInfo?.notifica_lettura_messaggi;
    let { showPatientList } = this.$route.params;
    if (showPatientList) {
      this.showPatientsSearchDialog = true;
    }
  },
  methods: {
    showPatientList(enabled) {
      let key = enabled ? "abilitati" : "disabilitati";
      let query = {};
      query[key] = true;
      let route = {
        name: PATIENT_LIST.name,
        query: query
      };

      this.$router.push(route);
    },
    async refreshList() {
      try {
        let { data: info } = await getDoctorInfo();
        this.$store.dispatch("setDoctorInfo", { info });
      } catch (error) {
        let statusCode = error?.response?.status;
        if (statusCode === 404) {
          this.$router.replace("/impostazioni");
        }
      }
    },
    async setReadNotifications() {
      this.isLoadingSettings = true;

      try {
        let payload = {
          notifica_lettura: this.activeNotifications
        };
        let response = await setReadNotifications(payload);

        this.$q.notify({
          type: "positive",
          message: "Notifiche di letture modificate con successo",
          textColor: "black"
        });
      } catch (error) {
        let message = "Non Ã¨ stato possibile modificare la notifica di lettura";
        apiErrorNotify({ error, message });
      }
      this.isLoadingSettings = false;
    }
  }
};
</script>

<style scoped></style>
