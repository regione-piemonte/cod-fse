/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

export const ERROR_404 = {
  path: "*",
  name: "error404",
  component: () => import("pages/PageError404.vue")
};


// PAGINE VERTICALE
// ---------------------------------------------------------------------------------------------------------------------
export const PROFILE = {
  path: "profilo-utente",
  name: "userProfile",
  component: () => import("pages/PageProfile.vue")
};

export const CONVERSATION_LIST = {
  path: "conversazioni",
  name: "conversationList",
  component: () => import("pages/PageConversationList.vue")
};


export const CONVERSATION_DETAIL = {
  path: ":id",
  meta:{
    hide_layout:true
  },
  name: "conversationDetail",
  component: () => import("pages/PageConversationDetail.vue")
};

export const ADHESION_NEW = {
  path: "adesione",
  name: "adhesionNew",
  component: () => import("pages/PageAdhesionNew.vue")
};
export const PATIENT_LIST = {
  path: "elenco-pazienti",
  name: "patientList",
  component: () => import("pages/PagePatientList.vue")
};


export const SETTINGS = {
  path: "impostazioni",
  name: "settings",
  component: () => import("pages/PageSettings.vue")
};

export const HOME = {
  path: "/",
  name: "home",
  component: () => import("pages/PageHome.vue"),
  children: [
    CONVERSATION_LIST,
    SETTINGS
  ]
};


export const APP = {
  path: "",
  name: "app",
  component: () => import("pages/AppCod.vue"),
  children: [
    HOME,
    CONVERSATION_DETAIL,
    ADHESION_NEW,
    PATIENT_LIST,
  ]
};

export const LAYOUT_APP = {
  path: "/",
  component: () => import("layouts/LayoutApp.vue"),
  children: [
    APP,

  ]
};

export const LAYOUT_CONVERSATION_DETAIL = {
  path: "/conversazione",
  component: () => import("layouts/LayoutConversationDetail.vue"),
  children: [
    APP,
    CONVERSATION_DETAIL,
  ]
};

const routes = [LAYOUT_APP,LAYOUT_CONVERSATION_DETAIL];

// Always leave this as last one
if (process.env.MODE !== "ssr") {
  routes.push(ERROR_404);
}

export default routes;
