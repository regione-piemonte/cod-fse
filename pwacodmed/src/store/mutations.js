/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export const SET_USER = (state, { user }) => {
  state.user = user;
};

export const SET_DOCTOR_INFO = (state, { info }) => {
  state.doctorInfo = info;
};
export const SET_ENABLED_PATIENTS = (state, patients) => {
  state.enabledPatients = patients;
};

export const SET_ENABLE_ALL_MODAL_BLOCK = (state, status ) => {
  state.enableAllModalRunningBlock = status;
};

