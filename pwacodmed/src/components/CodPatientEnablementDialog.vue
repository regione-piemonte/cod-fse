<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-dialog
    v-bind="attrs"
    v-on="listeners"
    :maximized="$q.screen.lt.md"
    ref="patientEnablementDialog"
  >
    <q-card style="max-width: 800px">
      <q-toolbar>
        <q-toolbar-title
          >{{ enablement ? "Abilita" : "Disabilita" }} assistiti</q-toolbar-title
        >
        <q-btn v-close-popup dense flat icon="close" round />
      </q-toolbar>
      <q-card-section>
        <div class="q-mb-md">
          <template v-if="nPatients === 1 && patients.length > 0">
            <q-item>
              <q-item-section avatar>
                <q-icon name="img:icone/avatar-uomo.svg" size="lg" />
              </q-item-section>
              <q-item-section>
                <q-item-label class="text-caption">Assistito</q-item-label>
                <q-item-label
                  ><strong
                    >{{ patients[0].nome }} {{ patients[0].cognome }}</strong
                  ></q-item-label
                >
              </q-item-section>
            </q-item>
          </template>
          <template v-else>
            <p>
              <strong>{{ nPatients }} assistiti selezionati</strong>
            </p>
          </template>
        </div>

        <div class="q-mb-xl">
          <div v-if="enablement">
            <template v-if="nPatients === 1 && patients.length > 0">
              Sei sicuro di voler abilitare l'assistito selezionato?
            </template>
            <template v-else>
              Sei sicuro di voler abilitare gli assistiti selezionati?
            </template>
          </div>

          <q-input
            v-else
            v-model="blockMotivation"
            :label="motivationBlockLabel"
            type="textarea"
          />
        </div>

        <csi-buttons>
          <csi-button @click="onConfirm" :loading="isLoading"
            >Conferma</csi-button
          >
          <csi-button outline v-close-popup>Annulla</csi-button>
        </csi-buttons>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import { managePatients } from "src/services/api";
import { apiErrorNotify } from "src/services/utils";

export default {
  name: "CodPatientEnablementDialog",
  props: {
    enablement: { type: Boolean, default: false },
    patients: { type: Array, default: () => [] }
  },
  data() {
    return {
      blockMotivation: "",
      isLoading: false,
      enablementChanged: false,
      enablingErr: false
    };
  },
  computed: {
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    },
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    nPatients() {
      return this.patients?.length;
    },
    isSinglePatient() {
      return this.nPatients === 1;
    },
    motivationBlockLabel() {
      let str = this.isSinglePatient ? "allâassistito" : "agli assistiti";
      return `Motivazione (facoltativa, la motivazione sarÃ  visibile ${str})`;
    }
  },
  methods: {
    async onConfirm() {
      let patientsTaxCode = this.patients?.map(p => p.codice_fiscale);

      let payload = {
        abilitazione: this.enablement,
        assistiti: patientsTaxCode,
        motivazione_medico: this.blockMotivation
      };
      this.isLoading = true;
      try {
        let response = await managePatients(payload);
        this.$store.dispatch("setEnabledPatients", patientsTaxCode);
        this.$q.notify({
          type: "positive",
          message: this.enablement ? 'Abilitazione assistiti effettuata con successo' : 'Blocco assistiti effettuata con successo',
          textColor: "black"
        });
      } catch (error) {
        let message = 'Non Ã¨ stato possibile inviare la richiesta di ' + (this.enablement? 'abilitazione' : 'disabilitazione');
        apiErrorNotify({ error, message });
      }
      this.isLoading = false;
      this.$refs.patientEnablementDialog.hide();
    }
  }
};
</script>

<style scoped></style>
