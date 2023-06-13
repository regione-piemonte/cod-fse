/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export const setUser = (context, { user }) => {
  context.commit("SET_USER", { user });
};
export const setDoctorInfo = (context, { info }) => {
  context.commit("SET_DOCTOR_INFO", { info });
};
export const setEnabledPatients = (context, patients ) => {
  context.commit("SET_ENABLED_PATIENTS", patients);
};

export const setEnableAllModalRunningBlock = (context, status) => {
  context.commit("SET_ENABLE_ALL_MODAL_BLOCK", status );
};


