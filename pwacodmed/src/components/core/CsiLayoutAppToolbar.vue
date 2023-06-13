<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-toolbar class="csi-layout-app-toolbar">
    <slot name="left" />

    <q-toolbar-title
      class="csi-layout-app-toolbar__title"
      shrink
    >
      <slot />
    </q-toolbar-title>

    <q-space />

    <slot name="right" />
  </q-toolbar>
</template>

<script>
import {APP, HOME} from "src/router/routes";

export default {
  name: "CsiLayoutAppToolbar",
  methods: {
  }
};
</script>

<style lang="sass">
.csi-layout-app-toolbar
  background-color: $secondary
  color: #FFF

.csi-layout-app-toolbar__title
  cursor: pointer
  padding-left: 56px
</style>
