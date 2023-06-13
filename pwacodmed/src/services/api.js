/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import {httpApiopsanAuth, httpAuth, httpPublic, httpServletAuth} from "src/boot/http";
import store from "src/store";
import {uid} from "quasar";

// API APIOPSAN
export const getUser = (httpConfig = {}) => {
  const url = `/apiopsan/api/v1/login/utente`;
  return httpPublic.get(url, httpConfig);
};

// API APICODOPSAN
export const getDoctorInfo = (httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/info`;
  return httpAuth.get(url, httpConfig);
};


export const manageAdhesion = (payload, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/adesione`;
  return httpAuth.post(url, payload, httpConfig);
};

export const setReadNotifications = (payload, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/notifica-lettura`;
  return httpAuth.put(url, payload, httpConfig);
};

export const getPatientsList = (httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/assistiti`;
  return httpAuth.get(url, httpConfig);
};

export const managePatients = (payload, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/assistiti`;
  return httpAuth.post(url, payload, httpConfig);
};

export const enableAllPatients = (payload, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/assistiti/tutti`;
  return httpAuth.post(url, payload, httpConfig);
};

export const searchPatients = (httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/assistiti/_search`;
  return httpAuth.get(url, httpConfig);
};

export const getPatologyExceptions = (httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/esenzioni-patologia`;
  return httpAuth.get(url, httpConfig);
};

export const getConversationList = (httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/conversazioni`;
  return httpAuth.get(url, httpConfig);
};

export const getMessageList = (idConversation, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/conversazioni/${idConversation}/messaggi`;
  return httpAuth.get(url, httpConfig);
};

export const setMessageNew = (idConversation, payload, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/conversazioni/${idConversation}/messaggi`;
  return httpAuth.post(url, payload, httpConfig);
};

export const editMessage = (idConversation, idMessage, payload, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/conversazioni/${idConversation}/messaggi/${idMessage}`;
  return httpAuth.put(url, payload, httpConfig);
};

export const setMessageAsRead = (idConversation, idMessage, httpConfig = {}) => {
  const url = `/apicodopsan/api/v1/conversazioni/${idConversation}/messaggi/${idMessage}/letto`;
  return httpAuth.put(url, httpConfig);
};

// API AURA
export const getSearchAura = (httpConfig = {}) => {
  const url = `/apiopsanaura/api/v1/assistiti`;
  return httpAuth.get(url, httpConfig);
};

export const getPatologyExceptionsAura = (httpConfig = {}) => {
  const url = `/apiopsanaura/api/v1/esenzioni-patologia`;
  return httpAuth.get(url, httpConfig);
};


// DOCUMENTI -  API APIOPSAN
// ----------------------------------------------------------------------------------------------------------

export const getImageStatus = (httpConfig = {}) => {
  const url = `/apiopsan/api/v1/referti/verifica-stato-pacchetto`;
  return httpApiopsanAuth.get(url, httpConfig);
};


export const createImageBooking = (payload, httpConfig = {}) => {
  const url = `/apiopsan/api/v1/referti/prenotazione-pacchetto-immagini`;
  return httpApiopsanAuth.post(url, payload, httpConfig);
};


export const getDocumentFse = (idPatient, payload, httpConfig = {}) => {
  const url = `/apiopsan/api/v1/cittadini/${idPatient}/documento/_search`;
  httpConfig.responseType = 'json'
  return httpApiopsanAuth.post(url, payload, httpConfig);
};

export const getDocumentPersonal = (idPatient, idDocument, httpConfig = {}) => {
  let url = `/apiopsan/api/v1/cittadini/${idPatient}/documenti/${idDocument}/personale`;
  return httpApiopsanAuth.get(url, httpConfig);
};

export const getAgreement = (idPatient, httpConfig = {}) => {
  let url = `/apiopsan/api/v1/cittadini/${idPatient}/consenso`;
  return httpApiopsanAuth.get(url, httpConfig);
};


export const getDocumentImageDownloadUrl = (payload, httpConfig = {}) => {
  let cfAssistito = payload.cf_assistito;
  let idDocumentoIlec = payload.id_documento_ilec;
  let codCL = payload.cod_cl;
  let appCode = process.env.APP_CODE
  let roleParams = store.getters["getRoleParams"]
  let codRuolo = roleParams?.ruolo
  let codRegime =  roleParams?.regime;
  let idCollocazione = roleParams?.collocazione_codice;
  let requestId = uid()
  let baseUrl = process.env.APP_IS_PROD
      ? "https://ap-tint-dmass.nivolapiemonte.it/dmasssrv/scaricoPacchettoOperatoreSanitario"
      : "https://ts-ap-tint-dmass.nivolapiemonte.it/dmasssrv/scaricoPacchettoOperatoreSanitario";

  const url = `${baseUrl}?cfAssistito=${cfAssistito}&idDocumentoIlec=${idDocumentoIlec}&codCL=${codCL}&archivioDocumentoIlec=FSE&codApplicazione=${appCode}&codVerticale=${appCode}&codRuolo=${codRuolo}&codRegime=${codRegime}&idCollocazione=${idCollocazione}&pin=12345&requestId=${requestId}`;
  return httpAuth.getUri({url, ...httpConfig});
};
//
// export const getDocumentImageDownloadUrl = ( httpConfig = {}) => {
//   // let cfAssistito = payload.cf_assistito;
//   // let idDocumentoIlec = payload.id_documento_ilec;
//   // let codCL = payload.cod_cl;
//   // let appCode = process.env.APP_CODE
//   // let roleParams = store.getters["getRoleParams"]
//   // let codRuolo = roleParams?.ruolo
//   // let codRegime =  roleParams?.regime;
//   // let idCollocazione = roleParams?.collocazione_codice;
//
//   let
//     url = process.env.APP_IS_PROD
//       ? "https://ap-tint-dmass.nivolapiemonte.it/dmasssrv/scaricoPacchettoOperatoreSanitario"
//       : "https://ts-ap-tint-dmass.nivolapiemonte.it/dmasssrv/scaricoPacchettoOperatoreSanitario";
//   httpConfig.responseType= 'blob'
//   // const url = `${baseUrl}?cfAssistito=${cfAssistito}&idDocumentoIlec=${idDocumentoIlec}&codCL=${codCL}&archivioDocumentoIlec=FSE&codApplicazione=${appCode}&codVerticale=${appCode}&codRuolo=${codRuolo}&codRegime=${codRegime}&idCollocazione=${idCollocazione}&pin=1234`;
//    return httpAuth.get(url, httpConfig);
// };
