<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <q-chat-message
    :bg-color="chatColor"
    :class="classes"
    :sent="isDoctorMsg"
    :size="$q.screen.gt.md ? '6' : '11'"
    class="q-px-md"
  >
    <template v-slot:avatar>
      <q-icon
        :name="chatAvatar"
        :style="!isDoctorMsg ? 'left:-16px' : 'right:-16px'" size="sm"
      />

    </template>
    <template v-slot:stamp>

      <div class="row items-end  q-mt-md q-col-gutter-y-xs">
        <template v-if="isDoctorMsg">
          <div class="col col-sm text-caption">
            <div>
              <span v-if="isEdited">Modificato il</span> {{ messageDate | datetime }}
            </div>
            <div v-if="isEdited" class="text-caption text-italic">Modificato</div>
          </div>
        </template>
        <template v-else>
          <div class="row col items-end q-mt-md q-col-gutter-y-xs">
            <div class="col-12 col-sm text-caption">
              <div>  <span v-if="isEdited">Modificato il</span> {{ messageDate | datetime }}</div>
              <div v-if="isDelegator">Inviato da delegato {{ author.codice_fiscale }}</div>
            </div>
            <div v-if="isEdited" :class="{'text-right': $q.screen.gt.xs}"
                 class="col-12 col-sm text-caption text-italic ">
              {{ changeText }}
            </div>
          </div>

          <div v-if="readNotification" class="col-auto q-pl-sm">
            <q-icon v-if="message.letto && isDoctorMsg" color="secondary" name="done_all"
                    size="sm"/>
          </div>

        </template>


      </div>

    </template>

    <template v-slot:default>

      <div class="row items-start">
        <div class="col">
          <p ref="codMessageText" class="q-mb-xs pre-class"> {{ messageText }}</p>
          <div v-if="attachments.length>0" class="q-mt-md">
            <cod-message-attachment-item
              v-for="attachment in attachments"
              :key="attachment.id"
              :attachment="attachment"
              :patient-tax-code="patientTaxCode"

            />
          </div>
        </div>

        <template v-if="isMessageEditable && doctorInfo && !doctorInfo?.data_fine_adesione">
          <q-btn
            aria-label="Menu opzioni"
            class="col-auto"
            dense
            flat
            icon="more_vert"
            round
          >
            <q-menu anchor="bottom right" self="top right">
              <q-list style="min-width: 180px">
                <q-item v-close-popup clickable @click="$emit('edit-message', message)">
                  <q-item-section>Modifica</q-item-section>
                </q-item>
              </q-list>
            </q-menu>
          </q-btn>
        </template>
        <div v-else class="q-px-md"></div>

      </div>
    </template>
  </q-chat-message>
</template>

<script>
import {date} from "quasar";
import {DEFAULT_FORMAT_DATETIME, isEmpty} from "src/services/utils";
import {
  DOCUMENT_CATEGORY_MAP,
  DOCUMENT_TYPE_CODE_LIST_IMAGE_DOWNLOADABLE,
  IMAGE_STATUS_CODE_LIST_BOOKABLE, IMAGE_STATUS_CODE_LIST_DOWNLOADABLE, IMAGE_STATUS_CODE_LIST_IN_ELABORATION,
  MSG_AUTHOR_MAP
} from "src/services/config";
import {getGender} from "src/services/tax-code";
import {searchMessageRegex} from "src/services/business-logic";
import {getImageStatus} from "src/services/api";
import CodMessageAttachmentItem from "components/CodMessageAttachmentItem";

