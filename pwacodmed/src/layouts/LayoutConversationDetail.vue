<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-layout class="cod-layout-app" view="hHh lpr fff">


    <!-- PAGINE -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <q-page-container>
      <router-view/>
    </q-page-container>




  </q-layout>
</template>

<script>
import { apiErrorNotifyDialog } from "src/services/utils";
import {appAssistanceForm, appAssistanceTree, appDetailFaq} from "../services/urls";
import CsiLayoutHeader from "components/core/CsiLayoutHeader";
import CsiHelpButton from "components/core/CsiHelpButton";
import CsiLayoutHeaderProfileButton from "components/core/CsiLayoutHeaderProfileButton";
import CsiLayoutAppToolbar from "components/core/CsiLayoutAppToolbar";
import CsiLayoutFooter from "components/core/CsiLayoutFooter";
import {CONVERSATION_LIST} from "src/router/routes";

export default {
  name: "LayoutConversationDetail",
  components: {

  },
  data() {
    return {
      isMenuVisible: false,
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    }
  },
  async created() {
    if(!this.$route.name)
      this.$router.push(CONVERSATION_LIST)
  },
  methods: {
    toggleMenu() {
      this.isMenuVisible = !this.isMenuVisible;
    }
  }
};
</script>

<style lang="sass">
.csi-menu-list__logo
  width: 100%
  max-width: 250px
  height: auto

.csi-notification-list__body > .csi-notification-list-item:not(:last-of-type)
  border-bottom: 1px solid rgba(0, 0, 0, .12)

.csi-notification-list-empty
  text-align: center
  padding: map-get($space-lg, 'y') map-get($space-lg, 'x')
  color: $csi-text-faded-color

.csi-notification-list-item-contacts-activation
  background-color: $blue-2
  padding: map-get($space-md, 'y') map-get($space-md, 'x')
</style>
