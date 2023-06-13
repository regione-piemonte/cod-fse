<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-page >

      <div class="bg-secondary  q-py-xl"></div>
      <!-- CHI SEI -->
      <div class="text-right relative-position q-mb-xl" >
        <q-avatar
          size="80px"
          font-size="32px"
          color="white"
          class="absolute-center"
          text-color="primary"
        >
          <strong>{{ user.nome.charAt(0) }}{{ user.cognome.charAt(0) }}</strong>
        </q-avatar>

      </div>
      <div class="text-center q-pt-lg">
        <div class="text-h1 q-mt-sm">{{ user.nome }} {{ user.cognome }}</div>
        <div class="text-h6 q-mt-xs">{{ user.codice_fiscale }}</div>
      </div>
<!--      &lt;!&ndash;aggiunta tab &ndash;&gt;-->
<!--      <q-tabs-->
<!--        no-caps-->
<!--        class="q-pt-md"-->
<!--        active-color="primary"-->
<!--      >-->
<!--        <q-route-tab-->
<!--         exact-->
<!--          label="Anagrafica"-->
<!--          :to="USER_ANAGRAPHICS"-->
<!--        />-->

<!--        <q-route-tab-->
<!--          exact-->
<!--          label="Contatti"-->
<!--          :to="USER_CONTACTS"-->
<!--        />-->

<!--        <q-route-tab-->
<!--          exact-->
<!--          label="Notifiche"-->
<!--          :to="USER_NOTIFICATION_PREFERENCES"-->
<!--        />-->
<!--      </q-tabs>-->

  <div>
      <keep-alive :include="keepAlive">
        <router-view></router-view>
      </keep-alive>
    </div>
  </q-page>
</template>

<script>



export default {
  name: "PageProfile",
  data(){
    return{

      keepAlive: [

      ],
    }
  },
  created() {

  },
  computed:{
    user() {
      return this.$store.getters["getUser"];
    },

  }
}
</script>

<style scoped>

</style>
