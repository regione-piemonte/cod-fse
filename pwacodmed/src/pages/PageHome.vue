<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div>
    <template v-if="doctorInfo && doctorInfo?.data_fine_adesione">
      <csi-banner class="q-mb-lg" type="info">
        Per effetto della revoca, le funzionalitÃ  principali sono state disabilitate. <br>
        Se si intende ripristinarle, Ã¨ necessario effettuare nuovamente lâadesione al servizio.
      </csi-banner>
    </template>
    <template
      v-if="isValidAdhesion || (doctorInfo && doctorInfo.data_fine_adesione)"
    >
      <q-tabs indicator-color="primary" active-color="primary" align="left">
        <q-route-tab :to="CONVERSATION_LIST" label="Conversazioni" exact />
        <q-route-tab :to="SETTINGS" label="Impostazioni" exact />
      </q-tabs>
      <router-view />
    </template>
    <template v-else>
      <q-toolbar>
        <q-toolbar-title
          >Adesione al servizio "Contatto Digitale"</q-toolbar-title
        >
      </q-toolbar>
      <csi-banner class="q-mb-lg" type="info">

          Aderendo al servizio "Contatto Digitale" Ã¨ possibile ricevere
          comunicazioni esclusivamente dagli assistiti abilitati. Lâadesione
          comporta lâimpegno a rispondere a messaggi dei propri pazienti e a
          consultare gli eventuali referti inviati.<br>
          Se si abilita la notifica di lettura da parte dei pazienti, gli assistiti potranno vedere quando i loro messaggi sono stati visualizzati.<br>
          Si ricorda che in qualunque momento Ã¨ possibile:

        <ul>
          <li>
            disabilitare al servizio pazienti con cui non si ritiene piÃ¹ opportuno dialogare
          </li>
          <li>disabilitare le notifiche di lettura</li>
          <li>revocare l'adesione al servizio</li>
        </ul>
      </csi-banner>
      <csi-buttons>
        <csi-button @click="showAdhesionDialog = true">Aderisci</csi-button>
      </csi-buttons>
    </template>

    <cod-doctor-adhesion-dialog v-model="showAdhesionDialog" />
  </div>
</template>

<script>
import { CONVERSATION_LIST, HOME, SETTINGS } from "src/router/routes";
import CodDoctorAdhesionDialog from "components/CodDoctorAdhesionDialog";

export default {
  name: "PageHome",
  components: { CodDoctorAdhesionDialog },
  data() {
    return {
      CONVERSATION_LIST,
      SETTINGS,
      showAdhesionDialog: false,
    };
  },
  computed: {
    isValidAdhesion() {
      return this.$store.getters["isValidAdhesion"];
    },
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
  },
  async created() {
    if (this.isValidAdhesion || this.doctorInfo?.data_fine_adesione) {
      await this.handleNavigation(this.$route);
    }
  },
  methods: {
    handleNavigation(to, from, next) {
      // Quando la route target Ã¨ proprio la home
      // => significa Ã¨ arrivato da qualche URL che punta direttamente al servizio
      //
      // In questo caso gestiamo dove redirigere l'utente
      console.log("PAGE HOME----------------------------------", to);
      if (to.name === HOME.name) {
        // Di default effettuiamo il redirect alla lista di conversazioni
        let route = CONVERSATION_LIST;
        console.log("pushing:", route.name);
        next ? next(route) : this.$router.push(route);
        return;
      } else if (next) {
        next();
      }
    },
  },
};
</script>

<style scoped></style>
