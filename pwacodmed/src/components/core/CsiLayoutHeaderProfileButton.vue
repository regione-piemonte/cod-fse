<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-avatar class="csi-layout-header-profile-button" @click="showMenu">
    <div class="csi-layout-header-profile-button__text">
      <slot>{{ avatarText }}</slot>
    </div>

<!--    <q-menu-->
<!--      :value="isMenuVisible"-->
<!--      class="csi-layout-header-profile-button__menu"-->
<!--      @input="updateMenu"-->
<!--    >-->
<!--      <q-list style="min-width: 160px">-->
<!--&lt;!&ndash;        <q-item>&ndash;&gt;-->
<!--&lt;!&ndash;          <q-item-section>&ndash;&gt;-->
<!--&lt;!&ndash;            <q-item-label> {{ surname }} {{ name }} </q-item-label>&ndash;&gt;-->
<!--&lt;!&ndash;            <q-item-label caption> {{ taxCode | empty }}</q-item-label>&ndash;&gt;-->
<!--&lt;!&ndash;          </q-item-section>&ndash;&gt;-->
<!--&lt;!&ndash;        </q-item>&ndash;&gt;-->

<!--        <q-item clickable @click="onClickProfile">-->
<!--          <q-item-section side>-->
<!--            <q-icon :name="iconProfile" />-->
<!--          </q-item-section>-->

<!--          <q-item-section>-->
<!--            <q-item-label>Profilo</q-item-label>-->
<!--          </q-item-section>-->
<!--        </q-item>-->

<!--        <q-item clickable @click="onClickLogout">-->
<!--          <q-item-section side>-->
<!--            <q-icon :name="iconLogout" />-->
<!--          </q-item-section>-->

<!--          <q-item-section>-->
<!--            <q-item-label>Esci</q-item-label>-->
<!--          </q-item-section>-->
<!--        </q-item>-->
<!--      </q-list>-->
<!--    </q-menu>-->
  </q-avatar>
</template>

<script>
import {PROFILE} from "src/router/routes";

export default {
  name: "CsiLayoutHeaderProfileButton",
  props: {
    name: { type: String, required: false, default: "" },
    surname: { type: String, required: false, default: "" },
    taxCode: { type: String, required: false, default: "" },
    iconProfile: { type: String, required: false, default: "person" },
    iconLogout: { type: String, required: false, default: "exit_to_app" }
  },
  data() {
    return {
      isMenuVisible: false,
      PROFILE
    };
  },
  computed: {
    avatarText() {
      let n = this.name ? this.name.charAt(0) : "";
      let c = this.surname ? this.surname.charAt(0) : "";
      let result = `${c}${n}`;
      return result.trim();
    }
  },
  methods: {
    showMenu() {
      this.isMenuVisible = true;
    },
    hideMenu() {
      this.isMenuVisible = false;
    },
    updateMenu(val) {
      this.isMenuVisible = val;
    },
    onClickProfile() {
      this.hideMenu();
      this.$router.push(PROFILE)

    },
    onClickLogout() {
      this.hideMenu();
      let eventName = "click-logout";
      let url = "/sepac-api/bff/api/logout";

      if (eventName in this.$listeners) return this.$emit(eventName, url);

      window.location.assign(url);
    }
  }
};
</script>

<style lang="sass">
.csi-layout-header-profile-button
  background-color: $accent
  cursor: pointer

.csi-layout-header-profile-button
  font-size: 32px !important

.csi-layout-header-profile-button__text
  text-transform: uppercase
  font-size: 14px
</style>
