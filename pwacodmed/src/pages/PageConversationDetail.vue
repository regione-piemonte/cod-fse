<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="q-px-md page-conversation-detail">

    <div :style="pageHeightStyle" class="column ">
      <div class="col-auto q-pb-lg q-pt-md row items-center justify-between">
        <div class="col " >
          <csi-page-title :back="CONVERSATION_LIST" >
            Conversazione <span v-if="patientName">con {{ patientName | empty }}</span>
          </csi-page-title>
        </div>

        <div class="col-md-auto">
          <div class="row items-center q-col-gutter-x-md">
            <div v-if="blocked" class="col-md-auto">
              <div v-if="$q.screen.gt.md" class="">
                <q-btn :class="{'readonly': !blockDoctorMotivation && !hasNotAgreed}" class="q-px-xs block-btn" dense no-caps outline
                       rounded
                       @click="onClickBlockBtn">
                  <q-icon class="q-mr-xs" name="lock" size="xs"/>
                  <div class="text-caption text-bold">
                    Conversazione in sola lettura
                  </div>
                </q-btn>
              </div>
              <template v-else>
                <q-btn :class="{'readonly': !blockDoctorMotivation && !hasNotAgreed}" class="block-btn" flat icon="lock" round
                       @click="onClickBlockBtn">
                  <q-tooltip>
                    Conversazione in sola lettura
                  </q-tooltip>
                </q-btn>
              </template>

            </div>

            <div v-if="conversationEndDate" class="col-md-auto ">

              <div class="text-caption ">
                <span class="gt-md"> Data eliminazione:  <strong>{{ conversationEndDate | date }}</strong></span>
                <span class="q-pl-xs"><q-btn
                  :size="$q.screen.gt.md ? 'sm' : 'md'"
                  aria-label="Maggiori informazioni"
                  class=""
                  color="secondary"
                  dense
                  flat
                  icon="o_info"
                  round
                  @click="showEndDateInfo =true"
                /></span>
              </div>

            </div>
          </div>

        </div>

      </div>


        <div class="col">

          <template v-if="isLoading">
            <cod-conversation-detail-skeleton/>
          </template>
          <template v-else-if="conversation">

            <div :class="{'q-pb-lg': blocked}" class="full-height">
              <q-card class="fit column" square>
                <div class="col-auto">
                  <csi-card-item-bar type="info">
                    <span>{{ infoConversationMsg }}</span>
                    <span class="q-pl-xs">
                      <a class="csi-link--black" href="#" @click.prevent="toggleMoreInfo">
                        {{isShowingMoreInfo ? 'Nascondi': 'Leggi altro'  }}
                      </a>
                    </span>

                  </csi-card-item-bar>
                  <q-card-section class="bg-grey-3 q-py-sm">
                    <div class=" row items-center justify-between">

                      <q-item  class="col conversation-argument" :class="{'argument-hidden' :  showSearchInput && $q.screen.lt.md}" >
                        <q-item-section avatar>
                          <q-icon name="img:icone/img_notifiche.svg" size="lg"/>
                        </q-item-section>
                        <q-item-section>
                          <q-item-label class="text-caption">Argomento</q-item-label>
                          <q-item-label><strong>{{ argument | empty }}</strong></q-item-label>
                        </q-item-section>

                      </q-item>

                      <div v-if="messagesListLength>0" class="col-auto text-right">

                        <div class="row items-center reverse">
                          <div class="col-auto q-pl-xs">
                            <q-fab
                              active-icon="close"
                              color="primary"
                              icon="search"
                              padding="sm"
                              unelevated
                              @input="onInputSearchFab"
                            >
                            </q-fab>
                          </div>

                          <div :class="{'active': showSearchInput}" class="col search-input">
                            <div class="full-width">
                              <q-input
                                key="input"
                                v-model="searchInput"
                                bg-color="white"
                                class="search-input__field"
                                dense
                                filled
                                label="Cerca nei messaggi"
                                @input="filterMessages"
                                @keyup.enter="showFilteredMessages"
                              />
                            </div>

                          </div>

                        </div>

                      </div>

                      <div
                        v-if="doctorInfo && !doctorInfo?.data_fine_adesione"
                        class="col-auto q-pl-md"
                      >
                        <q-btn
                          aria-label="Menu altre azioni"
                          color="accent"
                          dense
                          flat
                          icon="more_vert"
                          round
                          @click.stop=""
                        >
                          <q-menu anchor="bottom right" self="top right">
                            <q-list style="min-width: 180px">
                              <q-item
                                v-if="isPatientBlocked"
                                :disabled="isEnabling"
                                clickable
                                @click="enablePatient"
                              >
                                <q-item-section> Abilita assistito</q-item-section>
                                <q-item-section v-if="isEnabling" side>
                                  <q-spinner :thickness="2" color="primary"/>
                                </q-item-section>
                              </q-item>
                              <q-item
                                v-else
                                v-close-popup
                                clickable
                                @click="showPatientsBlockDialog = true"
                              >
                                <q-item-section>Disabilita assistito</q-item-section>
                              </q-item>
                            </q-list>
                          </q-menu>
                        </q-btn>
                      </div>
                    </div>

                  </q-card-section>
                  <q-separator/>
                </div>

                <!-- ---------   LISTA MESSAGGI ------------------------>
                <!------------------------------------------------------------------------------------------------------------>
                <q-slide-transition>
                  <q-splitter
                    id="message-splitter"
                    v-model="splitterModel"
                    :limits="[0, 100]"
                    class="col"
                    disable
                  >
                    <template v-slot:before>
                      <!-- ---------   LISTA MESSAGGI ------------------------>
                      <!------------------------------------------------------------------------------------------------------------>

                      <q-scroll-area
                        v-show="messagesListLength>0"
                        id="message-scroll-area"
                        ref="messageScrollArea"
                        style="height: 100%"
                        visible
                      >

                        <q-infinite-scroll ref="messagesInfiniteScroll" :debounce="100"
                                           :offset="10"
                                           reverse scroll-target="#message-scroll-area .q-scrollarea__container"
                                           style="height: 100%" @load="onLoadMessages">
                          <template v-slot:loading>
                            <div class="row justify-center q-my-md">
                              <q-spinner-dots :size="isSearchingMessages ? '40px': '0px'" color="primary"/>
                            </div>
                          </template>

                          <div v-for="(message) in orderedMessageList" :id="'messageItem'+ message.id"
                               :key="`${message.id}-${reloadCount}`"
                               :ref="'messageItem'+ message.id" class="q-pa-md">
                            <div ref="messageItemScroll"
                                 v-scroll="(el) => scrollFire(el, message)" class="messageItemScroll"></div>


                            <cod-message-item
                              :focused="messageFocused && messageFocused.id === message.id"
                              :message="message"
                              :patient="patient"
                              read-notification
                              :searched-text="searchedString"
                              @edit-message="onEditMessage"
                              @show-document="showDocument"
                            />
                          </div>

                        </q-infinite-scroll>


                      </q-scroll-area>
                      <div v-if="!isSearchingMessages && messagesListLength<=0" class="q-py-md">
                        <csi-banner class="q-mx-md" type="info">
                          Nessun messaggio
                        </csi-banner>
                      </div>
                    </template>
                    <template v-slot:after>
                      <!-- ---------   RICERCA MESSAGGI (DESKTOP) ------------------------>
                      <!------------------------------------------------------------------------------------------------------------>
                      <template v-if="showFilteredMsg">
                        <div v-if="$q.screen.gt.sm" ref="searchContainer"
                             class="relative-position filtered-messages-container full-height">

                          <template v-if="filteredMessageList.length<=0">
                            <q-bar class="text-body1 bg-orange-2">
                              Nessun risultato
                            </q-bar>

                            <div v-if="!areAllMessagesLoaded" class=" column items-center  q-pt-md" style="">
                              <q-btn
                                :loading="isSearchingMessages"
                                class="col q-px-sm"
                                dense
                                outline

                                @click="loadMessageList"
                              >
                                Espandi ricerca
                              </q-btn>
                            </div>

                          </template>
                          <template v-else>
                            <template v-if="filteredMessageList.length===1">
                              <q-bar class="text-body1 bg-grey-3">{{ filteredMessageList.length }} messaggio trovato
                              </q-bar>
                            </template>
                            <template v-else>
                              <q-bar class="text-body1 bg-grey-3">{{ filteredMessageList.length }} messaggi trovati
                              </q-bar>
                            </template>

                            <q-scroll-area
                              class="position-relative"
                              style="height: 300px; "
                            >
                              <div>
                                <q-list separator>
                                  <cod-message-filtered-item
                                    v-for="(message) in filteredMessageList"
                                    :key="message.id"
                                    ref="filteredMessage"
                                    :conversation="conversation"
                                    :focused="messageFocused === message"
                                    :message="message"
                                    @click="onSearchMessage"
                                  />
                                </q-list>
                              </div>
                              <div v-if="!areAllMessagesLoaded" class=" column items-center  q-pt-md" style="">
                                <q-btn
                                  :loading="isSearchingMessages"
                                  class="col q-px-sm"
                                  dense
                                  outline

                                  @click="loadMessageList"
                                >
                                  Espandi ricerca
                                </q-btn>
                              </div>

                            </q-scroll-area>


                          </template>


                        </div>
                      </template>

                    </template>
                  </q-splitter>

                </q-slide-transition>

                <!-- ---------   RICERCA MESSAGGI ------------------------>
                <!------------------------------------------------------------------------------------------------------------>
                <q-slide-transition>
                  <!-- ---------   RICERCA MESSAGGI (MOBILE) ------------------------>
                  <!------------------------------------------------------------------------------------------------------------>
                  <div v-show="$q.screen.lt.md && showFilteredMsg" class="bg-secondary col-auto">
                    <q-item>
                      <q-item-section side>
                        <q-icon color="white" name="pageview"></q-icon>
                      </q-item-section>
                      <q-item-section>
                        <q-item-label class="text-white text-h6">
                          <div v-if="filteredMessageList.length<=0" class="row items-center justify-between">
                            Nessun risultato
                            <div v-if="!areAllMessagesLoaded" class="col-auto q-pl-sm ">
                              <q-btn
                                :loading="isSearchingMessages"
                                class="q-px-xs"
                                dense
                                outline
                                @click="loadMessageList"
                              >
                                Espandi ricerca
                              </q-btn>
                            </div>

                          </div>
                          <div v-else>
                            {{ messageFocusedIndex + 1 }} di {{ filteredMessageList.length }}
                          </div>
                        </q-item-label>
                      </q-item-section>
                      <q-item-section v-if="filteredMessageList.length>0" side>
                        <div class="row items-center">

                          <template
                            v-if="!areAllMessagesLoaded && messageFocusedIndex>=filteredMessageList.length-1">
                            <q-btn
                              :loading="isSearchingMessages"
                              class="q-px-xs"
                              color="white"
                              dense
                              flat
                              @click="loadMessageList"
                            >
                              Espandi ricerca
                            </q-btn>
                          </template>
                          <template v-else>
                            <q-btn
                              :disable="messageFocusedIndex>=filteredMessageList.length-1"
                              color="white"
                              flat
                              icon="keyboard_arrow_up"
                              round
                              @click="onFocusMessage(messageFocusedIndex+1)"/>
                          </template>

                          <q-btn
                            :disable="messageFocusedIndex<=0"

                            color="white"
                            flat
                            icon="keyboard_arrow_down"
                            round
                            @click="onFocusMessage(messageFocusedIndex-1)"/>
                        </div>
                      </q-item-section>
                    </q-item>
                    <!--                  <div  v-if="!areAllMessagesLoaded">-->
                    <!--                    <q-btn-->
                    <!--                      class="q-px-xs"-->
                    <!--                      :loading="isSearchingMessages"-->
                    <!--                      dense-->
                    <!--                      color="white"-->
                    <!--                      outline-->
                    <!--                      @click="loadMessageList"-->
                    <!--                    >-->
                    <!--                      Espandi ricerca-->
                    <!--                    </q-btn>-->
                    <!--                  </div>-->
                  </div>
                </q-slide-transition>

                <!-- ---------   NUOVO MESSAGGIO ------------------------>
                <!------------------------------------------------------------------------------------------------------------>
                <div v-if="!blocked && !doctorInfo?.data_fine_adesione && !hasNotAgreed" class="col-auto">
                  <q-card-section v-if="isEditingMsg" class="q-pb-none">
                    <q-item class="q-px-none " dense>
                      <q-item-section side>
                        <q-icon color="primary" name="edit"></q-icon>
                      </q-item-section>
                      <q-item-section>
                        <q-item-label class="text-bold text-primary">Modifica messaggio</q-item-label>
                        <q-item-label v-if="editableMessage" caption lines="1">{{
                            editableMessage.testo
                          }}
                        </q-item-label>
                      </q-item-section>
                      <q-item-section avatar>
                        <q-btn color="primary" dense flat icon="close" outline rounded @click="closeEditing"></q-btn>
                      </q-item-section>
                    </q-item>
                  </q-card-section>
                  <q-card-section id="new-message-input" :class="{'q-px-sm': $q.screen.lt.md}">
                    <q-input
                      v-model="messageText"
                      autogrow
                      bg-color="white"
                      dense
                      no-error-icon
                      outlined
                      placeholder="Scrivi messaggio"
                    >

                      <template v-slot:after>
                        <div class="lt-md">
                          <q-btn :disabled="isEmptyMessage" :loading="isSendingMessage" color="primary" dense flat
                                 icon="send" @click="sendMessage">
                            <q-tooltip>
                              Invia
                            </q-tooltip>
                          </q-btn>
                        </div>

                      </template>

                    </q-input>

                    <csi-buttons class="q-mt-md gt-sm">
                      <csi-button
                        :disabled="isEmptyMessage"
                        :loading="isSendingMessage"
                        @click="sendMessage"
                      >Invia
                      </csi-button
                      >
                    </csi-buttons>


                  </q-card-section>
                </div>
              </q-card>
            </div>

          </template>
        </div>
        <!--DIALOGS -->
        <template v-if="!isLoading">
          <cod-patients-block-dialog
            v-model="showPatientsBlockDialog"
            :enablement="blocked"
            :patients="[patient]"
            @refreshList="refreshList"
            @on-block="onBlock"
          />

          <q-dialog v-model="showEndDateInfo">
            <q-card>
              <q-toolbar>
                <q-toolbar-title>
                  Data eliminazione conversazione
                </q-toolbar-title>

                <q-btn v-close-popup aria-label="chiudi finestra" flat icon="close" round/>
              </q-toolbar>

              <q-card-section>
                <div class="q-mb-md">
                  Se non vengono inseriti nuovi messaggi questa conversazione sarÃ  eliminata il
                  {{ conversationEndDate | date }}.
                </div>

              </q-card-section>
            </q-card>
          </q-dialog>

          <q-dialog v-model="showBlockMotivationDialog">
            <q-card style="">
              <q-toolbar>
                <q-toolbar-title>
                  Conversazione in sola lettura
                </q-toolbar-title>

                <q-btn v-close-popup aria-label="chiudi finestra" flat icon="close" round/>
              </q-toolbar>

              <q-card-section>
                <csi-banner class="q-mb-lg" type="info">
                  {{blockDoctorMotivationLabel}}
                </csi-banner>
              </q-card-section>

            </q-card>
          </q-dialog>
        </template>


    </div>


  </div>
