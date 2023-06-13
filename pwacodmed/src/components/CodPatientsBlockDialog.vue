<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-dialog
    ref="patientBlockDialog"
    v-bind="attrs"
    v-on="listeners"
    :maximized="$q.screen.lt.md"
  >
    <q-card style="max-width: 800px">
      <q-toolbar>
        <q-toolbar-title>Disabilita assistiti</q-toolbar-title>
        <q-btn v-close-popup dense flat icon="close" round />
      </q-toolbar>
      <q-card-section>
        <div class="q-mb-md">
          <template v-if="nPatients === 1 && patients.length > 0">
            <q-item>
              <q-item-section avatar>
                <q-icon name="img:icone/avatar-uomo.svg" size="lg" />
              </q-item-section>
              <q-item-section>
                <q-item-label class="text-caption">Assistito</q-item-label>
                <q-item-label
                  ><strong
                    >{{ patients[0].nome }} {{ patients[0].cognome }}</strong
                  ></q-item-label
                >
              </q-item-section>
            </q-item>
          </template>
          <template v-else>
            <p>
              <strong>{{ nPatients }} assistiti selezionati</strong>
            </p>
          </template>
        </div>

        <div class="q-mb-xl">
          <q-input
            v-model="blockMotivation"
            :label="motivationBlockLabel"
            type="textarea"
          />
        </div>

        <csi-buttons>
          <csi-button :loading="isBlocking" @click="onConfirm"
            >Conferma</csi-button
          >
          <csi-button v-close-popup outline>Annulla</csi-button>
        </csi-buttons>
      </q-card-section>
    </q-card>
  </q-dialog>
</template>

<script>
import { managePatients } from "src/services/api";
import { apiErrorNotify } from "src/services/utils";

export default {
  name: "CodPatientsBlockDialog",
  props: {
    patients: { type: Array, default: () => [] }
  },
  data() {
    return {
      isBlocking: false,
      blockMotivation: ""
    };
  },
  computed: {
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    },
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    nPatients() {
      return this.patients?.length;
    },
    isSinglePatient() {
      return this.nPatients === 1;
    },
    motivationBlockLabel() {
      let str = this.isSinglePatient ? "allâassistito" : "agli assistiti";
      return `Motivazione (facoltativa, la motivazione sarÃ  visibile ${str})`;
    }
  },
  methods: {
    async onConfirm() {
      this.isBlocking = true;
      let patientsTaxCode = this.patients?.map(p => p.codice_fiscale);
      let payload = {
        abilitazione: false,
        assistiti: patientsTaxCode,
        motivazione_medico: this.blockMotivation
      };
      this.$refs.patientBlockDialog.hide();
      try {
        let response = await managePatients(payload);
        await this.$emit('refreshList')
        this.$q.notify({
          type: "positive",
          message: "Assistiti disabilitati correttamente",
          textColor: "black"
        });
      } catch (error) {
        let message = "Non Ã¨ stato possibile disabilitare l'assistito";
        apiErrorNotify({ error, message });
      }
      this.isBlocking = false;
      this.$emit("on-block", this.blockMotivation);

    }
  }
};
</script>

<style scoped></style>
