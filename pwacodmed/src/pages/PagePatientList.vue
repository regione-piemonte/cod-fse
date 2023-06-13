<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="page-patient-list q-pb-xl">
    <csi-page-title :back="SETTINGS">{{ title }}</csi-page-title>
    <!-- FILTRI -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <q-card class="bg-transparent" flat>
      <q-card-section class="q-px-none">
        <template>
          <div class="text-right">
            <q-btn
              flat
              text-color="black"
              icon="filter_list"
              label="Filtri"
              @click="openFilters"
            />
          </div>

          <q-slide-transition>
            <div v-show="isOpenFilters">
              <div class="row items-center q-col-gutter-lg q-mb-lg">
                <div class="col-12 col-md-4">
                  <q-input v-model="patientName" label="Nome" dense clearable />
                </div>

                <div class="col-12 col-md-4">
                  <q-input
                    v-model="patientSurname"
                    label="Cognome"
                    dense
                    clearable
                  />
                </div>
                <div class="col-12 col-md-4">
                  <q-input
                    :mask="TAX_CODE_MASK"
                    v-model="patientTaxCode"
                    label="Codice fiscale"
                    dense
                    clearable
                  />
                </div>
                <div class="col-12 col-md-3">
                  <q-input
                    mask="###"
                    v-model="minAge"
                    label="EtÃ  minima"
                    dense
                    clearable
                  />
                </div>
                <div class="col-12 col-md-3">
                  <q-input
                    mask="###"
                    v-model="maxAge"
                    label="EtÃ  massima"
                    dense
                    clearable
                  />
                </div>
                <div class="col-12 col-md-3">
                  <q-select
                    v-model="gender"
                    label="Sesso"
                    :options="GENDER_OPTIONS"
                    dense
                    clearable
                  />
                </div>
                <div class="col-12 col-md-3">
                  <csi-buttons>
                    <csi-button no-min-width outline @click="setFilters"
                      >Filtra</csi-button
                    >
                    <csi-button no-min-width outline @click="resetFilters"
                      >Reset</csi-button
                    >
                  </csi-buttons>
                </div>
              </div>
            </div>
          </q-slide-transition>
        </template>
      </q-card-section>
    </q-card>
    <q-card-section v-if="alertPatients">
      <csi-banner type="warning" class="q-mb-lg">
        Gli assistiti segnalati con
        <q-icon name="warning" color="red" size="2rem" /> non sono abilitabili
      </csi-banner>
    </q-card-section>

    <template v-if="!isLoading">
      <template v-if="patientList.length > 0">
        <q-table
          :data="patientList"
          :columns="TABLE_PATIENT_COLUMNS"
          row-key="codice_fiscale"
          selection="multiple"
          :selected-rows-label="
            () => {
              return '';
            }
          "
          :selected.sync="selectedPatients"
          :rows-per-page-options="[]"
          @request="loadPatientsList"
          responsive
        >
          <!-- SLOT HEADER E COLONNE -->
          <template v-slot:header="props">
            <q-tr :props="props">
              <q-th>
                <!-- CHECK SELEZIONA TUTTI -->
                <q-checkbox
                  v-model="selectAllCheck"
                  v-if="selectedPatients && selectedPatients.length < limit"
                  v-on:click.native="selectAllpatients"
                ></q-checkbox
              ></q-th>
              <q-th v-for="col in props.cols" :key="col.name" :props="props">
                {{ col.label }}
              </q-th>
            </q-tr>
          </template>
          <template v-slot:header-selection="scope">
            <q-checkbox color="primary" v-model="scope.selected" />
          </template>
          <!-- CHECKBOX SINGOLA -->
          <template v-slot:body-selection="scope">
            <template v-if="notAble(scope.row)">
              <q-icon name="warning" color="red" size="2rem" />
              <!-- <template v-if="statusPatient(scope.row)">{{
                statusPatient(scope.row)
              }}</template> -->
            </template>
            <template v-else>
              <q-checkbox
                color="primary"
                v-model="scope.selected"
                v-if="disableCheck(scope.row)"
              />
            </template>
          </template>
          <!-- PULSANTI SOPRA LA TABELLA-->
          <template v-slot:top>
            <div
              class="row items-center q-col-gutter-md full-width"
              :class="{ reverse: $q.screen.gt.sm }"
            >
              <div class="col-12 col-md-auto">
                <csi-buttons>
                  <q-btn-dropdown
                    v-if="!blocked"
                    color="primary"
                    label="Aggiungi assistiti"
                  >
                    <q-list>
                      <q-item
                        v-close-popup
                        clickable
                        @click="showPatientsSearchDialog = true"
                      >
                        <q-item-section>
                          <q-item-label>Cerca assistiti</q-item-label>
                        </q-item-section>
                      </q-item>
                      <q-item
                        v-close-popup
                        clickable
                        @click="showPatientsEnablementALLDialog = true"
                        :disable="getEnableAllModalRunningBlock"
                      >
                        <q-item-section>
                          <q-item-label
                            >Aggiungi tutti i tuoi assistiti</q-item-label
                          >
                        </q-item-section>
                      </q-item>
                    </q-list>
                  </q-btn-dropdown>

                  <template v-if="blocked">
                    <csi-button
                      outline
                      no-min-width
                      :disable="nSelectedPatients === 0"
                      label="Abilita assistiti"
                      @click="enablePatients"
                      :loading="isEnabling"
                    />
                  </template>
                  <template v-else>
                    <csi-button
                      color="negative"
                      :disable="nSelectedPatients === 0"
                      no-min-width
                      outline
                      label="Disabilita assistiti"
                      @click="manageEnablement"
                      :loading="isEnabling"
                    />
                  </template>
                </csi-buttons>
              </div>

              <div
                class="text-caption text-italic text-accent text-bold col-12 col-md"
              >
                {{ selectionMsg }}
                <p v-if="selectedPatients && selectedPatients.length >= limit">
                  Numero massimo selezionabile {{ limit }}
                </p>
              </div>
            </div>
          </template>
          <template v-slot:bottom> </template>
        </q-table>
        <template v-if="totalElements > patientList.length">
          <div class="q-mt-md text-center">
            <q-btn
              :loading="isLoading"
              color="primary"
              dense
              flat
              @click="loadPatientsList"
            >
              Carica altri assistiti
            </q-btn>
          </div>
        </template>
      </template>
      <template v-else>
        <csi-buttons>
          <q-btn-dropdown
            v-if="!blocked"
            color="primary"
            label="Aggiungi assistiti"
            auto-close
          >
            <q-list>
              <q-item clickable @click="showPatientsSearchDialog = true" v-close-popup>
                <q-item-section>
                  <q-item-label>Cerca assistiti</q-item-label>
                </q-item-section>
              </q-item>
              <q-item clickable @click="showPatientsEnablementALLDialog = true" v-close-popup
              :disable="getEnableAllModalRunningBlock">
                <q-item-section>
                  <q-item-label>Aggiungi tutti i tuoi assistiti</q-item-label>
                </q-item-section>
              </q-item>
            </q-list>
          </q-btn-dropdown>
        </csi-buttons>
      </template>
      <div v-if="noResults">
        <csi-banner type="info" class="q-mt-md">
          Nessun assistito trovato in base ai filtri di ricerca.
        </csi-banner>
      </div>
      <div v-if="checkAgeError">
        <csi-banner type="negative">
          L'etÃ  minima deve essere inferiore alla massima
        </csi-banner>
      </div>
    </template>

    <template v-else>
      <cod-patient-list-skeleton />
    </template>
    <cod-patients-search-dialog
      v-if="showPatientsSearchDialog"
      v-model="showPatientsSearchDialog"
      @refreshList="refreshList"
    />
    <cod-patients-block-dialog
      v-model="showPatientsBlockDialog"
      :patients="selectedPatients"
      @refreshList="refreshList"
    />
    <cod-patient-enable-all-dialog
      v-model="showPatientsEnablementALLDialog"
      @refreshList="refreshList"
    />
  </div>
