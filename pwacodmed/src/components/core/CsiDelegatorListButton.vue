<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-btn flat dense round icon="people" class="csi-delegator-list-button">
    <q-menu
      :value="isMenuVisible"
      class="csi-delegator-list-button__menu"
      @input="updateMenu"
      @click="hideMenu"
    >
      <q-list class="sepac-delegator-list-button__list" style="min-width: 160px">
        <q-item dense>
          <q-item-section>
            <q-item-label caption>
              Deleghe
            </q-item-label>
          </q-item-section>
        </q-item>

        <slot />
      </q-list>

      <q-item
        clickable
        class="sepac-delegator-list-button__delegation"
        @click="onClickDelegation"
      >
        <q-item-section>
          <q-item-label>
            Gestisci deleghe
          </q-item-label>
        </q-item-section>
      </q-item>
    </q-menu>
  </q-btn>
</template>

<script>
export default {
  name: "sepacDelegatorListButton",
  data() {
    return {
      isMenuVisible: false
    };
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
    onClickDelegation() {
      let eventName = "click-delegation";
      let url = "/la-mia-salute/#/deleghe";

      if (eventName in this.$listeners) return this.$emit(eventName, url);

      window.location.assign(url);
    }
  }
};
</script>

<style lang="sass">
.sepac-delegator-list-button__menu
  min-width: 180px
  max-height: 80%

.sepac-delegator-list-button__list
  padding: 0

.sepac-delegator-list-button__delegation
  background-color: $grey-3
</style>