</template>

<script>

import {BLOCK_REASONS_MAP, CONVERSATION_STATUS_MAP, MSG_AUTHOR_MAP} from "src/services/config";

import {apiErrorNotify,  orderBy} from "src/services/utils";

import {
  scrollToElement,
  searchMessageRegex
} from "src/services/business-logic";
import CodMessageItem from "components/CodMessageItem";
import CodMessageFilteredItem from "components/CodMessageFilteredItem";
import {
  editMessage, getAgreement,
  getConversationList,
  getMessageList, managePatients,
  setMessageAsRead, setMessageNew
} from "src/services/api";
import CodConversationDetailSkeleton from "components/loaders/CodConversationDetailSkeleton";
import {date, Screen} from 'quasar'
import {truncate} from "boot/filters";
import {CONVERSATION_LIST} from "src/router/routes";
import CsiCardItemBar from "components/core/CsiCardItemBar";
import {getGender} from "src/services/tax-code";
import CodPatientsBlockDialog from "components/CodPatientsBlockDialog";


const MAX_SPLITTER = 110
const LIMIT = 20
export default {
  name: "PageConversationDetail",
  components: {
    CodPatientsBlockDialog,
    CsiCardItemBar,
    CodConversationDetailSkeleton, CodMessageFilteredItem, CodMessageItem,
  },
  data() {
    return {
      Screen,
      BLOCK_REASONS_MAP,
      CONVERSATION_LIST,
      id: null,
      conversation: null,
      isLoading: false,
      messagesList: [],
      messageText: '',
      showDocumentListDialog: false,
      showBlockMotivationDialog: false,
      showSearchInput: false,
      searchInput: '',
      splitterModel: 0,
      messageFocused: null,
      messageFocusedIndex: 0,
      isEditingMsg: false,
      editableMessage: null,
      isSearchingMessages: false,
      totalMessagesCount: Number.MAX_SAFE_INTEGER,
      isSendingMessage: false,
      showFilteredMsg: false,
      sentMessage: false,
      showDocumentDetail: false,
      selectedDocument: null,
      isLoadingDocumentDetail: false,
      offset: 0,
      reloadCount: 0,
      showEndDateInfo: false,
      fromHome: false,
      isEnrollmentGuardVisible: true,
      documentList: [],
      isShowingMoreInfo: false,
      isEnabling: false,
      showPatientsBlockDialog:false,
      agreementError: false,
      messageId: null,
    }
  },
  computed: {
    user() {
      return this.$store.getters["getUser"];
    },
    doctorInfo() {
      return this.$store.getters["getDoctorInfo"];
    },
    patient() {
      return this.conversation?.assistito;
    },
    isPatientBlocked() {
      return this.blocked;
    },
    patientGender() {
      return getGender(this.patientTaxCode);
    },
    patientTaxCode() {
      return this.patient?.codice_fiscale;
    },
    hasNotAgreed() {
      return this.agreementError
    },
    patientIcon() {
      let gender = this.patientGender === "F" ? "donna" : "uomo";
      return `img:icone/avatar-${gender}.svg`;
    },
    infoConversationMsg() {
      let maxLetters = 90

      if (this.$q.screen.width > 991)
        maxLetters = 121
      else if (this.$q.screen.lt.md)
        maxLetters = 84
      if (this.$q.screen.width < 400)
        maxLetters = 75
      let msg = "Le conversazioni che avvengono attraverso questo servizio non sostituiscono in alcun modo il colloquio o la visita medica, pertanto non hanno valore nÃ© di prescrizione nÃ© di consulto medico"

      return this.isShowingMoreInfo ? msg : truncate(msg, maxLetters)

    },
    taxCode() {
      return this.$store.getters["getTaxCode"];
    },

    argument() {
      return this.conversation?.argomento ?? ''
    },
    patientName() {
      let lastName = this.patient?.cognome ?? "";
      let firstName = this.patient?.nome ?? "";
      return [firstName, lastName]
        .map(el => el.trim())
        .filter(el => !!el)
        .join(" ");
    },
    canShowReadNotification() {
      return !!this.doctorInfo?.notifica_lettura_messaggi
    },

    blocked() {
      return this.conversation?.sola_lettura || this.hasNotAgreed
    },
    areAllMessagesLoaded() {
      return this.messagesListLength >= this.totalMessagesCount
    },
    messagesListLength() {
      return this.messagesList?.length ?? 0
    },
    noMessages() {
      return this.totalMessagesCount <= 0;
    },
    conversationEndDate() {
      return this.conversation?.data_eliminazione
    },
    blockDoctorMotivation() {
      return this.conversation?.motivo_blocco?.codice
    },
    blockDoctorMotivationLabel() {
      let message = "";
      if (this.conversation?.motivo_blocco?.descrizione) {
        message = `Motivo della disabilitazione: ${this.conversation?.motivo_blocco?.descrizione}`;
        let blockReason = this.conversation?.motivo_blocco?.codice;

        if (
          blockReason === BLOCK_REASONS_MAP.DOCTOR_CHOICE &&
          this.conversation?.motivazione_medico
        ) {
          message += `. ${this.conversation?.motivazione_medico}`;
        }
      }else if(this.hasNotAgreed){
        message = "L'assistito non ha fornito il consenso alla consultazione, perciÃ² non Ã¨ possibile contattarlo"
      }

      return message;
    },
    orderedMessageList() {
      return orderBy(this.messagesList, ['data_creazione'], ['asc']) ?? []
    },
    filteredMessageList() {
      let list = []
      if (this.searchInput.length > 0) {
        let regex = searchMessageRegex(this.searchInput)
        list = this.orderedMessageList?.filter(msg => msg.testo.match(regex)) ?? []
        list = orderBy(list, ['data_creazione'], ['desc'])

      }
      return list
    },
    searchedString() {
      return this.showFilteredMsg ? this.searchInput : ''
    },
    isEmptyMessage() {
      // verifico che il messaggio non sia vuoto
      let emptyMessage = !this.messageText.replace(/\s/g, '').length
      return emptyMessage
    },
    pageHeightStyle() {
      let height = this.block ? '900px' : '100vh'
      return `height: ${height}`
    }
  },
  watch: {
    Screen: {
      immediate: true,
      deep: true,
      handler(val, oldVal) {
        if (this.showFilteredMsg) {
          if (val.gt.sm) {
            this.splitterModel = 60
          } else {
            this.splitterModel = MAX_SPLITTER
          }
        }
      }

    }
  },
  async created() {

    let {id, conversation} = this.$route.params;
    this.id = id;
    this.splitterModel = MAX_SPLITTER;
    this.isLoading = true;
    if (!conversation) {
      let params = {
        limit: 1,
        offset: 0,
        sola_lettura: CONVERSATION_STATUS_MAP.ALL,
        id_conversazione: id,
      };

      try {
        let {data: list} = await getConversationList({params});
        conversation = list[0];
      } catch (error) {
        let message = "Non Ã¨ stato possibile recuperare la conversazione.";
        apiErrorNotify({error, message});
      }
    }

    if (!conversation) {
      this.isLoading = false;
      return;
    }

    this.conversation = conversation;

    if (this.conversation && !this.conversation?.sola_lettura) {
      try{
        let response = await getAgreement(this.patientTaxCode);
        console.log("response", response.data);
        if (!response?.data?.consenso_consultazione) {
          this.agreementError = true;
        } else {
          this.agreementError = false;
        }
      }catch (error) {
        this.agreementError = true;

        let message = "Non Ã¨ possibile contattare lâinfrastruttura nazionale per verificare lo stato dei consensi del cittadino nel Fascicolo Sanitario.";

        apiErrorNotify({error, message});


      }

    }

    let params = {
      limit: LIMIT,
      offset: 0,
    };
    let messageList = [];
    try {
      let response = await getMessageList(id, {params});
      messageList = response.data;
      this.totalMessagesCount =
        parseInt(response.headers["x-total-elements"]) ??
        this.messagesList?.length;
    } catch (error) {
      let message = "Non Ã¨ stato possibile recuperare la lista dei messaggi.";
      apiErrorNotify({error, message});
      return;
    }

    this.messagesList = await this.getAttachmentsInfo(messageList);

    this.isLoading = false;

  },
  methods: {
    async getAttachmentsInfo(messageList) {
      for (let i = 0; i < messageList?.length; i++) {
        let message = messageList[i];
        let attachments = message.allegati ?? [];

        for (let j = 0; j < attachments.length; j++) {
          let attachment = attachments[j];
          let structureName = attachment.descrizione_struttura ?? "";
          let aslName = attachment.azienda?.descrizione ?? "";
          let issueDate = attachment.data_validazione
            ? date.formatDate(attachment.data_validazione, "DD/MM/YYYY")
            : "";

          attachment.nome_allegato = `${structureName} - ${aslName} - ${issueDate}`;

          if (attachment.accession_numbers?.length > 0) {
            //@TODO: si fa la chiamata a  stato generazione pacchetto per capire se deve prima prenotare immagine
            attachment.stato_generazione_pacchetto = "S";
          }
        }
      }

      return messageList;
    },


    async loadMessageList() {
      this.isSearchingMessages = true;

      let params = {
        limit: LIMIT,
        offset: this.messagesList?.length,
      };

      try {
        let response = await getMessageList(this.id, {params});
        this.messagesList = [...this.messagesList, ...response.data];
        this.totalMessagesCount =
          parseInt(response.headers["x-total-elements"]) ??
          this.messagesList?.length;
        if (this.showFilteredMsg) {
          this.onFocusMessage(0);
        }
      } catch (error) {
        let message = "Non Ã¨ stato possibile recuperare la lista dei messaggi.";
        apiErrorNotify({error, message});
      }
      this.isSearchingMessages = false;
    },

    async onLoadMessages(index, done) {
      if (this.areAllMessagesLoaded || this.noMessages) {
        //this.$refs.messagesInfiniteScroll.stop();
        done();
        return;
      }
      await this.loadMessageList();
      done();
      this.isLoading = false;
    },
    async setEditMessage() {
      let payload = {
        testo: this.messageText
      }
      try {
        let {data: message} = await editMessage( this.id,
          this.messageId,
          payload)

        this.sentMessage = true

        message.autore = {
          tipo: MSG_AUTHOR_MAP.DOCTOR,
        };


        let newMessageList = this.messagesList?.map(msg => {
          msg.modificabile = false
          if (msg.id !== message?.id) {
            return msg
          } else {
            return message
          }
        })

        console.log(newMessageList)
        // newMessageList.push(message)
        this.messagesList = newMessageList
        this.messageText = ''
        this.editableMessage = null

        return message
      } catch (error) {
        let message = 'Non Ã¨ stato possibile modificare il messaggio'
        apiErrorNotify({error, message})
      }
    },
    async sendMessage() {
      if (this.isEmptyMessage) return

      this.isSendingMessage = true

      let message = null
      if (this.isEditingMsg) {
        message = await this.setEditMessage()
      } else {
        message = await this.sendNewMessage()
      }
      this.reloadCount++
      this.isEditingMsg = false
      this.isSendingMessage = false
      // this.offset=0
      // await this.loadMessageList()

      let lastMessageItem = this.getMessageRef(message?.id)
      this.$refs.messageScrollArea.setScrollPosition('vertical', lastMessageItem.offsetTop, 300)

    },
    async sendNewMessage() {

      let payload = {
        testo: this.messageText,
        nome_medico: this.user?.nome,
        cognome_medico: this.user?.cognome,
      };
      try {
        let {data: message} = await setMessageNew(this.id, payload);
        message.autore = {
          tipo: MSG_AUTHOR_MAP.DOCTOR,
        };

        let newMessageList = this.messagesList?.map(msg => {
          msg.modificabile = false
          return msg
        })
        newMessageList.push(message)
        this.messagesList = newMessageList
        this.sentMessage = true
        this.messageText = ''
        this.editableMessage = null
        return message
      } catch (error) {
        let message = 'Non Ã¨ stato possibile inviare il messaggio'
        apiErrorNotify({error, message})
      }


    },
    filterMessages(val) {
      if (val?.length === 0) {
        this.cleanSearch(false)
      }


    },

    onInputSearchFab(val) {
      this.showSearchInput = val
      this.splitterModel = MAX_SPLITTER
      this.cleanSearch()
    },
    cleanSearch(cleanInput = true) {
      if (cleanInput) {
        this.searchInput = ''
      }

      this.showFilteredMsg = false
      this.messageFocused = null
      this.splitterModel = MAX_SPLITTER
    },
    showFilteredMessages() {
      if (this.searchInput?.length === 0) return

      if (this.$q.screen.gt.sm) {
        this.splitterModel = 60
      } else {
        this.splitterModel = MAX_SPLITTER
        let length = this.filteredMessageList.length
        if (length > 0) {

          this.messageFocused = this.filteredMessageList[0]
          this.messageFocusedIndex = 0
          this.onSearchMessage(this.messageFocused)
        }
      }
      this.showFilteredMsg = true
    },
    onSearchMessage(message) {
      this.messageFocused = message
      let index = this.orderedMessageList.indexOf(message)
      console.log('onSearchMessage', index)

      this.$nextTick(() => {
        let messageItem = this.getMessageRef(message.id)
        if (messageItem) scrollToElement(messageItem);
      });

    },
    onEditMessage(message) {
      console.log('onEditMessage')
      this.isEditingMsg = true;
      this.editableMessage = message;
      this.messageId = message.id;
      this.messageText = message.testo ?? '';
    },
    closeEditing() {
      this.isEditingMsg = false
      this.editableMessage = null
      this.messageText = ''
 Ã¬
    },
    onFocusMessage(index) {
      console.log('onFocusMessage', index)
      this.messageFocused = this.filteredMessageList[index]
      console.log([this.messageFocused, index])
      this.messageFocusedIndex = index
      this.onSearchMessage(this.messageFocused)
    },


    async showDocument(attachment) {
      console.log("attachment", attachment);
    },

    scrollFire(position, message) {
      if (message.letto || message.autore?.tipo === MSG_AUTHOR_MAP.DOCTOR) {
        return
      }
      let messageItem = this.getMessageRef(message.id)
      let offset = messageItem.offsetTop
      let scrollArea = this.$refs.messageScrollArea.getScroll()
      let scrollAreaHeight = scrollArea.verticalContainerSize
      if (offset - position <= scrollAreaHeight && offset > position) {
        this.setByRead(message)
      }


    },
    async setByRead(message) {

      //Evitiamo di chiamare diverse volte il servizio prima della risposta
      let index = this.messagesList.indexOf(message)
      this.messagesList[index].letto = true

      try {
        let response = await setMessageAsRead(this.id, message.id);
      } catch (e) {
        console.error(e);
      }


    },
    getMessageRef(id) {
      return this.$refs['messageItem' + id][0]
    },
    onClickBlockBtn() {

        this.showBlockMotivationDialog = true
    },
    onHideEnrollmentGuard() {
      this.isEnrollmentGuardVisible = false
    },
    toggleMoreInfo() {
      this.isShowingMoreInfo = !this.isShowingMoreInfo
    },
    async enablePatient() {
      this.isEnabling = true;
      let payload = {
        abilitazione: true,
        assistiti: [this.patientTaxCode],
        motivazione_medico: null,
      };

      try {
        let response = await managePatients(payload);
        this.conversation.sola_lettura = false;
      } catch (error) {
        let message = "Non Ã¨ stato possibile abilitare l'assistito";
        apiErrorNotify({error, message});
      }
      await this.refreshList()
      this.isEnabling = false;
    },
    onBlock(blockMessage) {
      this.conversation.sola_lettura = true;

      this.conversation.motivo_blocco = {
        codice: BLOCK_REASONS_MAP.DOCTOR_CHOICE,
        descrizione: blockMessage,
      };

      this.conversation.motivazione_medico = blockMessage;
    },
    async refreshList() {
      let params = {
        limit: 1,
        offset: 0,
        sola_lettura: CONVERSATION_STATUS_MAP.ALL,
        id_conversazione: this.id,
      };
      try {
        this.isLoading = true
        let {data: list} = await getConversationList({params});
        this.conversation = list[0];
        this.isLoading = false
      } catch (error) {
        this.isLoading = false
        let message = "Non Ã¨ stato possibile recuperare la conversazione.";
        apiErrorNotify({error, message});
      }
    },
  }
}
</script>

<style lang="sass">
.conversation-argument
  opacity: 1
  transition: all 0.5s ease-out 0s
  &.argument-hidden
    opacity: 0
    transition: all 0.5s ease-out 0s


.block-btn
  &.readonly
    cursor: default

    &:hover, &:active, &:focus
      .q-focus-helper
        background: transparent !important


.search-input
  margin-right: 60px
  position: absolute
  width: 0px !important
  right: 16px
  transition: width 0.5s ease-out 0s

  .search-input__field
    opacity: 0
    transition: 0.5s

  &.active
    width: 300px !important
    transition: width 0.5s ease-out 0s

    .search-input__field
      opacity: 1
      transition: 0.5s

@media (max-width: $breakpoint-md-min)
  .search-input
    &.active
      width: 250px !important

#message-splitter
  .q-splitter__panel.q-splitter__before
    transition: width 0.2s linear 0s

#new-message-input
  textarea
    max-height: 85px

a.csi-link--black
  color: black
  font-weight: bold
  text-decoration: none
  transition: color .4s ease-in-out
  i
    color: black

  &:hover
    color: $csi-link-hover-color
    i
      color: $csi-link-hover-color !important
      transition: color .4s ease-in-out

</style>
