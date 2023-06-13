<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-card >
    <q-card-section>
      <div class="row items-center  q-col-gutter-sm" :class="{'reverse': $q.screen.gt.xs}">
        <div style="min-width: 200px" class="col-12 col-sm-auto self-end">
          <q-skeleton
            type="QBtn"
            width="100%"
            class="inline-block bg-blue-8"
          />
        </div>
        <div style="min-width: 150px" class="col-12 col-sm-auto self-end">
          <q-skeleton
            type="QBtn"
            width="100%"
            class="inline-block bg-grey-5"
          />
        </div>



      </div>
    </q-card-section>
    <q-markup-table>


      <thead>
      <tr class="q-px-none">
        <th  class="text-center" style="width: 20px">
          <div class="q-px-sm">
            <q-skeleton size="22px" />
          </div>

        </th>
        <th >
          <q-skeleton animation="blink" type="text" width="60px" class="bg-grey-5"/>
        </th>
        <th >
          <q-skeleton animation="blink" type="text" width="60px"  class="bg-grey-5" />
        </th>
        <th >
          <q-skeleton animation="blink" type="text" width="70px"  class="bg-grey-5" />
        </th>
        <th >
          <q-skeleton animation="blink" type="text" width="70px"  class="bg-grey-5" />
        </th>
        <th >
          <q-skeleton animation="blink" type="text" width="50px"  class="bg-grey-5" />
        </th>
      </tr>
      </thead>

      <tbody>
      <tr v-for="n in 5" :key="n">
        <td  class="text-center" style="width: 20px">
          <div class="q-px-sm">
          <q-skeleton size="22px" />
          </div>
        </td>
        <td class="text-right">
          <q-skeleton animation="blink" type="text" width="60px" />
        </td>
        <td class="text-right">
          <q-skeleton animation="blink" type="text" width="60px" />
        </td>
        <td class="text-right">
          <q-skeleton animation="blink" type="text" width="50px" />
        </td>
        <td class="text-right">
          <q-skeleton animation="blink" type="text" width="45px" />
        </td>
        <td class="text-right">
          <q-skeleton animation="blink" type="text" width="45px" />
        </td>
      </tr>
      </tbody>
    </q-markup-table>
  </q-card>
</template>

<script>
export default {
  name: "CodPatientListSkeleton"
}
</script>

<style scoped>

</style>
