<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-footer v-bind="attrs" v-on="listeners" class="csi-layout-footer">
    <!-- RATING -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <template v-if="rating">
      <div class="csi-layout-footer__rating row justify-center q-col-gutter-x-md">
        <div class="col-auto">
          <q-icon name="chat_bubble_outline" size="md" />
        </div>

        <div class="col-auto cursor-pointer" @click="$emit('click-rating')">
          <div class="text-h6 text-bold" style="line-height: 1">
            Valuta questo servizio
          </div>
          <div class="text-body1 q-mt-xs">
            Aiutaci a migliorare
          </div>
        </div>
      </div>
    </template>

    <!-- HELP -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <div class="csi-layout-footer__help">
      <div class="row q-col-gutter-xl">
        <div class="col-12 col-md-auto text-center">
          <img
            src="images/logo-regione-piemonte.svg"
            alt="Regione Piemonte"
            class="csi-layout-footer__logo-regione"
          />
        </div>

        <div class="col-12 col-md-auto text-center">
          <img
            src="images/logo-fesr.svg"
            alt="Fondo europeo di sviluppo regionale"
            class="csi-layout-footer__logo-fesr"
          />
        </div>

        <template v-if="helpContacts || helpFaq">
          <q-space class="gt-sm" />

          <div class="col-12 col-md-auto column csi-layout-footer__help__links">
            <div class="text-h6 text-bold">
              Hai bisogno di aiuto?
            </div>

            <div class="row justify-center q-mt-sm">
              <template v-if="helpContacts">
                <q-btn
                  flat
                  no-caps
                  :label="helpContactsLabel"
                  @click="$emit('click-help-contacts')"
                />
              </template>

              <template v-if="helpContacts && helpFaq">
                <q-separator vertical spaced color="white" />
              </template>

              <template v-if="helpFaq">
                <q-btn
                  flat
                  no-caps
                  :label="helpFaqLabel"
                  @click="$emit('click-help-faq')"
                />
              </template>
            </div>
          </div>
        </template>
      </div>
    </div>

    <!-- LINKS -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <div
      class="csi-layout-footer__links row justify-between items-center q-col-gutter-y-lg no-margin"
    >
      <div class="col-12 col-sm-auto">
        <div class="text-h6 text-bold">
          Sistemapiemonte
        </div>
        <div class="text-caption">
          Un servizio a cura della regione Piemonte
        </div>
        <div class="text-caption">
          P.Iva 02843860012 - CF 80087670016
        </div>
      </div>

      <div class="col-12 col-sm-auto">
        <img
          src="images/logo-csi.svg"
          alt="Logo CSI"
          class="csi-layout-footer__logo-csi"
        />
      </div>
    </div>
  </q-footer>
</template>

<script>
export default {
  name: "csiLayoutFooter",
  inheritAttrs: false,
  props: {
    rating: { type: Boolean, required: false, default: false },
    helpContacts: { type: Boolean, required: false, default: false },
    helpContactsLabel: { type: String, required: false, default: "Contatti" },
    helpFaq: { type: Boolean, required: false, default: false },
    helpFaqLabel: { type: String, required: false, default: "FAQ" }
  },
  data() {
    return {};
  },
  computed: {
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    attrs() {
      const { ...attrs } = this.$attrs;
      if (!("elevated" in attrs)) attrs.elevated = true;
      return attrs;
    }
  }
};
</script>

<style lang="sass">
  .csi-layout-footer__rating
    background-color: $secondary
    color: #FFF
    padding: 32px 16px

  .csi-layout-footer__help
    padding: 32px 16px
    background-color: $primary

  .csi-layout-footer__links
    background-color: $blue-10
    color: #FFF
    padding: 0 16px 32px 16px
    text-align: center

  .csi-layout-footer__logo-regione
    width: 160px
    height: auto

  .csi-layout-footer__logo-csi
    max-width: 180px
    height: auto

  .csi-layout-footer__logo-fesr
    width: 280px
    height: auto

  .csi-layout-footer__help__links
    color: #FFFFFF
    text-align: center
</style>
