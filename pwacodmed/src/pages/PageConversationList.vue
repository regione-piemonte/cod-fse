<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div class="page-conversation-list q-pb-xl">
    <!-- FILTRI -->
    <!-- ----------------------------------------------------------------------------------------------------------- -->
    <q-card class="bg-transparent" flat>
      <q-card-section class="q-px-none">
        <template>
          <div class="text-right">
            <q-btn
              flat
              icon="filter_list"
              label="Filtri"
              text-color="black"
              @click="openFilters"
            />
          </div>

          <q-slide-transition>
            <div v-show="isOpenFilters">
              <div class="row items-center q-col-gutter-lg q-mb-lg">
                <div class="col-12 col-md">
                  <q-input
                    v-model="patientSurname"
                    clearable
                    dense
                    label="Cognome assistito"
                  />
                </div>
                <div class="col-12 col-md">
                  <csi-input-tax-code
                    v-model="patientTaxCode"
                    label="Codice fiscale assistito"
                  />
                </div>
              </div>
              <div class="row items-center q-col-gutter-lg q-mb-md">
                <div class="col-12 col-md">
                  <q-checkbox
                    v-model="readOnly"
                    dense
                    label="Nascondi conversazioni in sola lettura"
                  />
                </div>
                <div class="col-12 col-md">
                  <csi-buttons>
                    <csi-button outline @click="setFilters" no-min-width
                      >Filtra</csi-button
                    >
                  </csi-buttons>
                </div>
              </div>
            </div>
          </q-slide-transition>
        </template>
      </q-card-section>
    </q-card>

    <div v-if="isLoading" class="row items-center justify-center">
      <div v-for="i in 5" :key="i" class="q-my-md col-12 col-md-11">
        <cod-conversation-item-skeleton />
      </div>
    </div>

    <div
      v-else-if="conversationList.length > 0"
      class="row items-center justify-center"
    >
      <div
        v-for="(conversation, index) in conversationList"
        :key="index"
        class="q-my-sm col-12"
      >
        <cod-conversation-item
          :conversation="conversation"
          @manage-patient="onReload"
        />
      </div>
      <template v-if="hasMoreConversations">
        <div class="q-mt-md text-center">
          <q-btn
            :loading="isLoading"
            color="primary"
            dense
            flat
            @click="loadConversationList()"
          >
            Carica altre
          </q-btn>
        </div>
      </template>
    </div>
    <template v-else>
      <csi-banner type="info">
        Non ci sono conversazioni
      </csi-banner>
    </template>
  </div>
</template>

<script>
import CodConversationItem from "components/CodConversationItem";
import { CONVERSATION_STATUS_MAP } from "src/services/config";
import CsiInputTaxCode from "components/core/CsiInputTaxCode";
import { getConversationList } from "src/services/api";
import CodConversationItemSkeleton from "components/loaders/CodConversationItemSkeleton";
import { apiErrorNotify } from "src/services/utils";

const LIMIT = 20;
export default {
  name: "PageConversationList",
  components: {
    CodConversationItemSkeleton,
    CsiInputTaxCode,
    CodConversationItem
  },
  data() {
    return {
      isLoading: false,
      patientTaxCode: null,
      patientSurname: null,
      conversationList: [],
      argumentFilter: null,
      readOnly: false,
      isOpenFilters: false,
      offset: 0,
      firstSearch: false,
      conversationListTotalCount:  Number.MAX_SAFE_INTEGER
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
    roleParams() {
      return this.$store.getters["getRoleParams"];
    },
    hasMoreConversations() {
      return (
        this.conversationList.length < this.conversationListTotalCount
      );
    }
  },
  async created() {
    this.onReload();
  },
  methods: {
    openFilters() {
      this.isOpenFilters = !this.isOpenFilters;
    },

    async setFilters() {
      this.offset = 0;
      this.conversationList = [];
      this.isLoading = true;
      await this.loadConversationList();
      this.isLoading = false;
    },
    async loadConversationList(reload) {
      if(reload) this.conversationListTotalCount = Number.MAX_SAFE_INTEGER

      let readOnly = this.readOnly
        ? CONVERSATION_STATUS_MAP.ACTIVE
        : CONVERSATION_STATUS_MAP.ALL;
      let params = {
        ruolo: this.roleParams?.ruolo,
        collocazione_codice: this.roleParams?.collocazione_codice,
        collocazione_descrizione: this.roleParams?.collocazione_descrizione,
        limit: LIMIT,
        offset: reload ? 0 : this.offset,
        sola_lettura: readOnly,
        cognome: this.patientSurname
          ? this.patientSurname.toUpperCase()
          : this.patientSurname,
        codice_fiscale: this.patientTaxCode
      };

      try {
        let response = await getConversationList({ params });
        let conversations = response?.data;
        let headers = response?.headers;
        if (reload) {
          this.conversationList = conversations;
        } else {
          this.conversationList = [...this.conversationList, ...conversations];
        }
        this.offset = this.conversationList.length;
        this.conversationListTotalCount = parseInt(headers["x-total-elements"]);
        this.firstSearch = true;
      } catch (error) {
        let message =
          "Non Ã¨ stato possibile recuperare la lista delle conversazioni.";
        apiErrorNotify({ error, message });
      }
    },

    async onReload() {
      this.isLoading = true;
      await this.loadConversationList("reload");
      this.isLoading = false;
    }
  }
};
</script>

<style scoped></style>
