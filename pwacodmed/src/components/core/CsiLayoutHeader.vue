<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-header v-bind="attrs" v-on="listeners" class="csi-layout-header">
    <slot name="before" />

    <q-toolbar>
      <slot name="left">
      </slot>

      <img alt="Salute Piemonte"
           class="csi-layout-header__toolbar__title q-hoverable q-link cursor-pointer"
           src="images/logo-salute-piemonte-operatori.svg"
           @click="onClickLogo"
      />


      <q-space />

      <slot name="right" />
    </q-toolbar>

    <slot name="after" />
  </q-header>
</template>

<script>
import {HOME} from "src/router/routes";
import {urlPua} from "src/services/urls";

export default {
  name: "CsiLayoutHeader",
  inheritAttrs: false,
  props: {
    menu: { type: Boolean, required: false, default: false }
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      if (!("reveal" in attrs)) attrs.reveal = true;
      if (!("elevated" in attrs)) attrs.elevated = true;
      return attrs;
    }
  },
  methods: {
    onClickLogo() {
      let goTo = urlPua()

      // this.$router.push(HOME)
      // let eventName = "click-logo";
      // let goTo = "/la-mia-salute/";
      //
      // if (eventName in this.$listeners) return this.$emit(eventName, goTo);
      //
       window.location.assign(goTo);
    }
  }
};
</script>

<style lang="sass">
.csi-layout-header__title
  cursor: pointer

.csi-layout-header__toolbar__title
  max-width: 200px
</style>
