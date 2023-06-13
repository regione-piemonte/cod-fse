<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-icon v-bind="attrs" />
</template>

<script>
import {DOCTOR_TYPES_MAP} from "src/services/config";

const GENDER_MAP = {
  MALE: 'M',
  FEMALE: 'F'
}
const ICON_PEDIATRICIAN={
  [GENDER_MAP.MALE] : "pediatra-uomo.svg",
  [GENDER_MAP.FEMALE] : "pediatra-donna.svg"
}


const ICON_MMG={
  [GENDER_MAP.MALE] : "medico-uomo.svg",
  [GENDER_MAP.FEMALE] : "medico-donna.svg"
}



export default {
name: "CodDoctorTypeIcon",
  props: {
    doctor:{type:Object, default: null}
  },
  computed:{
    attrs() {
      const { ...attrs } = this.$attrs;
      if (!("name" in attrs)) attrs.name = this.icon;
      return attrs;
    },
    isPediatrician() {
      let type = this.doctor?.tipologia?.id;
      return type === DOCTOR_TYPES_MAP.PLS
    },
    icon() {
      let icon = 'img:icone/'
      let doctorGender = this.doctor?.sesso ?? 'M'
      if(this.isPediatrician){
        icon += ICON_PEDIATRICIAN[doctorGender]
      }else{
        icon += ICON_MMG[doctorGender]
      }
      return icon
    }
  }


}
</script>

<style scoped>

</style>
