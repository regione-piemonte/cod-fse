<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="csi-maintenance">
    <div class="row items-center justify-around">
      <div class="col-10 col-sm-8 col-md-6 col-lg-4">
        <img
          src="images/manutenzione.svg"
          alt="Manutenzione"
          class="responsive"
        />
      </div>
    </div>

    <h1 class="text-h4 text-primary text-center q-my-md">
      Servizio non disponibile
    </h1>

    <p class="text-center text-body1">
      Servizio al momento non disponibile a causa di un intervento di
      manutenzione. <br />
      Ti consigliamo di riprovare piÃ¹ tardi. Ci scusiamo per il disagio.
    </p>

    <div class="q-mt-xl text-center">
      <csi-button @click="$emit('go-to-home')">Torna alla Home</csi-button>
    </div>
  </div>
</template>

<script>

export default {
  name: "CsiMaintenance",
  inheritAttrs: false,
  computed: {}
};
</script>