</template>

<script>
import { SETTINGS } from "src/router/routes";
import { TAX_CODE_MASK } from "src/services/tax-code";
import {
  CONVERSATION_STATUS_MAP,
  GENDER_OPTIONS,
  TABLE_PATIENT_COLUMNS,
} from "src/services/config";
import CodPatientsSearchDialog from "components/CodPatientsSearchDialog";
import CodPatientEnablementDialog from "components/CodPatientEnablementDialog";
import { getPatientSelectionMessage } from "src/services/business-logic";
import CodPatientEnableAllDialog from "components/CodPatientEnableAllDialog";
import CodPatientsBlockDialog from "components/CodPatientsBlockDialog";
import {
  getPatientsList,
  managePatients,
  getDoctorInfo,
} from "src/services/api";
import { apiErrorNotify } from "src/services/utils";
import CodPatientListSkeleton from "components/loaders/CodPatientListSkeleton";

const LIMIT = 150;
export default {
  name: "PagePatientList",
  components: {
    CodPatientListSkeleton,
    CodPatientsBlockDialog,
    CodPatientEnableAllDialog,
    CodPatientsSearchDialog,
  },
  data() {
    return {
      LIMIT,
      TABLE_PATIENT_COLUMNS,
      GENDER_OPTIONS,
      TAX_CODE_MASK,
      SETTINGS,
      blocked: null,
      isLoading: false,
      patientName: null,
      patientSurname: null,
      patientTaxCode: null,
      minAge: null,
      maxAge: null,
      gender: null,
      patientList: [],
      isOpenFilters: true,
      hasMorePatients: false,
      offset: 0,
      patientStatus: null,
      selectedPatients: [],
      pagination: null,
      showPatientsSearchDialog: false,
      showPatientsBlockDialog: false,
      showPatientsEnablementALLDialog: false,
      isEnabling: false,
      checkAgeError: false,
      noResults: false,
      totalElements: null,
      selectAllCheck: false,
      listResponse: [],
      responseTotale: null,
      alertPatients: false,
      limit: LIMIT,
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    role() {
      return this.$store.getters["getRoleCode"];
    },
    placement() {
      return this.$store.getters["getPlacementCode"];
    },
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
    getEnableAllModalRunningBlock() {
      return this.$store.getters["getEnableAllModalRunningBlock"];
    },
    nSelectedPatients() {
      return this.selectedPatients?.length;
    },
    title() {
      if (this.blocked)
        return `Assistiti disabilitati (${this.doctorInfo?.n_pazienti_bloccati})`;
      else {
        return `Assistiti abilitati (${this.doctorInfo?.n_pazienti_abilitati})`;
      }
    },

    selectionMsg() {
      return getPatientSelectionMessage(this.nSelectedPatients, LIMIT);
    },
    hasEnablingRequest() {
      return this.doctorInfo?.abilitazione_assistiti;
    },
    selectAll: {
      get() {
        let patientsListLength = this.patientList?.length;
        if (this.nSelectedPatients <= 0) return false;
        if (this.nSelectedPatients >= patientsListLength) return true;
        return null;
      },
      set() {
        let patientsListLength = this.patientList?.length;
        if (
          this.nSelectedPatients <= 0 ||
          this.nSelectedPatients < patientsListLength
        ) {
          this.selectPatientsAll();
        } else {
          this.disablePatientsAll();
        }
      },
    },
  },
  created() {
    let { abilitati, bloccati } = this.$route.query;
    if (!abilitati && !bloccati) this.$router.replace(SETTINGS);

    if (abilitati) {
      this.patientStatus = CONVERSATION_STATUS_MAP.ACTIVE;
      this.blocked = false;
    } else if (bloccati) {
      this.patientStatus = CONVERSATION_STATUS_MAP.BLOCKED;
      this.blocked = true;
    }

    this.pagination = {
      page: 1,
      rowsPerPage: LIMIT,
    };
    this.isLoading = true;
    this.loadPatientsList({ pagination: this.pagination });
  },
  methods: {
    openFilters() {
      this.isOpenFilters = !this.isOpenFilters;
    },
    setFilters() {
      this.checkAgeError = false;
      this.patientList = [];
      this.pagination = {
        page: 1,
        rowsPerPage: LIMIT,
      };
      this.noResults = false;
      let isOkAge = this.maxAge >= this.minAge ? false : true;
      if (isOkAge) {
        this.checkAgeError = true;
        return;
      }
      this.isLoading = true;
      this.loadPatientsList({ pagination: this.pagination });
    },
    resetFilters() {
      this.offset = 0;
      this.patientName = null;
      this.patientSurname = null;
      this.patientTaxCode = null;
      this.minAge = null;
      this.maxAge = null;
      this.gender = null;
      this.checkAgeError = false;
      this.noResults = false;
      this.selectedPatients = [];
      this.selectAllCheck = false;
      this.patientList = [];
      this.listResponse = [];
      this.alertPatients = false;
      this.responseTotale = null;
    },
    //METODO IN GENERE CHIAMATO DA ALTRI COMPONENTI TIPO MODALI PER RICARICARE LISTA
    refreshList() {
      console.log('dentro refresh di page patient list')
      this.selectedPatients = [];
      this.loadPatientsList("reload");
    },

    selectAllpatients() {
      if (this.selectAllCheck) {
        if (this.patientList.length > LIMIT) {
          this.selectedPatients = this.patientList.slice(0, LIMIT);
        } else {
          this.selectedPatients = this.patientList;
        }
      } else {
        this.selectedPatients = [];
      }
    },

    disableCheck(patient) {
      if (this.selectedPatients && this.selectedPatients.length >= LIMIT) {
        let isClickable =
          this.selectedPatients
            .map((e) => e.codice_fiscale)
            .indexOf(patient.codice_fiscale) !== -1
            ? true
            : false;
        if (isClickable == false) {
          return false;
        } else {
          return true;
        }
      }
      return true;
    },
    //METODO PER VERIFICARE SE E' SBLOCCABILE
    notAble(patient) {
      if (this.listResponse) {
        for (let i = 0; i < this.listResponse.length; i++) {
          if (this.listResponse[i] == patient.codice_fiscale) {
            return true;
          }
        }
      } else {
        return false;
      }
    },
    statusPatient(patient) {
      return this.responseTotale.find(
        (x) => x.codice_fiscale === patient.codice_fiscale
      ).stato_abilitazione;
    },
    async loadPatientsList(props) {
      let params = {
        limit: LIMIT,
        offset: props === "reload" ? 0 : this.offset,
        nome: this.patientName ? this.patientName : null,
        cognome: this.patientSurname ? this.patientSurname : null,
        codice_fiscale: this.patientTaxCode ? this.patientTaxCode : null,
        eta_min: this.minAge ? this.minAge : null,
        eta_max: this.maxAge ? this.maxAge : null,
        stato: this.patientStatus,
        sesso: this.gender ? this.gender.value : null,
      };
      // GESTIONE ERRORI MESSAGGIO RECUPERATO DA RESPONSE PROVVISORIO
      try {
        let response = await getPatientsList({ params });
        this.totalElements = parseInt(response.headers["x-total-elements"]);
        let list = response.data;

        if (props === "reload") {
          this.patientList = list;
        } else {
          this.patientList = [...this.patientList, ...list];
        }
        this.noResults = list.length === 0 ? true : false;
        if (this.totalElements > this.patientList.length) {
          this.offset =
            this.offset <= this.totalElements
              ? (this.offset += LIMIT)
              : this.totalElements;
        }
      } catch (error) {
        let message = error?.response?.data?.detail[0].valore ?? "";
        apiErrorNotify({ error, message });
      }
      if (props === "reload") {
        await this.callDoctorInfo();
      }
      this.isLoading = false;
    },
    async callDoctorInfo() {
      try {
        let { data: info } = await getDoctorInfo();
        this.$store.dispatch("setDoctorInfo", { info });
      } catch (error) {
        let statusCode = error?.response?.status;
        if (statusCode === 404) {
          this.$router.replace("/impostazioni");
        }
      }
    },
    selectPatientsAll() {
      this.selectedPatients = this.patientList;
    },
    manageEnablement() {
      if (this.nSelectedPatients > 0) this.showPatientsBlockDialog = true;
    },
    async enablePatients() {
      if (this.nSelectedPatients <= 0) return;
      this.isEnabling = true;
      let patientsTaxCode = this.selectedPatients?.map((p) => p.codice_fiscale);

      let payload = {
        abilitazione: true,
        assistiti: patientsTaxCode,
        motivazione_medico: null,
      };

      try {
        this.responseTotale = null;
        let response = await managePatients(payload);
        // LISTA CODICI FISCALI PER VERIFICARE CHI ABBIAMO INVIATO COME RICHIESTA
        let listOfTaxCode = response.data?.map((p) => p.codice_fiscale);
        // RESPONSE TOTALE CI SERVIRA' PER MAPPARE TUTTI GLI STATI
        this.responseTotale = response.data;
        // LISTA STATUS PER GESTIRE SNACKBAR
        let listStatus = response.data?.map((p) => p.stato_abilitazione);
        this.selectedPatients = [];
        await this.loadPatientsList("reload");
        // LIST RESPONSE SERVE PER CONTROLLARE SE ALCUNI UTENTI NON POSSONO ESSERE DISABILITATI
        this.listResponse = [...this.listResponse, ...listOfTaxCode];
        // LA SNACKBAR DIVENTA INFO SE NON TUTTI SONO ABILITABILI
        if (listStatus.includes("NON_ABILITABILE")) {
          this.alertPatients = true;
          this.$q.notify({
            type: "info",
            message: "Non tutti gli assistiti sono abilitabili",
            textColor: "black",
          });
        } else {
          this.$q.notify({
            type: "positive",
            message: "Assistiti abilitati  correttamente",
            textColor: "black",
          });
        }
      } catch (error) {
        let message =
          "Non Ã¨ stato possibile abilitare gli assistiti selezionati";
        apiErrorNotify({ error, message });
      }
      this.pagination = {
        page: 1,
        rowsPerPage: LIMIT,
      };
      this.isLoading = false;
      this.isEnabling = false;
    },
  },
  watch: {
    selectedPatients() {
      if (this.selectedPatients.length < 1) {
        this.selectAllCheck = false;
      } else if (this.selectedPatients.length == this.patientList.length) {
        this.selectAllCheck = true;
      } else {
        return;
      }
    },
  },
};
</script>

<style scoped></style>
