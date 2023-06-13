<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="page-patient-list">
    <csi-page-title v-if="doctorInfo" :back="SETTINGS"
      >Assistiti abilitati {{ doctorInfo.n_pazienti_abilitati }}</csi-page-title
    >
    <template v-if="!isLoading">
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
                    <q-input
                      v-model="patientName"
                      label="Nome"
                      dense
                      clearable
                    />
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
                      <csi-button outline>Filtra</csi-button>
                    </csi-buttons>
                  </div>
                </div>
              </div>
            </q-slide-transition>
          </template>
        </q-card-section>
      </q-card>

      <q-table
        :columns="TABLE_PATIENT_COLUMNS"
        :data="patientList"
        :pagination.sync="pagination"
        :rows-per-page-options="[]"
        :selected-rows-label="
          () => {
            return '';
          }
        "
        :selected.sync="selectedPatients"
        @request="loadPatientsList"
        row-key="codice_fiscale"
        selection="multiple"
      >
        <template v-slot:header-selection="scope">
          <q-checkbox color="primary" v-model="scope.selected" />
        </template>
        <template v-slot:body-selection="scope">
          <q-checkbox color="primary" v-model="scope.selected" />
        </template>

        <template v-slot:top>
          <div class="row items-center justify-between full-width  q-mt-md">
            <div
              class="text-caption text-italic text-accent text-bold col-12 col-md-auto"
            >
              {{ selectionMsg }}
            </div>

            <div class="col-12 col-md">
              <csi-buttons>
                <q-btn-dropdown color="primary" label="Aggiungi assistiti">
                  <q-list>
                    <q-item clickable @click="showPatientsSearchDialog = true">
                      <q-item-section>
                        <q-item-label>Cerca assistiti</q-item-label>
                      </q-item-section>
                    </q-item>
                    <q-item clickable>
                      <q-item-section>
                        <q-item-label
                          >Aggiungi tutti i tuoi assistiti</q-item-label
                        >
                      </q-item-section>
                    </q-item>
                  </q-list>
                </q-btn-dropdown>
                <csi-button
                  color="negative"
                  no-min-width
                  outline
                  label="Disabilita assistiti"
                  @click="blockPatientList()"
                />
              </csi-buttons>
            </div>
          </div>
          <!--            <div class="text-caption text-italic text-accent q-py-md full-width q-pl-sm"  style="height: 20px">-->
          <!--              {{selectionMsg}}-->
          <!--            </div>-->
        </template>
      </q-table>
    </template>

    <cod-patients-search-dialog v-model="showPatientsSearchDialog" v-if="showPatientsSearchDialog" />
  </div>
</template>

<script>
import { SETTINGS } from "src/router/routes";
import CodPatientsSearchDialog from "components/CodPatientsSearchDialog";
import { TAX_CODE_MASK } from "src/services/tax-code";
import {
  CONVERSATION_STATUS_MAP,
  GENDER_OPTIONS,
  TABLE_PATIENT_COLUMNS
} from "src/services/config";
import { getPatientSelectionMessage } from "src/services/business-logic";

const LIMIT = 5;
export default {
  name: "PagePatientEnabledList",
  components: { CodPatientsSearchDialog },
  data() {
    return {
      TABLE_PATIENT_COLUMNS,
      GENDER_OPTIONS,
      TAX_CODE_MASK,
      SETTINGS,
      patientList: [],
      isLoading: false,
      patientName: "",
      patientSurname: "",
      patientTaxCode: "",
      minAge: null,
      maxAge: null,
      gender: null,
      isOpenFilters: true,
      hasMorePatients: false,
      patientStatus: null,
      selectedPatients: [],
      showPatientsSearchDialog: false,
      pagination: null,
      showPatientsBlockDialog: false
    };
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
    nSelectedPatients() {
      return this.selectedPatients?.length;
    },
    role() {
      return this.$store.getters["getRoleCode"];
    },
    placement() {
      return this.$store.getters["getPlacementCode"];
    },
    selectionMsg() {
      return getPatientSelectionMessage(this.nSelectedPatients, LIMIT);
    },
    roleParams() {
      return this.$store.getters["getRoleParams"];
    }
  },
  created() {
    this.pagination = {
      page: 1,
      rowsPerPage: LIMIT,
      rowsNumber: LIMIT
    };
    this.loadPatientsList({ pagination: this.pagination });
  },
  methods: {
    openFilters() {
      this.isOpenFilters = !this.isOpenFilters;
    },
    loadPatientsList(props) {
      const { page, rowsPerPage, sortBy, descending } = props.pagination;

      let offset = (page - 1) * rowsPerPage;
      let payload = {
        ruolo: this.role,
        collocazione: this.placement,
        nome: this.patientName,
        cognome: this.patientSurname,
        codice_fiscale: this.patientTaxCode,
        eta_min: this.minAge,
        eta_max: this.maxAge,
        stato: CONVERSATION_STATUS_MAP.ACTIVE,
        limit: rowsPerPage,
        offset: offset
      };

      // this.patientList = PATIENTS_LIST;
      // let xTotalPages = parseInt(headers['x-total-pages']);
      // if(this.nPage < xTotalPages){
      //   this.hasMorePatients =  true
      //   this.nPage++
      // }

      // let xTotalElements = parseInt(headers['x-total-elements']);
      // if(this.PatientsList?.length < xTotalElements){
      //   this.hasMoreConversations =  true
      //
      //
      // }
      let xTotalElements = this.patientList?.length;
      this.pagination.rowsNumber = xTotalElements;
    },
    blockPatientList() {
      if (this.nSelectedPatients > 0) this.showPatientsBlockDialog = true;
    },
    enablePatients() {
      let patientsTaxCode = this.selectedPatients?.map(p => p.codice_fiscale);
      let params = this.roleParams;
      let payload = {
        abilitazione: true,
        assistiti: patientsTaxCode,
        motivazione_medico: null
      };
    }
  }
};
</script>

<style scoped></style>
