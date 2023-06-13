/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { orderBy } from "src/services/utils";
import { date } from "quasar";

const { subtractFromDate, isBetweenDates } = date;


export const getUser = state => {
  return state.user?.richiedente;
};


export const getTaxCode = state => {
  return state.user?.richiedente?.codice_fiscale;
};


export const getRoleCode = state => {
  return state.user?.richiedente?.ruolo?.codice;
};
export const getPlacementCode = state => {
  return state.user?.richiedente?.collocazione?.codice_collocazione;
};
export const getRoleParams = (state, getters) => {
  let placementCode = getters["getPlacementCode"]
  let roleCode = getters["getRoleCode"]
  let roleParams = {
    ruolo: roleCode,
    collocazione_codice: placementCode,
    collocazione_descrizione: state.user?.richiedente?.collocazione?.descrizione_collocazione,
    regime: "AMB"
  }

  return (placementCode && roleCode) ? roleParams : null
};


export const getDoctorInfo = state => {
  return state.doctorInfo;
};

export const getEnableAllModalRunningBlock = state => {
  return state.enableAllModalRunningBlock;
};

export const isValidAdhesion = state =>{
    let doctorInfo= state.doctorInfo
    if(!doctorInfo) return false

    let startDate = doctorInfo?.data_inizio_adesione
    let endDate = doctorInfo?.data_fine_adesione
    let now = new Date()
    let isValidStartDate =  date.getDateDiff(startDate, now, 'days') <= 0
    let isValidEndDate = date.getDateDiff(endDate, now, 'days') > 0 || !endDate

    return isValidStartDate && isValidEndDate
}

export const getEnabledPatients = state => {
  return state.enabledPatients ?? [];
};
