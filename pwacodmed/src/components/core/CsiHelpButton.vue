<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-btn flat dense round no-caps icon="help" class="csi-help-button">
    <q-tooltip anchor="top middle">Aiuto</q-tooltip>
    <q-menu
      anchor="bottom left"
      :value="isMenuVisible"
      class="csi-help-button__menu"
      auto-close
    >
<!--      <q-list class="csi-help-button__menu__list" style="min-width: 120px">-->
<!--        <q-item clickable   :to="HELP_MANUALS">-->
<!--          <q-item-section>-->
<!--            <q-item-label>-->
<!--              Manuale-->
<!--            </q-item-label>-->
<!--          </q-item-section>-->
<!--        </q-item>-->

<!--        <q-item clickable   :to="HELP_CONTACTS">-->
<!--          <q-item-section>-->
<!--            <q-item-label>-->
<!--              Assistenza-->
<!--            </q-item-label>-->
<!--          </q-item-section>-->
<!--        </q-item>-->
<!--      </q-list>-->
    </q-menu>
  </q-btn>
</template>

<script>

export default {
  name: "CsiHelpButton",
  data() {
    return {
      isMenuVisible: false,

    };
  },
  computed:{

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

  }
};
</script>
