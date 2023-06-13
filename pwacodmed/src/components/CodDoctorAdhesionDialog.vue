<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-dialog
    ref="adhesionDialog"
    v-bind="attrs"
    v-on="listeners"
    :maximized="$q.screen.lt.md"
    persistent
  >
    <q-card style="max-width: 100%">
      <q-toolbar>
        <q-toolbar-title>Adesione al servizio: Impostazioni</q-toolbar-title>
        <q-btn v-close-popup dense flat icon="close" round />
      </q-toolbar>
      <q-card-section>
        <q-card bordered flat>
          <q-card-section>
            <div class="text-h5 ">Notifica di lettura</div>
            <q-item class="q-px-none">
              <q-item-section>
                Mostra agli assistiti quando i messaggi vengono letti
              </q-item-section>
              <q-item-section avatar>
                <q-toggle
                  v-model="activeNotifications"
                  color="primary"
                  size="lg"
                />
              </q-item-section>
            </q-item>
          </q-card-section>
        </q-card>
      </q-card-section>
      <q-card-section>
        <q-card bordered flat>
          <q-card-section>
            <div class="text-h5 q-mb-lg">Abilitazione assistiti</div>
            <div v-if="notValidChoiceErr">
              <p class="text-negative text-caption">
                <strong>Scegliere un'opzione</strong>
              </p>
            </div>
            <q-option-group
              id="enablement-option-group"
              v-model="enablementType"
              :options="PATIENT_ENABLEMENT_OPTIONS"
              color="primary"
              @input="notValidChoiceErr = false"
            />
          </q-card-section>
        </q-card>
      </q-card-section>
      <q-card-section class="text-right q-pt-sm q-mb-lg">
        <csi-button @click="onSubmit" :loading="isLoading">Salva</csi-button>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import { SETTINGS } from "src/router/routes";
import { PATIENT_ENABLEMENT_MAP } from "src/services/config";
import {
  enableAllPatients,
  getDoctorInfo,
  manageAdhesion,
  setReadNotifications
} from "src/services/api";
import { apiErrorNotify } from "src/services/utils";

const PATIENT_ENABLEMENT_OPTIONS = [
  {
    label:
      "Abilita gli assistiti puntualmente (scegliendo questa opzione nelle pagine succesive potrai indicare quali assistiti potranno inviarti messaggi attraverso questo) servizio",
    value: PATIENT_ENABLEMENT_MAP.PATIENT_LIST
  },
  {
    label:
      "Abilita tutti gli assistiti (scegliendo questa opzione tutti i tuoi assistiti potranno inviarti messaggi attraverso questo servizio. Gli assistiti abilitati potranno successivamente essere disabilitati puntualmente",
    value: PATIENT_ENABLEMENT_MAP.ALL
  },
  {
    label:
      "Al momento non voglio abilitare nessun assistito, lo farÃ² successivamente",
    value: PATIENT_ENABLEMENT_MAP.NONE
  }
];

export default {
  name: "CodDoctorAdhesionDialog",
  data() {
    return {
      PATIENT_ENABLEMENT_OPTIONS,
      activeNotifications: false,
      enablementType: null,
      notValidChoiceErr: false,
      isLoading: false,
      adhesionInfo: null,
      isPatientEnabled: false
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    },
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    }
  },
  methods: {
    async onSubmit() {
      if (!this.enablementType) {
        this.notValidChoiceErr = true;
        return;
      }

      this.isLoading = true;

      try {
        let payload = {
          adesione: true,
          nome_medico: this.user?.nome,
          cognome_medico: this.user?.cognome
        };
        let { data: adhesionInfo } = await manageAdhesion(payload);
        this.adhesionInfo = adhesionInfo;
      } catch (error) {
        this.isLoading = false;
        let errorValue = error?.response?.data?.detail
          ? error?.response?.data?.detail[0].valore
          : null;
        let message =
          errorValue ?? "Si Ã¨ verificato un errore, riprovare piÃ¹ tardi";
        apiErrorNotify({ error, message });
      }

      //Nel caso abilita la notifica lettura chiamare il servizio dedicato
      if (this.activeNotifications) {

        try {
          let payload = {
            notifica_lettura: true
          };
          let response = await setReadNotifications(payload);
        } catch (error) {
          let message =
            "Non Ã¨ stato possibile abilitare la notifica di lettura";
          apiErrorNotify({ error, message });
          this.activeNotifications = false;
        }
      }

      //Nel caso abilita tutti i assistiti faccio la chiamata al servizio{
      if (this.enablementType === PATIENT_ENABLEMENT_MAP.ALL) {

        try {
          let payload = {
            abilitazione: true,
            motivazione_medico: ""
          };

          let response = await enableAllPatients(payload);
          this.isPatientEnabled = true;
        } catch (error) {
          let message =
            "Non Ã¨ stato possibile inviare la richiesta di abilitazione degli assistiti";
          apiErrorNotify({ error, message });
        }
      }
      if (this.adhesionInfo) {
        try {
          let { data: info } = await getDoctorInfo();
          this.$store.dispatch("setDoctorInfo", { info });
        } catch (error) {
          let message = "Non Ã¨ stato possibile aggiornare i dati";
          apiErrorNotify({ error, message });
        }
      }

      let route = {
        name: SETTINGS.name,
        params: {
          showPatientList:
            this.enablementType === PATIENT_ENABLEMENT_MAP.PATIENT_LIST
        }
      };
      if (this.$route.name !== route.name) {
        this.$router.replace(route);
      }
      this.isLoading = false;
      await this.$emit('refreshList')
      this.$refs.adhesionDialog.hide();
    }
  }
};
</script>

<style lang="sass">
#enablement-option-group
  .q-radio
    margin-bottom: 24px

    .q-radio__label
      padding-left: 8px
</style>
