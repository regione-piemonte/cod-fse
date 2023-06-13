<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div
    class="csi-card-item-primary no-margin bg-grey-3 q-py-md"
    :class="classes"
  >
    <slot>
      <div class="col-auto" :class="{ 'text-center': $q.screen.gt.xs }">
        <slot name="icon" />
      </div>

      <div class="col-auto" :class="{ 'text-center': $q.screen.gt.xs }">
        <slot name="text" />
      </div>
    </slot>
  </div>
</template>

<script>
export default {
  name: "CsiCardItemPrimary",
  props:{
    inline:{type:Boolean, default:false}
  },
  data() {
    return {};
  },
  computed:{
    classes(){
      let classes=null

      if(this.inline)
        classes = 'row q-col-gutter-x-md items-center full-height'
      else
        classes = 'q-col-gutter-y-md full-height'

      return classes
    }
  }
};
</script>

<style lang="sass"></style>
