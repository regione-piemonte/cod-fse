<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-card  class="cod-conversation-item cursor-pointer">

    <q-card-section class="row">
      <div class="col row items-center q-col-gutter-md">
        <div class="col-auto gt-sm">
          <q-skeleton type="QAvatar" class="bg-blue-1" />
        </div>
        <div class="col column q-col-gutter-y-xs">
          <div class="row justify-between">
            <div  class="col-12 col-md">
              <q-skeleton type="text" style="max-width:150px" />
            </div>


          </div>

          <div class="text-h5">
            <q-skeleton type="text" height="25px" style="max-width:200px" class="bg-grey-5"/>
          </div>
          <div class="text-italic cod-conversation-item--msg-text">
            <q-skeleton type="text" />
          </div>

          <div class=" text-caption text-accent q-mt-xs lt-md">
            <q-skeleton type="text" style="max-width:50px" />
          </div>

        </div>

      </div>
      <div class="col-auto q-pl-md">
        <q-icon name="more_vert" color="grey-6" size="md"/>
      </div>

    </q-card-section>
</q-card>
</template>

<script>
export default {
  name: "CodConversationItemSkeleton"
}
</script>

<style scoped>

</style>
