<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="csi-card-item row">
    <template v-if="onSide">
      <div class="col-12">
        <slot name="header"></slot>
      </div>

      <div class="col-12 ">
        <slot name="primary"></slot>
      </div>

      <div class="col-12 column">

        <div>
          <slot name="secondary"></slot>
        </div>

        <div>
          <slot name="actions"></slot>
        </div>
      </div>
    </template>

   <template v-else>
     <div v-if="$q.screen.lt.sm" class="col-12">
       <slot name="header"></slot>
     </div>

     <div class="col-12 col-sm-3">
       <slot name="primary"></slot>
     </div>

     <div class="col-12 col-sm-9 column">
       <div v-if="$q.screen.gt.xs">
         <slot name="header"></slot>
       </div>

       <div>
         <slot name="secondary"></slot>
       </div>

       <!-- Spacer -->
       <!--<div class="col"></div>-->

       <div>
         <slot name="actions"></slot>
       </div>
     </div>
   </template>
  </div>
</template>

<script>
export default {
  name: "CsiCardItem",
  props:{
    onSide:{ type: Boolean, default: false}
  },
  data() {
    return {};
  }
};
</script>

<style lang="sass"></style>
