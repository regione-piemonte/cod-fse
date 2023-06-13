<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <csi-page :padding="!hideLayout">
    <template v-if="isLoading">
      <csi-inner-loading :showing="true" block />
    </template>
    <template v-else-if="noToken">
      <csi-banner class="q-my-lg" type="warning">
        Nessun token trovato
      </csi-banner>
    </template>

    <template v-else-if="!user">
      <csi-banner class="q-my-lg" type="warning">
        Utente non trovato
      </csi-banner>
    </template>
    <template v-else-if="doctorErr">
      <csi-banner class="q-my-lg" type="negative">
        In questo momento non Ã¨ possibile reperire i tuoi dati, ti invitiamo a
        riprovare piÃ¹ tardi. <br />
        Ci scusiamo per il disagio. Se il problema persiste ti suggeriamo di
        contattare l'assistenza.
      </csi-banner>
    </template>
    <template v-else>
      <router-view />
    </template>
  </csi-page>
</template>

<script>
import { getDoctorInfo, getUser } from "src/services/api";

import { SessionStorage } from "quasar";

import { TOKEN } from "src/services/mocks";
import { HOME } from "src/router/routes";
import { IS_DEV } from "src/services/config";

const USER_SESSION_KEY = "USER";
export default {
  name: "AppCod",
  components: {},
  data() {
    return {
      isLoading: false,
      noToken: false,
      token: null,
      doctorErr: false
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
    roleParams() {
      return this.$store.getters["getRoleParams"];
    },
    //utile per la pagina del dettaglio conversazione a schermo intero
    hideLayout(){
      return !!this.$route.meta.hide_layout
    }
  },
  async created() {

    this.isLoading = true;

    let { token } = this.$route?.query;
    //SU DEV DOBBIAMO AGGIUNGERE MANUALMENTE IL TOKEN
    if (IS_DEV) {
      token = TOKEN;
      //  cerco prima l'utente nel sessionstorage
      let user = SessionStorage.getItem(USER_SESSION_KEY);
      if (user) this.$store.dispatch("setUser", { user });
    }

    this.token = token;

    if (!this.token) {
      console.log("APPCOD no token ");
      // Se non c'Ã¨ il TOKEN cerco l'utente nel sessionstorage
      let user = SessionStorage.getItem(USER_SESSION_KEY);
      console.log("AppCod.vue saved user", user);
      if (user) this.$store.dispatch("setUser", { user });
      else {
        //redirect ver PUA
        // let goTo = urlPua();
        // window.location.assign(goTo);

        this.noToken = true;
        this.isLoading = false;
        return;
      }
    } else {
      try {

        let params = {
          token: this.token
        };
        let { data: user } = await getUser({ params });

        this.$store.dispatch("setUser", { user });

        // Salvo dati dell'utente sul sessionStorage
        SessionStorage.set(USER_SESSION_KEY, user);
      } catch (error) {
        // SOLO PER PROVE IN LOCALE
        // let user = USER
        // this.$store.dispatch("setUser", { user });
        console.log(error);
      }
    }



    //CHIAMIAMO il servizio info medico
    try {
      let { data: info } = await getDoctorInfo();

      this.$store.dispatch("setDoctorInfo", { info });
    } catch (error) {
      let statusCode = error?.response?.status;
      if (statusCode === 404) {
        this.$router.replace("/impostazioni");
      } else {
        this.doctorErr = true;
      }
    }

    if (this.doctorInfo) {
      let isJoined = this.$store.getters["isValidAdhesion"];
      let route = isJoined ? this.$route : HOME;
      if (this.$route.name !== route.name) {
        this.$router.replace(route);
      }
    }

    this.isLoading = false;
  },
  methods: {}
};
</script>

<style scoped></style>
