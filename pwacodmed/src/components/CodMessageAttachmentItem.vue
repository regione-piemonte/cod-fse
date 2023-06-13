<!--
  - Copyright Regione Piemonte - 2023
  - SPDX-License-Identifier: EUPL-1.2
  -->

<template>
  <div>
    <a
      :href="attachmentName"
      class="csi-link"
      @click.prevent.stop="showDocument"
    >
      <div class="row">
        <div class="col-auto q-pr-xs">
          <q-icon name="attach_file" size="xs" />
        </div>

        <div class="col">{{ attachmentName }}</div>

        <div class="col-auto q-pl-lg">
          <q-spinner-dots
            color="primary"
            size="20px"
            v-if="isLoading || isDownloadingDocument"
          />
        </div>
      </div>
    </a>
    <!-- IL PULSANTE SI ATTIVERA' SOLO SE E' POSSIBILE APRIRE IL DOC L'AZIONE WINDOW OPEN DEVE ESSERE DIRETTA DELL'UTENTE NON RICHIAMATA DA JAVASCRIPT -->
    <div v-if="docIframe" class="q-pl-md text-primary text-italic">
      <q-btn
        class="text-body1"
        color="primary"
        dense
        flat
        no-caps
        @click="openDoc()"
      >
        Apri documento
      </q-btn>
    </div>

    <div v-if="transcription" class="q-pl-md text-primary text-italic">
      <q-btn
        class="text-body1"
        color="primary"
        dense
        flat
        no-caps
        @click="isSelfContributed = true;"
      >
        Apri documento
      </q-btn>
    </div>

    <div v-if="hasImage && !isLoading" class="q-mt-sm">
      <q-btn
        v-if="canBookImage"
        :loading="isBookingImage"
        class="text-body1"
        color="primary"
        dense
        flat
        no-caps
        @click="onBookImage"
      >
        Prenota immagine
      </q-btn>

      <q-btn
        v-else-if="canDownloadImage"
        class="text-body1"
        color="primary"
        dense
        flat
        no-caps
        @click="showDownloadImageDialog = true"
      >
        Scarica immagine
      </q-btn>
      <div
        v-else-if="isImageInElaboration"
        class="q-pl-md text-primary text-italic"
      >
        Le immagini sono in corso di elaborazione, riprovare piÃ¹ tardi.
      </div>
      <div v-else-if="isImageError" class="q-pl-md text-primary text-italic">
        Attenzione! Si Ã¨ verificato un problema con lo scarico delle immagini, Ã¨ necessario contattare lâassistenza.
      </div>
    </div>
    <q-dialog v-model="isSelfContributed" :maximized="$q.screen.lt.sm">
      <q-card>
        <q-toolbar>
          <q-toolbar-title> Dettaglio allegato </q-toolbar-title>
          <q-btn
            v-close-popup
            aria-label="chiudi finestra"
            flat
            icon="close"
            round
            @click="isSelfContributed = false"
          />
        </q-toolbar>
        <q-card-section>
          {{ transcription }}
        </q-card-section>
      </q-card>
    </q-dialog>

    <q-dialog v-model="showDownloadImageDialog" ref="downloadImageDialog">
      <q-card style="max-width: 800px">
        <q-toolbar>
          <q-toolbar-title> Scarica immagine </q-toolbar-title>

          <q-btn
            v-close-popup
            aria-label="chiudi finestra"
            flat
            icon="close"
            round
          />
        </q-toolbar>

        <q-card-section class="q-gutter-y-md">
          <csi-banner type="info" class="q-mt-md">
            <strong>Attenzione!</strong><br />
            Lo scarico del file contenente le immagini potrebbe richiedere tempi lunghi di attesa: quando sarÃ  terminato, il sistema aprirÃ  una nuova finestra per consentire di salvare il file sul proprio dispositivo.
          </csi-banner>

          <csi-buttons>
            <csi-button :loading="isDownloadingImage" @click="onDownloadImage"
              >Scarica immagine</csi-button
            >
            <csi-button v-close-popup outline>Annulla</csi-button>
          </csi-buttons>
        </q-card-section>
      </q-card>
    </q-dialog>
  </div>
</template>

