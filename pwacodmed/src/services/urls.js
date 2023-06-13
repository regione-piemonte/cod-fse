/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import { store } from "src/store";

export const urlPua = () => {
  let url = "https://tst-pua.salutepiemonte.it/pua/#/";
  if (process.env.APP_IS_PROD) url = "https://pua.salutepiemonte.it/pua/#/";
  return url;
};
