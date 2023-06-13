<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-card class="q-my-md ">
    <q-card-section class="bg-grey-3">
      <q-item class="q-mb-md q-pr-none">
        <q-item-section avatar>
          <q-skeleton type="QAvatar" class="bg-blue-1" />
        </q-item-section>
        <q-item-section>
          <q-item-label class="text-caption"><q-skeleton type="text" style="max-width:50px" /></q-item-label>
          <q-item-label><strong><q-skeleton type="text" style="max-width:200px" class="bg-grey-5"/></strong></q-item-label>
        </q-item-section>
        <q-item-section side>
          <div class="col-auto q-pl-md">
            <q-icon name="more_vert" color="grey-6" size="md"/>
          </div>
        </q-item-section>
      </q-item>


      <div class="row items-center justify-between">

        <q-item class="col-12 col-md">
          <q-item-section avatar>
            <q-skeleton type="QAvatar" class="bg-blue-1"  />
          </q-item-section>
          <q-item-section>
            <q-item-label class="text-caption"><q-skeleton type="text" style="max-width:50px"/></q-item-label>
            <q-item-label><q-skeleton type="text" style="max-width:200px" class="bg-grey-5" /></q-item-label>
          </q-item-section>

        </q-item>


      </div>

    </q-card-section>
    <q-separator/>
    <!-- ---------   LISTA MESSAGGI ------------------------>
    <!------------------------------------------------------------------------------------------------------------>
    <q-slide-transition>
      <q-card-section class="" >
        <div class="row justify-center q-my-md">
          <q-spinner-dots color="primary" size="40px"/>
        </div>
      </q-card-section>
    </q-slide-transition>


  </q-card>
</template>

<script>
export default {
  name: "CodConversationDetailSkeleton",
  methods:{
    isEvenNumber (i){
      return i % 2 === 0
    }
  }
}
</script>

<style scoped>

</style>
