<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-item
    v-bind="attrs"
    v-on="listeners"
    class="sepac-menu-list-item"
    :class="classes"
  >
    <template v-if="icon_">
      <q-item-section side avatar>
        <q-icon :name="icon_" />
      </q-item-section>
    </template>

    <q-item-section>
      <template v-if="title">
        <q-item-label class="sepac-menu-list-item__title">
          {{ title }}
        </q-item-label>
      </template>

      <template v-if="caption">
        <q-item-label class="sepac-menu-list-item__caption">
          {{ caption }}
        </q-item-label>
      </template>
    </q-item-section>

    <template v-if="locked">
      <q-item-section side avatar>
        <q-icon class="sepac-menu-list-item__icon-lock" name="lock" />
      </q-item-section>
    </template>
  </q-item>
</template>

<script>
export default {
  name: "sepacMenuListItem",
  inheritAttrs: false,
  props: {
    icon: { type: String, required: false, default: null },
    title: { type: String, required: false, default: "" },
    caption: { type: String, required: false, default: "" },
    locked: { type: Boolean, required: false, default: false }
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      if (!("href" in attrs)) attrs.overlay = true;
      if (!("tag" in attrs)) attrs.tag = "a";
      if (!("target" in attrs)) attrs.target = "_self";
      if (!("clickable" in attrs)) attrs.clickable = true;
      if (!("active" in attrs)) attrs.active = false;
      if (!("activeClass" in attrs))
        attrs.activeClass = "sepac-menu-list-item--active";
      return attrs;
    },
    icon_() {
      if (!this.icon || !this.icon.startsWith("http")) return this.icon;
      return `img:${this.icon}`;
    },
    classes() {
      return {
        "sepac-menu-list-item--locked": this.locked
      };
    }
  }
};
</script>

<style lang="sass">
.sepac-menu-list-item__title
  color: $primary
  font-weight: bold

.sepac-menu-list-item__icon
  color: $primary

.sepac-menu-list-item__icon-lock
  color: $grey-7
  font-size: 18px !important

.sepac-menu-list-item--active
  background-color: $blue-1

  &.sepac-menu-list-item--locked
    background-color: $grey-2

.sepac-menu-list-item--locked
  .sepac-menu-list-item__title
    color: $grey-8
</style>
