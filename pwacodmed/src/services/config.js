/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import {getGenderLabel} from "src/services/business-logic";
import {date} from "quasar";

const {
  formatDate
} = date;


export const IS_DEV = process.env.APP_IS_DEV;
export const IS_TEST = process.env.APP_IS_TEST;
export const IS_PROD = process.env.APP_IS_PROD;

export const DELEGATION_STATUS_MAP = {
  ACTIVE: "ATTIVA",
  NOT_ACTIVE: "NON ATTIVA",
  REVOKED: "REVOCATA",
  REFUSED: "RIFIUTATA",
  IS_EXPIRING: "IN_SCADENZA",
  EXPIRED: "SCADUTA",
  UPDATED: "AGGIORNATA"
};

export const ENROLLMENT_CODES = {
  // Il soggetto  ha chiesto che non gli venisse piÃ¹ suggerito lâapertura del fascicolo.
  DO_NOT_ASK_ME_AGAIN: "000",
  // Il soggetto non Ã¨ piemontese
  NO_PIEDMONT: "001",
  // Il soggetto non Ã¨ maggiorenne
  NO_ADULT: "002",
  // Il soggetto ha un FSE con alimentazione a SI
  FSE_APPROVED: "003",
  // Il soggetto Ã¨ piemontese, maggiorenne ma non ha un FSE
  NO_FSE: "004",
  // Il soggetto Ã¨ piemontese, maggiorenne ma ha un FSE con alimentazione valorizzata a NO
  FSE_NOT_APPROVED: "005",
  UNKNOWN: "999"
};

export const MSG_AUTHOR_MAP ={
  PATIENT: 'assistito',
  DELEGATOR: 'delegato',
  DOCTOR: 'medico'
}

export const CONVERSATION_STATUS_MAP ={
  ACTIVE: 'A',
  BLOCKED: 'B',
  ALL: 'T'
}

export const ABILITATION_STATUS_MAP ={
  ACTIVE: 'A',//("ABILITATO")
  ALREADYACTIVE:'GIA_ABILITATO', //("GIA_ABILITATO")
  CANNOTACTIVATE:'NON_ABILITABILE', // ("NON_ABILITABILE")
  BLOCKED: 'B',//("DISABILITATO")    
  ALREADYBLOCKED:'GIA_DISABILITATO', //("GIA_DISABILITATO")
  CANNOTBLOCK:'NON_DISABILITABILE_SOGGETTO_MANCANTE',//("NON_DISABILITABILE_SOGGETTO_MANCANTE")
  ALL: 'T', // MAI RICEVUTO
}


export const GENDER_OPTIONS =[
  {label: 'Maschio', value:'M'},
  {label: 'Femmina', value:'F'},
]

export const ROWS_PER_PAGE = 3

export const TABLE_PATIENT_COLUMNS = [
  {
    name: 'surname',
    required: true,
    label: 'Cognome',
    align: 'left',
    field: row => row.cognome,
    format: val => val? `${val}`: '-',
    sortable: true
  },
  {
    name: 'name',
    required: true,
    label: 'Nome',
    align: 'left',
    field: row => row.nome,
    format: val => val? `${val}`: '-',
    sortable: true
  },
  {
    name: 'taxCode',
    required: true,
    label: 'Codice Fiscale',
    align: 'left',
    field: row => row.codice_fiscale,
    format: val => val? `${val}`: '-',
    sortable: true
  },
  {
    name: 'birthDate',
    required: true,
    label: 'Data di nascita',
    align: 'left',
    field: row => row.data_nascita,
    format: val =>  val?`${formatDate(val, "DD/MM/YYYY")}`:'-',
    sortable: true
  },
  {
    name: 'gender',
    required: true,
    label: 'Sesso',
    align: 'left',
    field: row => row.sesso,
    format: val => val?`${getGenderLabel(val)}`:'-',
    sortable: true
  },


]

export const TABLE_PATIENT_COLUMNS_AURA = [
  {
    name: 'surname',
    required: true,
    label: 'Cognome',
    align: 'left',
    field: row => row.cognome,
    format: val => val? `${val}`: '-',
    sortable: true
  },
  {
    name: 'name',
    required: true,
    label: 'Nome',
    align: 'left',
    field: row => row.nome,
    format: val => val? `${val}`: '-',
    sortable: true
  },
  {
    name: 'taxCode',
    required: true,
    label: 'Codice Fiscale',
    align: 'left',
    field: row => row.cod_fisc_ass,
    format: val => val? `${val}`: '-',
    sortable: true
  },
  {
    name: 'birthDate',
    required: true,
    label: 'Data di nascita',
    align: 'left',
    field: row => row.data_nascita,
    format: val => val? `${val}`: '-',
    sortable: true
  },
  {
    name: 'gender',
    required: true,
    label: 'Sesso',
    align: 'left',
    field: row => row.sesso,
    format: val => val?`${getGenderLabel(val)}`:'-',
    sortable: true
  },


]


