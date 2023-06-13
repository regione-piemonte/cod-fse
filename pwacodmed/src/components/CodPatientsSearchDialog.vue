<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-dialog v-bind="attrs" v-on="listeners" maximized>
    <q-card class="csi-bg-default" :class="{ 'q-px-lg': $q.screen.gt.sm }">
      <q-toolbar>
        <q-toolbar-title>Cerca assistiti</q-toolbar-title>
        <q-btn
          v-close-popup
          dense
          flat
          icon="close"
          round
          @click="resetFilters"
        />
      </q-toolbar>
      <q-card-section>
        <q-tabs
          v-model="tab"
          active-color="primary"
          align="left"
          indicator-color="primary"
          @input="onInputTab"
        >
          <q-tab :name="TABS.SPECIFIC" label="Ricerca puntuale" />
          <q-tab :name="TABS.GROUP" label="Cerca gruppo" />
        </q-tabs>

        <q-tab-panels v-model="tab" animated class="bg-transparent">
          <!-- RICERCA SPECIFICA -->
          <!-- ----------------------------------------------------------------------------------------------------------- -->
          <q-tab-panel :name="TABS.SPECIFIC">
            <!-- FILTRI -->
            <!-- ----------------------------------------------------------------------------------------------------------- -->
            <div class="row items-center q-col-gutter-md q-mb-lg">
              <div class="col-12 col-md">
                <q-input v-model="patientName" clearable dense label="Nome" />
              </div>

              <div class="col-12 col-md">
                <q-input
                  v-model="patientSurname"
                  clearable
                  dense
                  label="Cognome"
                />
              </div>
              <div class="col-12 col-md">
                <q-input
                  v-model="patientTaxCode"
                  :mask="TAX_CODE_MASK"
                  clearable
                  dense
                  label="Codice fiscale"
                />
              </div>
              <div class="col-12 col-md">
                <csi-buttons>
                  <csi-button
                    outline
                    no-min-width
                    @click="searchSpecific('specific')"
                    >Cerca</csi-button
                  >
                  <csi-button outline no-min-width @click="resetFilters"
                    >Reset</csi-button
                  >
                </csi-buttons>
              </div>
            </div>
            <div v-if="patientSearchError">
              <csi-banner type="negative">
                Inserire almeno nome e cognome oppure il codice fiscale
                dell'assistito
              </csi-banner>
            </div>
            <!-- TABELLA CERCA SPECIFICO-->
            <!-- ----------------------------------------------------------------------------------------------------------- -->
            <csi-inner-loading
              :showing="isLoading"
              class="bg-transparent"
              block
            />
            <div v-if="totalPatients > 0 && !isLoading" class="">
              <div class="text-center q-py-lg">
                <q-badge
                  class="text-caption text-bold"
                  color="primary-light"
                  text-color="black"
                >
                  <template v-if="totalPatients === 1">
                    {{ totalPatients }} assistito trovato</template
                  >
                  <template v-else>
                    {{ totalPatients }} assistiti trovati
                  </template>
                </q-badge>
              </div>
              <q-table
                :columns="columnsWithAction"
                :data="patientList"
                :rows-per-page-options="[]"
                flat
                :selected.sync="tableSelectPatients"
                :grid="$q.screen.lt.md"
                row-key="nome"
                @request="loadPatientsList"
              >
                <template v-slot:header="props">
                  <q-tr :props="props">
                    <q-th
                      v-for="col in props.cols"
                      :key="col.name"
                      :props="props"
                    >
                      <strong>{{ col.label }}</strong>
                    </q-th>
                  </q-tr>
                </template>
                <!-- PULSANTE ABILITA CERCA SPECIFICA -->
                <template v-slot:body-cell-action="props">
                  <q-td :props="props">
                    <template v-if="isEnabledPatient(props.row)">
                      <div
                        class="text-italic text-bold text-green-6"
                        v-if="statusPatient(props.row)"
                      >
                        {{ statusPatient(props.row) }}
                      </div>
                    </template>
                    <template v-else>
                      <q-btn
                        color="primary"
                        outline
                        @click="enablePatient(props.row)"
                        >Abilita</q-btn
                      >
                    </template>
                  </q-td>
                </template>

                <template v-slot:item="props">
                  <q-card class="q-mb-md full-width">
                    <q-card-section>
                      <q-list>
                        <q-item
                          class="q-px-none"
                          v-for="col in props.cols.filter(
                            (col) => col.name !== 'action'
                          )"
                          :key="col.name"
                        >
                          <q-item-section>
                            <q-item-label caption>{{ col.label }}</q-item-label>
                            <q-item-label>{{ col.value }}</q-item-label>
                          </q-item-section>
                        </q-item>
                      </q-list>
                    </q-card-section>
                  </q-card>
                </template>
                <template v-slot:bottom> </template>
              </q-table>
              <!-- DIFFICILMENTE SERVIRA' SU RICERCA PUNTUALE MA NON SI SA' MAI -->
              <template v-if="totalPage > nPage">
                <div class="q-mt-md text-center">
                  <q-btn
                    :loading="isLoading"
                    color="primary"
                    dense
                    flat
                    @click="searchSpecific()"
                  >
                    Carica altri assistiti
                  </q-btn>
                </div>
              </template>
            </div>
            <div v-if="typeTable === 'specific'">
              <csi-banner type="info">
                La ricerca non ha prodotto risultati
              </csi-banner>
            </div>
          </q-tab-panel>

          <!-- RICERCA PER GRUPPO -->
          <!-- ----------------------------------------------------------------------------------------------------------- -->
          <q-tab-panel :name="TABS.GROUP">
            <!-- FILTRI -->
            <!-- ----------------------------------------------------------------------------------------------------------- -->

            <div class="row items-center q-col-gutter-md q-mb-lg">
              <div class="col-12 col-md-2">
                <q-input
                  mask="###"
                  v-model="minAge"
                  label="EtÃ  minima"
                  dense
                  clearable
                />
              </div>
              <div class="col-12 col-md-2">
                <q-input
                  mask="###"
                  v-model="maxAge"
                  label="EtÃ  massima"
                  dense
                  clearable
                />
              </div>
              <div class="col-12 col-md-2">
                <q-select
                  v-model="gender"
                  label="Sesso"
                  :options="GENDER_OPTIONS"
                  dense
                  clearable
                />
              </div>
              <div class="col-12 col-md-3">
                <q-select
                  v-model="patology"
                  label="Patologia"
                  option-value="cod_esenzione"
                  option-label="desc_esenzione"
                  :options="patologyOptions"
                  dense
                  clearable
                  use-input
                  fill-input
                  hide-selected
                  input-debounce="0"
                  @filter="filterPatologies"
                >
                </q-select>
              </div>
              <div class="col-12 col-md">
                <csi-buttons>
                  <csi-button outline no-min-width @click="searchGroup('group')"
                    >Cerca</csi-button
                  >
                  <csi-button outline no-min-width @click="resetFilters"
                    >Reset</csi-button
                  >
                </csi-buttons>
              </div>
            </div>
            <!-- TABELLA CERCA GRUPPO -->
            <!-- ----------------------------------------------------------------------------------------------------------- -->
            <div v-if="groupSearchError">
              <csi-banner type="negative">
                Inserire almeno etÃ  minima e massima oppure sesso o patologia
                (se si cerca per etÃ  entrambi i campi minima e massima devono
                essere valorizzati)
              </csi-banner>
            </div>
            <div v-if="checkAgeError">
              <csi-banner type="negative">
                L'etÃ  minima deve essere inferiore alla massima
              </csi-banner>
            </div>
            <csi-inner-loading
              :showing="isLoading"
              class="bg-transparent"
              block
            />
            <div v-if="totalPatients > 0 && !isLoading" class="">
              <div class="text-center q-py-lg">
                <q-badge
                  class="text-caption text-bold"
                  color="primary-light"
                  text-color="black"
                >
                  <template v-if="totalPatients === 1">
                    {{ totalPatients }} assistito trovato</template
                  >
                  <template v-else>
                    <template v-if="totalPage > nPage"
                      >Oltre {{ others }} assistiti trovati</template
                    >
                    <template v-else
                      >{{ totalPatients }} assistiti trovati</template
                    >
                  </template>
                </q-badge>
              </div>
              <q-table
                :columns="columnsWithAction"
                :data="patientList"
                row-key="cod_fisc_ass"
                selection="multiple"
                :selected-rows-label="
                  () => {
                    return '';
                  }
                "
                :selected.sync="tableSelectPatients"
                :rows-per-page-options="[0]"
                @request="loadPatientsList"
                responsive
              >
                <!-- SLOT MESSAGGIO SELEZIONATI -->
                <template v-slot:top>
                  <div class="row items-center justify-between full-width">
                    <div
                      class="text-caption text-italic text-accent text-bold col-12 col-md"
                    >
                      {{ selectionMsg }}

                      <p
                        v-if="
                          selectedPatients && selectedPatients.length >= limit
                        "
                      >
                        Numero massimo selezionabile
                        {{ limit }}
                      </p>
                    </div>
                    <q-space />
                    <div class="col-12 col-md-auto text-right">
                      <csi-button @click="enablePatient()" label="Abilita" />
                    </div>
                  </div>
                </template>
                <!-- SLOT HEADER E COLONNE -->
                <template v-slot:header="props">
                  <q-tr :props="props">
                    <q-th>
                      <!-- CHECK SELEZIONA TUTTI -->
                      <q-checkbox
                        v-model="selectAllCheck"
                        v-if="
                          selectedPatients && selectedPatients.length < limit
                        "
                        v-on:click.native="selectAllpatients"
                      ></q-checkbox
                    ></q-th>
                    <q-th
                      v-for="col in props.cols"
                      :key="col.name"
                      :props="props"
                    >
                      {{ col.label }}
                    </q-th>
                  </q-tr>
                </template>
                <!-- SLOT CHECKBOX LATO SINISTRO -->
                <template v-slot:body-selection="props">
                  <q-tr :props="props">
                    <q-th>
                      <template v-if="isEnabledPatient(props.row)">
                        <div class="text-italic text-bold text-green-6"></div>
                      </template>
                      <template v-else>
                        <div class="text-center">
                          <q-checkbox
                            color="primary"
                            v-model="props.selected"
                            v-if="disableCheck(props.row)"
                          />
                        </div>
                      </template>
                    </q-th>
                    <q-th
                      v-for="col in props.row"
                      :key="col.name"
                      :props="props"
                    >
                      {{ col.label }}
                    </q-th>
                  </q-tr>
                </template>
                <!-- SLOT ABILITATO LATO DESTRO -->
                <template v-slot:body-cell-action="props">
                  <q-td :props="props">
                    <template v-if="isEnabledPatient(props.row)">
                      <div
                        class="text-italic text-bold text-green-6"
                        v-if="statusPatient(props.row)"
                      >
                        {{ statusPatient(props.row) }}
                      </div>
                    </template>
                    <template v-else>
                      <div class="text-italic text-bold text-green-6"></div>
                    </template>
                  </q-td>
                </template>
                <!-- SLOT BOTTOM-->
                <template v-slot:bottom> </template>
              </q-table>
              <!-- PULSANTE CERCA ALTRI -->
              <template v-if="totalPage > nPage">
                <div class="q-mt-md text-center">
                  <q-btn
                    :loading="isLoading"
                    color="primary"
                    dense
                    flat
                    @click="loadMore()"
                  >
                    Carica altri assistiti
                  </q-btn>
                </div>
              </template>
            </div>
            <!-- BANNER INFO -->
            <div v-if="typeTable === 'group'">
              <csi-banner type="info"
                >La ricerca non ha prodotto risultati</csi-banner
              >
            </div>
          </q-tab-panel>
        </q-tab-panels>
      </q-card-section>
    </q-card>

    <cod-patient-enablement-dialog
      enablement
      v-model="showEnablementDialog"
      :patients="selectedPatients"
    />
  </q-dialog>
