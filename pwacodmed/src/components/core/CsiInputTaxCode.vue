<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-input
    v-bind="attrs"
    v-on="listeners"
    :required="required"
    class="csi-input-tax-code"
    name="tax-code"
    @input="onInput"
    no-error-icon
  >
    <slot/>
    <slot slot="prepend" name="prepend"/>
    <slot slot="append" name="append"/>
    <slot slot="before" name="before"/>
    <slot slot="after" name="after"/>
    <slot slot="error" name="error"/>
    <slot slot="hint" name="hint"/>
    <slot slot="counter" name="counter"/>
    <slot slot="loading" name="loading"/>
  </q-input>
</template>

<script>
import {hasValidCin, hasValidLength, LENGTH, TAX_CODE_MASK, TEMPORARY_LENGTH} from "src/services/tax-code";

export default {
  name: "CsiInputTaxCode",
  inheritAttrs: false,
  props: {
    temporary: {type: Boolean, required: false, default: false},
    required: {type: Boolean, required: false, default: false},
    noUser: {type: Boolean, required: false, default: false},
  },
  computed: {
    listeners() {
      const {input, ...listeners} = this.$listeners;
      return listeners;
    },
    attrs() {
      const {...attrs} = this.$attrs;
      if (!("type" in attrs)) attrs.type = "text";
      if (!("label" in attrs)) attrs.label = "Codice fiscale";
      if (!("maxlength" in attrs)) attrs.maxlength = LENGTH.toString();
      if (!("upperCase" in attrs)) attrs.upperCase = true;
      if (!("autocomplete" in attrs)) attrs.autocomplete = "on";
      if (!("counter" in attrs)) attrs.counter = true;
      attrs.maxlength = LENGTH.toString();
      attrs.mask = TAX_CODE_MASK;
      attrs.unmaskedValue = true;

      attrs.rules = attrs.rules || [];
      attrs.rules = attrs.rules.concat(this.defaultRules);
      return attrs;
    },
    ruleRequired() {
      return v => this.$rules.required(v) || "Campo obbligatorio";
    },
    ruleLength() {
      let lengthList = [LENGTH];
      let mex = `Deve essere di ${LENGTH} caratteri`;

      if (this.temporary) {
        lengthList.push(TEMPORARY_LENGTH);
        mex = `Deve essere di ${TEMPORARY_LENGTH} o ${LENGTH} caratteri`;
      }

      return v => !v || this.$rules.lengthIn(v, lengthList) || mex;
    },
    ruleRegex() {
      // Se la lunghezza Ã¨ quella di un CF normale => deve essere formato da caratteri alphanumerici
      return v =>
        (!!v || !this.$rules.length(LENGTH) ||
          this.$rules.alphaNum(v)) ||
        `Deve essere composto solo da numeri o lettere`;
    },
    ruleRegexTemporary() {
      // Se la lunghezza Ã¨ quella di un CF temporaneo => deve essere formato da soli numeri
      return v => (!v ||
        !this.$rules.length(v, TEMPORARY_LENGTH) ||
        this.$rules.numeric(v)) ||
        `Deve essere composto solo da numeri`;
    },
    ruleTaxCodeCin() {
      return (v) => {
        return (!v || (!hasValidLength(v) || hasValidCin(v))) || "Il codice di controllo non Ã¨ corretto, potrebbero esserci degli errori di battitura"
      }
    },
    defaultRules() {
      let rules = [
        this.ruleLength,
        this.ruleRegex,
        this.ruleRegexTemporary
      ]

      if (this.required)
        rules.push(this.ruleRequired)

      if(this.noUser)
        rules.push(this.ruleSameUser)

      return rules
    },
    ruleSameUser() {
      let userTaxCode = this.$store.getters['getTaxCode']
      return (v) => userTaxCode !== v || "Deve essere diverso dal tuo codice fiscale"
    }
  },
  methods: {
    onInput(val) {
      let emitValue = val ? val.toUpperCase() : "";
      this.$emit("input", emitValue);
    }
  }
};
</script>
