<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-card class="csi-faq-list-item-wrapper">
    <q-expansion-item v-bind="attrs" v-on="listeners" class="csi-faq-list-item">
      <template v-slot:header>
        <q-item-section
          class="csi-faq-list-item__header__text text-body1 text-weight-bold"
        >
          <slot name="title">
            <div>
              <template v-if="number">
                <span class="q-mr-sm" style="white-space: pre"
                  >{{ number | padLeft }}.</span
                >
              </template>

              {{ title }}
            </div>
          </slot>
        </q-item-section>
      </template>

      <q-card class="csi-faq-list-item__body">
        <q-separator inset />
        <q-card-section>
          <slot />
        </q-card-section>
      </q-card>
    </q-expansion-item>
  </q-card>
</template>

<script>
export default {
  name: "csiFaqListItem",
  inheritAttrs: false,
  props: {
    title: { type: String, required: false, default: "" },
    number: { type: [String, Number], required: false, default: "" }
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    }
  }
};
</script>

<style lang="sass">
.csi-faq-list-item-wrapper:nth-of-type(2n) > .csi-faq-list-item
  border-left: 4px solid $indigo-6

.csi-faq-list-item-wrapper:nth-of-type(2n + 1) > .csi-faq-list-item
  border-left: 4px solid $teal-6

.csi-faq-list-item__header__number
  min-width: 32px
  align-items: flex-end
  font-weight: map-get($text-weights, 'bold')
  color: $primary
</style>
