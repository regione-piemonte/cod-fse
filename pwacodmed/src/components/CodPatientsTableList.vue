<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
<div>
  <q-table
    :data="patientList"
    :columns="TABLE_PATIENT_COLUMNS"
    row-key="codice_fiscale"
    selection="multiple"
    :selected-rows-label="()=>{return ''}"
    :selected.sync="selectedPatients"
    :pagination.sync="pagination"
    :rows-per-page-options="[]"
    @request="$emit('load-patients', pagination)"
  >

    <template v-slot:header-selection="scope">
      <q-checkbox color="primary" v-model="scope.selected" />
    </template>
    <template v-slot:body-selection="scope">
      <q-checkbox color="primary" v-model="scope.selected" />
    </template>

    <template v-slot:top>
      <div class="row items-center justify-between full-width  q-mt-md">

        <div class="text-caption text-italic text-accent text-bold col-12 col-md-auto">
          {{selectionMsg}}
        </div>

        <div class="col-12 col-md">
          <csi-buttons>

            <q-btn-dropdown v-if="enablement" color="primary" label="Abilita assistiti">
              <q-list>
                <q-item clickable @click="$emit('search-patients')">
                  <q-item-section>
                    <q-item-label>Cerca assistiti</q-item-label>
                  </q-item-section>

                </q-item>
                <q-item clickable @click="$emit('enable-all')">
                  <q-item-section>
                    <q-item-label>Aggiungi tutti i tuoi assistiti</q-item-label>
                  </q-item-section>

                </q-item>
              </q-list>
            </q-btn-dropdown>

            <template v-if="enablement">
              <csi-button  color="negative" :disable="nSelectedPatients===0" no-min-width outline label="Disabilita assistiti" @click="manageEnablement"   />
            </template>
            <template v-else>
              <csi-button outline no-min-width :disable="nSelectedPatients===0" label="Abilita assistiti" @click="enablePatients" :loading="isEnabling" />
            </template>

          </csi-buttons>
        </div>

      </div>

    </template>
  </q-table>
</div>
</template>

<script>
import { TABLE_PATIENT_COLUMNS} from "src/services/config";
import {getPatientSelectionMessage} from "src/services/business-logic";

const LIMIT = 5

export default {
name: "CodPatientsTableList",
  props:{
    patientList:{type:Array, default: ()=>[]},
    pagination: {type:Object, default:null},
    enablement: {type:Boolean, default:false}
  },
  data(){
    return{
      LIMIT,
      TABLE_PATIENT_COLUMNS,
      blocked:null,
      isLoading:false,
      hasMorePatients:false,
      nElements:1,
      nTotalElements:6,
      offset:0,
      selectedPatients:[],
    }
  },
  computed:{

    nSelectedPatients() {
      return this.selectedPatients?.length
    },

    selectionMsg(){
      return getPatientSelectionMessage(this.nSelectedPatients, LIMIT)
    },
    selectAll:{
      get() {
        let patientsListLength = this.patientList?.length
        if (this.nSelectedPatients <= 0) return false;
        if (this.nSelectedPatients >= patientsListLength) return true;
        return null;
      },
      set() {
        let patientsListLength = this.patientList?.length
        if (this.nSelectedPatients <= 0 || this.nSelectedPatients < patientsListLength) {
          this.selectPatientsAll()
        } else {
          this.disablePatientsAll()
        }
      }
    }
  },
}
</script>

<style scoped>

</style>