</template>

<script>
import { TAX_CODE_MASK } from "src/services/tax-code";
import {
  GENDER_OPTIONS,
  TABLE_PATIENT_COLUMNS_AURA,
} from "src/services/config";
import { getPatientSelectionMessage } from "src/services/business-logic";
import CodPatientEnablementDialog from "components/CodPatientEnablementDialog";
import {
  getSearchAura,
  getPatologyExceptionsAura,
  managePatients,
} from "src/services/api";
import { apiErrorNotify, isEmpty, orderBy } from "src/services/utils";

const TABS = {
  SPECIFIC: "specific",
  GROUP: "group",
};

const LIMIT = 150;
export default {
  name: "CodPatientsSearchDialog",
  components: { CodPatientEnablementDialog },
  data() {
    return {
      GENDER_OPTIONS,
      TABLE_PATIENT_COLUMNS_AURA,
      TABS,
      TAX_CODE_MASK,
      tab: TABS.SPECIFIC,
      isLoading: false,
      patientName: null,
      pagination: null,
      patientSurname: null,
      patientTaxCode: null,
      patientList: [],
      columnsWithAction: [],
      totalPage: null,
      nPage: 1,
      paginationSpecificSearch: null,
      hasMoreElements: false,
      minAge: null,
      maxAge: null,
      gender: null,
      patology: null,
      patologyList: [],
      filteredPatologyList: [],
      patientSearchError: false,
      groupSearchError: false,
      checkAgeError: false,
      showEnablementDialog: false,
      tableSelectPatients: [],
      typeTable: null,
      selectAllCheck: false,
      enabledPatientsResponse: null,
      limit: LIMIT,
    };
  },
  computed: {
    attrs() {
      const { ...attrs } = this.$attrs;
      return attrs;
    },

    others() {
      return this.limit * (this.totalPage - 1);
    },

    checkAge() {
      if (this.minAge > this.maxAge) {
        return true;
      } else {
        return false;
      }
    },
    listeners() {
      const { ...listeners } = this.$listeners;
      return listeners;
    },
    totalPatients() {
      return this.patientList?.length;
    },
    nSelectedPatients() {
      return this.selectedPatients?.length;
    },
    selectionMsg() {
      return getPatientSelectionMessage(this.selectedPatients?.length, LIMIT);
    },
    role() {
      return this.$store.getters["getRoleCode"];
    },
    placement() {
      return this.$store.getters["getPlacementCode"];
    },
    enabledPatients() {
      return this.$store.getters["getEnabledPatients"];
    },
    selectedPatients() {
      return this.tableSelectPatients.filter(
        (p) => !this.enabledPatients?.includes(p.codice_fiscale)
      );
    },
    roleParams() {
      return this.$store.getters["getRoleParams"];
    },
    patologyOptions() {
      return !isEmpty(this.filteredPatologyList)
        ? orderBy(this.filteredPatologyList, ["desc_esenzione"])
        : [];
    },
  },
  methods: {
    searchSpecific(type) {
      this.tableSelectPatients = [];
      this.enabledPatientsResponse = null;
      this.typeTable = null;
      if (type) {
        this.totalPage = null;
        this.nPage = 1;
      }
      this.patientList = [];
      if ((this.patientName && this.patientSurname) || this.patientTaxCode) {
        this.patientSearchError = false;
        let payload = {
          nome: this.patientName,
          cognome: this.patientSurname,
          cod_fisc_ass: this.patientTaxCode,
          eta_min: null,
          eta_max: null,
          esenzione_patologia: null,
          n_pagina: this.nPage,
        };
        this.loadPatientsList(payload, "specific");
      } else {
        this.patientSearchError = true;
        return;
      }
    },
    async loadMore() {
      let payload = {
        nome: null,
        cognome: null,
        cod_fisc_ass: null,
        eta_min: this.minAge ? this.minAge : null,
        eta_max: this.maxAge ? this.maxAge : null,
        esenzione_patologia: this.patology ? this.patology.cod_esenzione : null,
        sesso: this.gender ? this.gender.value : null,
        n_pagina: this.nPage < this.totalPage ? (this.nPage += 1) : this.nPage,
      };
      await this.loadPatientsList(payload);
    },
    searchGroup(type) {
      this.tableSelectPatients = [];
      this.enabledPatientsResponse = null;
      this.typeTable = null;
      if (type) {
        this.totalPage = null;
        this.nPage = 1;
      }
      this.patientList = [];
      let isOkAge = this.maxAge >= this.minAge ? false : true;
      if (isOkAge) {
        this.checkAgeError = true;
        return;
      }
      if (
        (this.maxAge >= this.minAge &&
          this.maxAge !== null &&
          this.minAge !== null) ||
        this.patology ||
        this.gender
      ) {
        this.groupSearchError = false;
        this.checkAgeError = false;

        let payload = {
          nome: null,
          cognome: null,
          cod_fisc_ass: null,
          eta_min: this.minAge ? this.minAge : null,
          eta_max: this.maxAge ? this.maxAge : null,
          esenzione_patologia: this.patology
            ? this.patology.cod_esenzione
            : null,
          sesso: this.gender ? this.gender.value : null,
          n_pagina: this.nPage,
        };
        this.loadPatientsList(payload, type);
      } else {
        this.checkAgeError = false;
        this.groupSearchError = true;
        return;
      }
    },
    async loadPatientsList(params, type) {
      this.isLoading = true;
      try {
        let response = await getSearchAura({ params });
        this.totalPage = response.data.total_page
          ? response.data.total_page
          : null;
        this.nPage = this.nPage < this.totalPage ? this.nPage++ : this.nPage;
        this.typeTable = response.data.ret_val === 0 && type ? type : null;
        let list = response.data.elenco_assistiti
          ? response.data.elenco_assistiti
          : [];
        this.isLoading = false;
        if (this.patientList) {
          this.patientList = [...this.patientList, ...list];
        } else {
          this.patientList = list;
        }
      } catch (error) {
        console.log("error", JSON.stringify(error));
        this.isLoading = false;
        let errorValue = error?.response?.data?.error_message
          ? error?.response?.data?.error_message
          : null;
        let message = errorValue
          ? errorValue
          : "Non Ã¨ stato possibile recuperare la lista degli assistiti";
        apiErrorNotify({ error, message });
      }
    },
    resetFilters() {
      this.nPage = 1;
      this.patientName = null;
      this.patientSurname = null;
      this.patientTaxCode = null;
      this.minAge = null;
      this.maxAge = null;
      this.gender = null;
      this.patology = null;
      this.patientSearchError = false;
      this.groupSearchError = false;
      this.checkAgeError = false;
      this.showEnablementDialog = false;
      this.tableSelectPatients = [];
      this.typeTable = null;
      this.selectAllCheck = false;
      this.patientList = [];
    },
    selectAllpatients() {
      if (this.selectAllCheck) {
        if (this.patientList.length > LIMIT) {
          this.tableSelectPatients = this.patientList.slice(0, LIMIT);
        } else {
          this.tableSelectPatients = this.patientList;
        }
      } else {
        this.tableSelectPatients = [];
      }
    },
    async enablePatient(patient) {
      if (patient) {
        this.tableSelectPatients = [patient];
      }
      if (this.nSelectedPatients <= 0) return;
      this.isEnabling = true;
      let patientsTaxCode = this.tableSelectPatients?.map(
        (p) => p.cod_fisc_ass
      );
      let payload = {
        abilitazione: true,
        assistiti: patientsTaxCode,
        motivazione_medico: null,
      };

      try {
        let response = await managePatients(payload);
        if (this.enabledPatientsResponse) {
          this.enabledPatientsResponse = [
            ...this.enabledPatientsResponse,
            ...response.data,
          ];
        } else {
          this.enabledPatientsResponse = response.data;
        }

        this.tableSelectPatients = [];
        await this.$emit("refreshList");
        this.$q.notify({
          type: "positive",
          message: "Assistiti abilitati correttamente",
          textColor: "black",
        });
      } catch (error) {
        let message =
          "Non Ã¨ stato possibile abilitare gli assistiti selezionati";
        apiErrorNotify({ error, message });
      }
      this.pagination = {
        page: 1,
        rowsPerPage: LIMIT,
        rowsNumber: LIMIT,
      };
      this.isLoading = false;
      this.isEnabling = false;
    },
    disableCheck(patient) {
      if (this.selectedPatients && this.selectedPatients.length >= LIMIT) {
        let isClickable =
          this.selectedPatients
            .map((e) => e.cod_fisc_ass)
            .indexOf(patient.cod_fisc_ass) !== -1
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
    isEnabledPatient(patient) {
      if (this.enabledPatientsResponse && patient) {
        return this.enabledPatientsResponse
          .map((e) => e.codice_fiscale)
          .indexOf(patient.cod_fisc_ass) !== -1
          ? true
          : false;
      } else {
        return false;
      }
    },
    statusPatient(patient) {
      let status = this.enabledPatientsResponse.find(
        (x) => x.codice_fiscale === patient.cod_fisc_ass
      ).stato_abilitazione;
      if (status === "A" || status === "ABILITATO") {
        return "Abilitato";
      } else if (status === "B" || status === "DISABILITATO") {
        return "Bloccato";
      } else if (status === "GIA_ABILITATO") {
        return "GiÃ  abilitato";
      } else if (status === "NON_ABILITABILE") {
        return "Non abilitabile";
      } else if (status === "GIA_DISABILITATO") {
        return "GiÃ  disabilitato";
      } else if (status === "NON_DISABILITABILE_SOGGETTO_MANCANTE") {
        return "Soggetto mancante";
      } else {
        return "Sconosciuto";
      }
    },
    onInputTab() {
      this.tableSelectPatients = [];
      this.patientList = [];
    },
    filterPatologies(val, update) {
      update(() => {
        if (val === "") {
          this.filteredPatologyList = this.patologyList;
        } else {
          const needle = val.toUpperCase();
          this.filteredPatologyList = this.patologyList.filter(
            (v) => v?.desc_esenzione?.toUpperCase().indexOf(needle) > -1
          );
        }
      });
    },
  },
  watch: {
    tableSelectPatients() {
      if (this.tableSelectPatients.length < 1) {
        this.selectAllCheck = false;
      } else if (this.tableSelectPatients.length == this.patientList.length) {
        this.selectAllCheck = true;
      } else {
        return;
      }
    },
  },
  async created() {
    this.pagination = {
      page: 1,
      rowsPerPage: LIMIT,
      rowsNumber: LIMIT,
    };

    this.columnsWithAction = [...TABLE_PATIENT_COLUMNS_AURA];
    this.columnsWithAction.push({
      name: "action",
      label: "",
      align: "center",
    });
    this.paginationSpecificSearch = {
      page: this.nPage,
      rowsPerPage: LIMIT,
      rowsNumber: LIMIT,
    };
    let params = {
      tipo_esenzione: "P",
      lista_diagnosi: "N",
    };
    try {
      let response = await getPatologyExceptionsAura({ params });
      this.patologyList = response.data?.elenco_esenzioni;
      this.filteredPatologyList = this.patologyList;
    } catch (error) {
      let message = "Non Ã¨ stato possibile recuperare la lista delle patologie";
      apiErrorNotify({ error, message });
    }
  },
};
</script>

<style scoped></style>