export const PATIENT_ENABLEMENT_MAP = {
  PATIENT_LIST: 'list',
  ALL: 'all',
  NONE: 'nome'
}


export const BLOCK_REASONS_MAP = {
  DOCTOR_CHOICE : 'BLOMED',
  DOCTOR_CHANGE : 'CHGMED',
  DOCTOR_REVOKE: 'REVADE'
}




//DOCUMENTS

export const DOCUMENT_CATEGORY_MAP = {
  FSE: "FSE",
  PERSONAL: "PERSONALE"
};
// quali sono i tipi di referto per cui Ã¨ disponibile download delle immagini?
export const DOCUMENT_TYPE_CODE_LIST_IMAGE_DOWNLOADABLE = [ "REFERTO_RIS", "68604-8"];


// IMMAGINE
// ---------------------------------------------------------------------------------------------------------------------
// Mappa dei possibili stati della richiesta del download dell'immagine
export const IMAGE_STATUS_CODE_MAP = {
  NULL: null,
  ERRORE_WS: "ERRORE_WS",
  NON_PRESENTE: "RICHIESTA_NON_PRESENTE",
  SCARICATO: "SCARICATO",
  DA_ELABORARE: "DA_ELABORARE",
  ELAB_IN_CORSO: "ELAB_IN_CORSO",
  DISPONIBILE: "DISPONIBILE",
  INVIO_MAIL_IN_CORSO: "INVIO_MAIL_IN_CORSO",
  ERRORE_INVIO_MAIL: "ERRORE_INVIO_MAIL",
  NOTIFICATO: "NOTIFICATO",
  COMPOSIZIONE_PACCHETTO_SOSPESA: "COMPOSIZIONE_PACCHETTO_SOSPESA",
  ERRORE_COMPONI_PACCHETTO: "ERRORE_COMPONI_PACCHETTO",
  PACCHETTO_DA_CANCELLARE: "PACCHETTO_DA_CANCELLARE",
  PACCHETTO_IN_CANCELLAZIONE: "PACCHETTO_IN_CANCELLAZIONE",
  CANCELLAZIONE_PACCHETTO_SOSPESA: "CANCELLAZIONE_PACCHETTO_SOSPESA",
  ERRORE_IN_CANCELLAZIONE_PACCHETTO: "ERRORE_IN_CANCELLAZIONE_PACCHETTO",
  PACCHETTO_CANCELLATO: "PACCHETTO_CANCELLATO"
};

// quali sono gli stati dell'immagine per cui l'utente puÃ² prenotarla?
export const IMAGE_STATUS_CODE_LIST_BOOKABLE = [
  IMAGE_STATUS_CODE_MAP.NULL,
  IMAGE_STATUS_CODE_MAP.NON_PRESENTE,
  IMAGE_STATUS_CODE_MAP.PACCHETTO_DA_CANCELLARE,
  IMAGE_STATUS_CODE_MAP.PACCHETTO_IN_CANCELLAZIONE,
  IMAGE_STATUS_CODE_MAP.PACCHETTO_CANCELLATO,
  IMAGE_STATUS_CODE_MAP.CANCELLAZIONE_PACCHETTO_SOSPESA,
  IMAGE_STATUS_CODE_MAP.ERRORE_IN_CANCELLAZIONE_PACCHETTO
];

// Quali sono gli stati per cui l'immagine risulta "in eleborazione"
export const IMAGE_STATUS_CODE_LIST_IN_ELABORATION = [
  IMAGE_STATUS_CODE_MAP.ELAB_IN_CORSO,
  IMAGE_STATUS_CODE_MAP.DA_ELABORARE,
];


// Quali sono gli stati dell'immagine per cui l'utente deve contattare l'assistenza?
export const IMAGE_STATUS_CODE_LIST_ERROR = [
  IMAGE_STATUS_CODE_MAP.ERRORE_WS,
  IMAGE_STATUS_CODE_MAP.ERRORE_COMPONI_PACCHETTO,
  IMAGE_STATUS_CODE_MAP.COMPOSIZIONE_PACCHETTO_SOSPESA
];

// quali sono gli stati dell'immagine per cui l'utente puÃ² scaricarla?
export const IMAGE_STATUS_CODE_LIST_DOWNLOADABLE = [
  IMAGE_STATUS_CODE_MAP.DISPONIBILE,
  IMAGE_STATUS_CODE_MAP.INVIO_MAIL_IN_CORSO,
  IMAGE_STATUS_CODE_MAP.NOTIFICATO,
  IMAGE_STATUS_CODE_MAP.ERRORE_INVIO_MAIL,
  IMAGE_STATUS_CODE_MAP.SCARICATO
];

