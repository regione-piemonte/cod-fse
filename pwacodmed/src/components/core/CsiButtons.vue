<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="csi-buttons row text-right q-gutter-sm" :class="classes">
    <slot />
  </div>
</template>

<script>
export default {
  name: "CsiButtons",
  components: {},
  props: {
    padding: { type: Boolean, required: false, default: false }
  },
  data() {
    return {};
  },
  computed: {
    classes() {
      return {
        "q-pa-md": this.padding,
        reverse: this.$q.screen.gt.xs
      };
    }
  },
  created() {},
  methods: {}
};
</script>

<style lang="sass">
.csi-buttons > .csi-button:not(.q-btn-round)
  width: 100%
.q-btn-dropdown
  width: 100%

@media (min-width: $breakpoint-sm-min)
  .csi-buttons > .csi-button:not(.q-btn-round)
    width: auto !important
  .q-btn-dropdown
    width: auto !important
</style>
