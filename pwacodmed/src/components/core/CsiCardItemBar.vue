<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div :class="classes" class="csi-card-item-bar row items-center">
    <slot/>
  </div>
</template>

<script>
const TYPES = ["positive", "negative", "info", "warning"];

export default {
  name: "CsiCardItemBar",
  component: {},
  props: {
    type: {
      type: String,
      required: false,
      default: null,
      validator: v => TYPES.includes(v)
    }
  },
  data() {
    return {};
  },
  computed: {
    classes() {
      let result = [];
      if (this.type) result.push(`csi-card-item-bar--${this.type}`);
      return result;
    }
  }
};
</script>

<style lang="sass">
.csi-card-item-bar
  padding: 8px 16px

.csi-card-item-bar--negative
  background-color: $red-2
  color: black

.csi-card-item-bar--info
  background-color: $blue-2
  color: black

.csi-card-item-bar--positive
  background-color: $green-2
  color: black

.csi-card-item-bar--warning
  background-color: $orange-2
  color: black
</style>
