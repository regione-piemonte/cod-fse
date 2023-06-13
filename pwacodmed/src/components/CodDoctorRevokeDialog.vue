<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-dialog v-bind="attrs" v-on="listeners" :maximized="$q.screen.lt.md">
    <q-card style="max-width: 800px">
      <q-toolbar>
        <q-toolbar-title>Revoca adesione</q-toolbar-title>
        <q-btn v-close-popup dense flat icon="close" round />
      </q-toolbar>
      <q-card-section>
        <csi-banner type="warning" class="q-mb-lg">
          In caso di revoca del servizio le conversazioni effettuate in precedenza potranno essere ancora lette dal medico e dai pazienti, ma non sarÃ  possibile avviare nuove conversazioni o aggiungere nuovi messaggi.<br>
          Si intende revocare lâadesione? 
        </csi-banner>

        <csi-buttons>
          <csi-button @click="onRevoke" :loading="isLoading"
            >Conferma</csi-button
          >
          <csi-button v-close-popup outline>Annulla</csi-button>
        </csi-buttons>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import { manageAdhesion } from "src/services/api";
import { apiErrorNotify } from "src/services/utils";

export default {
  name: "CodDoctorRevokeDialog",
  data() {
    return {
      isLoading: false
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
    },

    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    }
  },
  methods: {
    async onRevoke() {
      this.isLoading = true;
      this.$router.push("/conversazioni");
      let payload = {
        adesione: false,
        nome_medico: this.user?.nome,
        cognome_medico: this.user?.cognome
      };

      try {
        let { data: info } = await manageAdhesion(payload);
        this.$store.dispatch("setDoctorInfo", { info });
      } catch (error) {
        let message = error?.response?.data?.detail[0].valore ?? "";
        apiErrorNotify({ error, message });
      }

      this.isLoading = false;
    }
  }
};
</script>

<style scoped></style>
