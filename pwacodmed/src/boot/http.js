/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import axios from "axios";
import {uid} from "quasar";
import store from "src/store";
import {IS_DEV, IS_TEST} from "src/services/config";
import {MOCK_USER_CF_IRIDE, MOCK_USER_IDENTITY_IRIDE} from "src/services/mocks";

export const httpAuth = axios.create({
  baseURL: process.env.APP_API_BASE_URL
});

export const httpPublic = axios.create({
  baseURL: process.env.APP_API_BASE_URL
});
export const httpApiopsanAuth = axios.create({
  baseURL: process.env.APP_API_BASE_URL
});

export const httpServletAuth = axios.create({
  baseURL:  process.env.SERVLET_API_BASE_URL
});

apiInterceptorRequestId(httpAuth)
apiInterceptorRequestId(httpPublic, false)
apiopsanInterceptorRequestId(httpApiopsanAuth)
apiopsanInterceptorRequestId(httpServletAuth, true)


function apiInterceptorRequestId(http, isAuth = true) {
  http.interceptors.request.use(config => {
    config.headers["X-Request-Id"] = uid();
    config.headers["X-Codice-Servizio"] = process.env.APP_CODE;
    config.headers["X-Codice-Verticale"] = process.env.APP_CODE;

    if(isAuth){
      let roleParams = store.getters["getRoleParams"]
      config.params = config.params ?? {}
      config.params["ruolo"] = roleParams?.ruolo;
      config.params["collocazione_codice"] = roleParams?.collocazione_codice;
      config.params["collocazione_descrizione"] = roleParams?.collocazione_descrizione;

    }

    config = getDEVParams(config)
    return config;
  });
}
function apiopsanInterceptorRequestId(http, servlet=false) {
  http.interceptors.request.use(config => {
    let roleParams = store.getters["getRoleParams"]
    config.headers = config.headers ?? {}
    config.headers["X-Request-Id"] = uid();
    config.headers["X-Codice-Servizio"] = process.env.APP_CODE;
    config.headers["X-Codice-Verticale"] = process.env.APP_CODE;

    if(!servlet){
      config.headers["ruolo"] = roleParams?.ruolo;
      config.headers["collocazione"] = roleParams?.collocazione_codice;
      config.headers["regime"] = roleParams?.regime;
    }


    config = getDEVParams(config)
    return config;
  });
}



function getDEVParams(config) {
  if(IS_DEV){
    config.headers["X-Forwarded-for"] = "127.0.0.1"
    config.headers["Shib-Iride-IdentitaDigitale"] = MOCK_USER_IDENTITY_IRIDE
    config.headers["Shib-Identita-CodiceFiscale"] =  MOCK_USER_CF_IRIDE;
  }


  return config
}
