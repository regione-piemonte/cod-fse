<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="csi-page-title row items-center no-wrap">
    <div class="col-auto">
      <slot name="back">
        <q-btn
          v-if="!noBack"
          flat
          round
          dense
          icon="arrow_back"
          class="q-mr-xs"
          @click="onBack"
        />
      </slot>
    </div>

    <div class="col">
      <h1 class="text-h5 text-bold q-my-none">
        <slot />
      </h1>
    </div>
  </div>
</template>

<script>
export default {
  name: "CsiPageTitle",
  props: {
    noBack: { type: Boolean, default: false },
    back:{ default:null}
  },
  data() {
    return {
    };
  },

  methods: {
    onBack(...args) {
      let eventName = "back";
      if (eventName in this.$listeners) return this.$emit(eventName, ...args);

      console.debug(`default @${eventName}`);
      if(this.back)
        this.$router.push(this.back);
      else{
        this.$router.back();
      }
    }
  }
};
</script>