<script>
import {
  createImageBooking,
  getDocumentFse,
  getDocumentImageDownloadUrl,
  getDocumentPersonal,
  getImageStatus,
} from "src/services/api";
import {
  DOCUMENT_CATEGORY_MAP,
  DOCUMENT_TYPE_CODE_LIST_IMAGE_DOWNLOADABLE,
  IMAGE_STATUS_CODE_LIST_BOOKABLE,
  IMAGE_STATUS_CODE_LIST_DOWNLOADABLE,
  IMAGE_STATUS_CODE_LIST_ERROR,
  IMAGE_STATUS_CODE_LIST_IN_ELABORATION,
} from "src/services/config";
import { apiErrorNotify, notifySuccess } from "src/services/utils";
import { openURL } from "quasar";
import store from "src/store";
import { convertToBlobUrl } from "src/services/business-logic";
import { saveAs } from "file-saver";
export default {
  name: "CodMessageAttachmentItem",
  components: {},
  props: {
    attachment: { type: Object, default: null },
    patientTaxCode: { type: String, default: null },
  },
  computed: {
    attachmentId() {
      return this.attachment?.id_documento_ilec;
    },
    attachmentCl() {
      return this.attachment?.codice_cl;
    },
    attachmentName() {
      return this.attachment?.nome_allegato ?? "-";
    },
    hasImage() {
      return (
        this.attachment?.accession_numbers?.length > 0 &&
        this.isFse &&
        DOCUMENT_TYPE_CODE_LIST_IMAGE_DOWNLOADABLE.includes(
          this.attachment?.tipo_documento?.codice
        )
      );
    },
    canBookImage() {
      return IMAGE_STATUS_CODE_LIST_BOOKABLE.includes(this.imageStatus);
    },
    isImageInElaboration() {
      return IMAGE_STATUS_CODE_LIST_IN_ELABORATION.includes(this.imageStatus);
    },
    isImageError() {
      return IMAGE_STATUS_CODE_LIST_ERROR.includes(this.imageStatus);
    },
    canDownloadImage() {
      return IMAGE_STATUS_CODE_LIST_DOWNLOADABLE.includes(this.imageStatus);
    },
    isFse() {
      return this.attachment?.categoria_tipologia === DOCUMENT_CATEGORY_MAP.FSE;
    },
    isPersonal() {
      return (
        this.attachment?.categoria_tipologia === DOCUMENT_CATEGORY_MAP.PERSONAL
      );
    },
    docIframe (){
      return this.iframeDoc? true : false;
    }
  },
  data() {
    return {
      isLoading: false,
      imageStatus: null,
      isBookingImage: false,
      isDownloadingDocument: false,
      isDownloadingImage: false,
      isSelfContributed: false,
      transcription: null,
      showDownloadImageDialog: false,
      iframeDoc: null,
    };
  },
  async created() {
    if (!this.hasImage || !this.attachment) return;

    this.isLoading = true;

    await this.verifyImageStatus();
    this.isLoading = false;
  },
  methods: {
    openDoc() {
      let win = window.open();
      win.document.open();
      win.document.write(this.iframeDoc);
      win.document.close();
    },
    async verifyImageStatus() {
      let params = {
        codice_fiscale: this.patientTaxCode,
        id_documento_ilec: this.attachmentId,
        cod_cl: this.attachmentCl,
        cod_documento_dipartimentale:
          this.attachment.codice_documento_dipartimentale,
        archivio_documento_ilec: "FSE",
      };
      try {
        let { data } = await getImageStatus({ params });
        this.imageStatus = data?.stato_richiesta;
      } catch (e) {}
    },
    async showDocument() {
      this.isDownloadingDocument = true;
      if (this.isFse) {
        await this.downloadDocumentFse();
      } else if (this.isPersonal) {
        await this.downloadDocumentPersonal();
      }
      this.isDownloadingDocument = false;
    },

    async downloadDocumentFse() {
      this.iframeDoc = null;
      let payload = {
        id_documento_ilec: this.attachmentId,
        codice_componente_locale: this.attachmentCl,
        firmato: "S",
        codice_documento: this.attachment?.codice_documento_dipartimentale,
        identificativo_repository: this.attachment?.id_repository_cl,
      };

      try {
        let { data: attachment } = await getDocumentFse(
          this.patientTaxCode,
          payload
        );
        let file = attachment?.allegato;
        let fileType = attachment?.tipo_allegato;
        this.openAttachment(file, fileType);
      } catch (error) {
        let message = "Non Ã¨ stato possibile scaricare l'allegato.";
        apiErrorNotify({ error, message });
      }
    },
    async downloadDocumentPersonal() {
      this.iframeDoc = null;
      this.transcription = null;
      try {
        let { data: attachment } = await getDocumentPersonal(
          this.patientTaxCode,
          this.attachmentId
        );
        let file = attachment?.allegato;
        let fileType = attachment?.tipo_allegato;
        let transcription = attachment?.trascrizione;
        if (fileType) {
          this.openAttachment(file, fileType);
        }
        if (transcription) {
          this.transcription = transcription;
        }
      } catch (error) {
        let message = "non Ã¨ stato possibile scaricare l'allegato.";
        apiErrorNotify({ error, message });
      }
    },
    openAttachment(file, fileType) {
      let url = `data:${fileType};base64, ${file}`;
      let iframe = `<iframe width='100%' height='100%' src="${url}" allowfullscreen></iframe>`;
      this.iframeDoc = iframe;
    },
    async onBookImage() {
      this.isBookingImage = true;
      try {
        let payload = {
          cit_id: this.patientTaxCode,
          id_referto: this.attachment?.codice_documento_dipartimentale,
          periodo_conservazione: "30",
          pin: "12345",
          accession_number: this.attachment.accession_numbers,
          cod_cl: this.attachmentCl,
        };
        let { data } = await createImageBooking(payload);
        await this.verifyImageStatus();
        notifySuccess(
          "La richiesta Ã¨ stata inviata correttamente. Ã necessario uscire da questa pagina e accedere di nuovo  successivamente."
        );
      } catch (error) {
        let message = "Attenzione! Si Ã¨ verificato un problema con la prenotazione delle immagini, Ã¨ necessario contattare lâassistenza.";
        apiErrorNotify({ error, message });
      }
      this.isBookingImage = false;
    },
    async onDownloadImage() {
      this.isDownloadingImage = true;
      try {
        let payload = {
          cf_assistito: this.patientTaxCode,
          id_documento_ilec: this.attachmentId,
          cod_cl: this.attachmentCl,
        };
        let url = await getDocumentImageDownloadUrl(payload);
        notifySuccess(
          "Il pacchetto delle immagini richieste Ã¨ stato scaricato correttamente. Il sistema aprirÃ  una nuova finestra per consentire di salvare il file sul proprio dispositivo."
        );
        window.open(url);
      } catch (error) {
        let message = "Non Ã¨ stato possibile scaricare l'immagine";
        apiErrorNotify({ error, message });
      }
      this.showDownloadImageDialog = false;
      this.isDownloadingImage = false;
    },
  },
};
</script>

<style scoped></style>
