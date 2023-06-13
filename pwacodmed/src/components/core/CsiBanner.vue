<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-banner class="csi-banner" v-bind="attrs" v-on="listeners" :class="classes">
    <div class="row items-center q-pa-xs">
      <div class="col-auto q-pr-md" v-if="icon">
        <q-icon :name="icon" :size="iconSize" />
      </div>
      <div class="text-body1 col">
        <slot />
      </div>

    </div>


  </q-banner>
</template>

<script>

const VALID_TYPES = ['info', 'warning', 'positive', 'negative']
export default {
  name: "CsiBanner",
  props:{
    type:{type:String, default: 'info'},
    icon:{type:String, default: null},
    iconSize:{type:String, default: 'md'},
  },
  computed:{
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    },
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    classes(){
      let result = [];
      if(this.type) result.push(`csi-banner--${this.type}`);
      return result
    }
  }
}
</script>

<style lang="sass">
.csi-banner
  border-left-width: 8px
  border-left-style: solid
  border-left-color: transparent

.csi-banner
  &.csi-banner--info
    background-color: $blue-2
    border-left-color: $blue-4

  &.csi-banner--warning
    background-color: $orange-2
    border-left-color: $orange-5

  &.csi-banner--positive
    background-color: $green-3
    border-left-color: $green-6

  &.csi-banner--negative
    background-color: $red-3
    border-left-color: $red-6


</style>
