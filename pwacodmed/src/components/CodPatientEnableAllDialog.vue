<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-dialog ref="dialogEnableAll" v-bind="attrs" v-on="listeners" :maximized="$q.screen.lt.md" persistent>
    <q-card style="max-width: 800px">
      <q-toolbar>
        <q-toolbar-title>Abilitazione assistiti</q-toolbar-title>
      </q-toolbar>
      <q-card-section>
        <template v-if="enabled">
          <csi-banner type="positive" class="q-mb-md">
            Richiesta di abilitazione eseguita con successo <br />
            <template v-if="enabledCount">
              Assistiti abilitati:
              <strong>{{ enabledCount }}</strong> <br />
            </template>
            <template v-if="alreadyEnabled">
              Assistiti giÃ  abilitati:
              <strong>{{ alreadyEnabled }}</strong> <br />
            </template>
            <template v-if="cannotBeEnabled">
              Assistiti non abilitabili:
              <strong>{{ cannotBeEnabled }}</strong> <br />
            </template>
          </csi-banner>
          <csi-buttons>
            <csi-button v-close-popup outline @click="resetModal"
              >Chiudi</csi-button
            >
          </csi-buttons>
        </template>
        <template v-else-if="waitingAllPatients">
          <csi-banner type="info" class="q-mb-md">
            La richiesta Ã¨ in corso, attendere. Puoi chiudere questa finestra e
            continuare ad usare l'applicativo
          </csi-banner>
          <csi-buttons>
            <csi-button v-close-popup outline>Chiudi</csi-button>
          </csi-buttons>
        </template>
        <template v-else-if="enablingErr">
          <csi-banner type="negative" class="q-mb-md">
            Non Ã¨ stato possibile inviare la richiesta. Riprovare piÃ¹ tardi
          </csi-banner>
          <csi-buttons>
            <csi-button v-close-popup outline>Chiudi</csi-button>
          </csi-buttons>
        </template>
        <template v-else>
          <div class="q-mb-lg text-h6" style="font-weight: normal">
            Sei sicuro di voler abilitare tutti i tuoi assistiti?
          </div>

          <csi-buttons>
            <csi-button @click="onConfirm" :disable="waitingAllPatients"
              >Conferma</csi-button
            >
            <csi-button outline v-close-popup>Annulla</csi-button>
          </csi-buttons>
        </template>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import { enableAllPatients, getDoctorInfo } from "src/services/api";
import { apiErrorNotify } from "src/services/utils";

export default {
  name: "CodPatientEnableAllDialog",
  data() {
    return {
      enabled: false,
      enablingErr: false,
      waitingAllPatients: false,
      enabledCount: 0,
      alreadyEnabledCount: 0,
      cannotBeEnabled: 0,
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
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
  },
  methods: {
    resetModal() {
      this.$refs.dialogEnableAll.hide();
      this.enabled = false;
      this.enablingErr = false;
      this.waitingAllPatients = false;
      this.enabledCount = 0;
      this.alreadyEnabledCount = 0;
      this.cannotBeEnabled = 0;
      
    },
    async onConfirm() {
      let payload = {
        abilitazione: true,
        motivazione_medico: null,
      };
      this.waitingAllPatients = true;
      this.$store.dispatch("setEnableAllModalRunningBlock", true);
      try {
        let response = await enableAllPatients(payload);

        this.enabledCount = 0;
        this.alreadyEnabled = 0;
        this.cannotBeEnabled = 0;

        let responseList = response.data;
        // I TRE COUNTER VERRANNO MOSTRATI NELLA MODALE ALLA CONCLUSIONE DEL CARICAMENTO
        for (let i = 0; i < responseList.length; i++) {
          if (responseList[i].stato_abilitazione === "ABILITATO") {
            this.enabledCount += 1;
          } else if (responseList[i].stato_abilitazione === "GIA_ABILITATO") {
            this.alreadyEnabled += 1;
          } else if (responseList[i].stato_abilitazione === "NON_ABILITABILE") {
            this.cannotBeEnabled += 1;
          }
        }

        let { data: info } = await getDoctorInfo();
        await this.$emit("refreshList");

        this.$store.dispatch("setDoctorInfo", { info });
        this.enabled = true;
        this.waitingAllPatients = false;
        this.$store.dispatch("setEnableAllModalRunningBlock", false);
      } catch (error) {
        this.$store.dispatch("setEnableAllModalRunningBlock", false);
        this.enablingErr = true;
        this.waitingAllPatients = false;
        let message =
          "Non Ã¨ stato possibile inviare la richiesta di abilitazione di tutti gli assistiti";
        apiErrorNotify({ error, message });
      }
    },
  },
};
</script>

<style scoped></style>