export default {
  name: "CodMessageItem",
  components: {CodMessageAttachmentItem},
  props: {
    message: {type: Object, default: null},
    patient: {type: Object, default: null},
    focused: {type: Boolean, default: false},
    searchedText: {type: String, default: ''},
    readNotification: {type: Boolean, default: false},
  },
  data() {
    return {
      isLoading: false
    }
  },
  computed: {
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
    messageText() {
      return this.message?.testo
    },
    author() {
      return this.message?.autore
    },
    patientTaxCode() {
      return this.patient?.codice_fiscale
    },
    isDoctorMsg() {
      return this.author?.tipo === MSG_AUTHOR_MAP?.DOCTOR
    },
    chatColor() {
      return this.isDoctorMsg ? 'chat-blue' : 'chat-pink'
    },
    chatAvatar() {
      let patientGender = getGender(this.patientTaxCode)
      let userAvatar = ''

      if (this.isDelegator) {
        let delegatorGender = getGender(this.author?.codice_fiscale)
        userAvatar = `avatar-delegato-${patientGender.toLowerCase()}-${delegatorGender.toLowerCase()}`
      } else {
        userAvatar = patientGender === 'F' ? 'avatar-donna' : 'avatar-uomo'
      }

      let img = this.isDoctorMsg ? 'medico-uomo' : userAvatar
      return `img:icone/${img}.svg`
    },
    authorName() {
      let authorname = this.author?.nome + ' ' + this.author?.cognome
      return this.isDoctorMsg ? 'dott. ' + authorname : authorname
    },
    messageDate() {
      let msgDate = this.message?.data_creazione
      if(this.isEdited && this.message?.data_modifica) {
        msgDate = this.message?.data_modifica
      }

      return msgDate ? date.formatDate(msgDate, DEFAULT_FORMAT_DATETIME) : ''
    },
    isDelegator() {
      return this.author?.tipo === MSG_AUTHOR_MAP.DELEGATOR
    },
    attachments() {
      return this.message?.allegati ?? []
    },
    isMessageEditable() {
      return this.message?.modificabile && this.isDoctorMsg
    },
    isEdited() {
      return this.message?.modificato
    },
    changeText() {
      let author = this.message?.autore_modifica

      if (author === this.patientTaxCode)
        return `Modificato dall'assistito`
      else
        return `Modificato dal delegato ${author}`
    },
    classes() {
      let result = [];
      if (this.focused) {
        result.push(`cod-message-item__focused`);
      }
      return result
    },

  },
  async created() {
    // this.isLoading= true
    //   for(let i = 0; i<this.attachments?.length; i++){
    //     let attachment = this.attachments[i]
    //     if(this.hasImage(attachment)){
    //       let params={
    //         codice_fiscale: this.patientTaxCode,
    //         id_documento_ilec: attachment.id_documento_ilec,
    //         cod_cl: attachment.codice_cl,
    //         cod_documento_dipartimentale: attachment.codice_documento_dipartimentale,
    //         archivio_documento_ilec: 'FSE',
    //
    //       }
    //       try{
    //           let {data} = await getImageStatus({params})
    //           this.attachments[i].stato_richiesta = data?.stato_richiesta
    //       }catch (e) {
    //
    //       }
    //     }
    //   }
    //
    // this.isLoading= false


  },
  watch: {
    searchedText: {
      immediate: true,
      handler(val, oldVal) {
        let selectedText = this.$refs.codMessageText
        let innerHTML = selectedText?.innerHTML
        if (!innerHTML) return

        let newText = innerHTML.replaceAll('<span class="highlight">', '');
        newText = newText.replaceAll('</span>', '');
        selectedText.innerHTML = newText;

        if (!isEmpty(val)) {
          const regex = searchMessageRegex(val)
          newText = newText.replaceAll(regex, '<span class="highlight">$&</span>');
          selectedText.innerHTML = newText;

        }


      }
    }
  },
  methods: {
    hasImage(attachment) {
      return attachment.accession_numbers?.length > 0 &&
        // attachment.categoria_tipologia ===DOCUMENT_CATEGORY_MAP.FSE &&
        DOCUMENT_TYPE_CODE_LIST_IMAGE_DOWNLOADABLE.includes(attachment.tipo_documento?.codice)

    },
    canBookImage(attachment){
      return IMAGE_STATUS_CODE_LIST_BOOKABLE.includes(attachment.stato_richiesta)
    },
    isImageInElaboration(attachment){
      return IMAGE_STATUS_CODE_LIST_IN_ELABORATION.includes(attachment.stato_richiesta)
    },
    canDownloadImage(attachment){
      return IMAGE_STATUS_CODE_LIST_DOWNLOADABLE.includes(attachment.stato_richiesta)
    },
  }
}
</script>

<style lang="sass">
.pre-class
  white-space: pre-wrap
  word-wrap: break-word
  font-family: inherit

.cod-message-item__focused
  background-color: transparent
  animation-name: change-bg
  animation-duration: 4s

.highlight
  background-color: rgba($csi-secondary, 0.5)

@keyframes change-bg
  0%
    background-color: rgba($csi-secondary, 0.1)

</style>
