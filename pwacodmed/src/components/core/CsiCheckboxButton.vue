<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->


<template>
  <div
    class="sepac-checkbox-button"
    tabindex="0"
    @keyup.space.enter.prevent.stop="onKeyup"
  >
    <input
      :value="value"
      type="checkbox"
      :id="id"
      :name="name"
      @input="onInput"
    />

    <div class="row q-col-gutter-sm items-center">
      <div class="col-auto">
        <template v-if="!value">
          <q-icon
            name="check_box_outline_blank"
            :size="size"
            aria-hidden="true"
            @click="onToggle"
          />
        </template>
        <template v-else>
          <q-icon
            name="check_box"
            :size="size"
            :color="color"
            aria-hidden="true"
            @click="onToggle"
          />
        </template>
      </div>

      <div class="col">
        <label :for="id">
          <slot>
            {{ label }}
          </slot>
        </label>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "CsiCheckboxButton",
  props: {
    value: { type: Boolean, required: false, default: false },
    label: { type: String, required: false, default: null },
    id: { type: String, required: false, default: null },
    name: { type: String, required: false, default: null },
    size: { type: String, required: false, default: "xs" },
    color: { type: String, required: false, default: "primary" }
  },
  data() {
    return {};
  },
  created() {},
  methods: {
    onInput(event) {
      this.onToggle(event);
    },
    onToggle(event) {
      this.$emit("input", !this.value, event);
    },
    onKeyup(event) {
      this.onToggle(event);
    }
  }
};
</script>

<style lang="sass">
.csi-checkbox-button
  cursor: pointer
  input
    position: absolute
    z-index: -1
    opacity: 0
  label
    cursor: pointer
    user-select: none
    vertical-align: middle
  &:focus
    outline: 2px solid darkorange

</style>
