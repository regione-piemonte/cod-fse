<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-item clickable class="cod-message-filtered-item full-width" :class="classes" @click="$emit('click', message)">
    <q-item-section side top>
      <q-icon :name="avatar"></q-icon>
    </q-item-section>
    <q-item-section>
      <div class="row items-center q-col-gutter-x-md justify-between q-mb-xs">
        <q-item-label class="text-bold text-primary">{{authorName}}</q-item-label>
        <q-item-label caption>{{messageDate | datetime}}</q-item-label>
      </div>

      <q-item-label class="text-body1 " >{{messageText}}</q-item-label>
    </q-item-section>

  </q-item>
</template>

<script>
import {MSG_AUTHOR_MAP} from "src/services/config";
import {getGender} from "src/services/tax-code";
import {date} from "quasar";
import {DEFAULT_FORMAT_DATETIME} from "src/services/utils";

export default {
name: "CodMessageFilteredItem",
  props: {
    conversation: {type: Object, default: null},
    message: {type: Object, default: null},
    focused: {type: Boolean, default: false},
  },
  computed:{
    author(){
      return  this.message?.autore
    },
    patientTaxCode(){
      return this.conversation?.assistito?.codice_fiscale
    },
    isDoctorMsg(){
      return  this.author?.tipo === MSG_AUTHOR_MAP?.DOCTOR
    },

    avatar() {
      let patientGender = getGender(this.patientTaxCode)
      let userAvatar = ''

      if(this.isDelegator){
        let delegatorGender = getGender(this.author?.codice_fiscale)
        userAvatar = `avatar-delegato-${patientGender.toLowerCase()}-${delegatorGender.toLowerCase()}`
      }else{
        userAvatar = patientGender === 'F' ? 'avatar-donna' : 'avatar-uomo'
      }

      let img = this.isDoctorMsg ? 'medico-uomo' : userAvatar

      return `img:icone/${img}.svg`

    },
    authorName() {
      let authorName = "Tu"

      if(!this.isDoctorMsg){
        if(this.isDelegator)
          authorName = `Delegato ${this.author?.codice_fiscale}`
        else {
          let patient = this.conversation?.assistito
          authorName =  patient?.nome + ' ' + patient.cognome
        }
      }


      return authorName
    },
    messageDate() {
      let date =   this.message?.data_creazione
      if(this.isEdited && this.message?.data_modifica) {
        date = this.message?.data_modifica
      }
      return date
    },
    isEdited() {
      return this.message?.modificato
    },
    isDelegator(){
      return this.author?.tipo === MSG_AUTHOR_MAP.DELEGATOR
    },
    messageText(){
      let text = this.message?.testo
      let shortText = text.substring(0, 35)

      if(shortText.length< text.length)
        shortText = shortText + '...'

      return shortText;
    },
    classes(){
      let result = [];
      if(this.focused){
        result.push(`cod-message-filtered-item__focused`);
      }
      return result
    }
  },
}
</script>

<style lang="sass">
.cod-message-filtered-item
  .message-text
    text-overflow: ellipsis
    overflow: hidden
    width: 100%
    height: 1.2em
    white-space: nowrap
  &__focused
    background-color: rgba($csi-secondary,0.1)

</style>
