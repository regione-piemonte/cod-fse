/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import VueSync from "vue-sync";
import CsiButton from "components/core/CsiButton";
import CsiButtons from "components/core/CsiButtons";
import CsiInnerLoading from "components/core/CsiInnerLoading";
import CsiPage from "components/core/CsiPage";
import CsiPageTitle from "components/core/CsiPageTitle";
import CsiBanner from "components/core/CsiBanner";

export default ({ app, router, store, Vue }) => {
  Vue.use(VueSync);

  Vue.component(CsiButton.name, CsiButton);
  Vue.component(CsiButtons.name, CsiButtons);
  Vue.component(CsiInnerLoading.name, CsiInnerLoading);
  Vue.component(CsiPage.name, CsiPage);
  Vue.component(CsiPageTitle.name, CsiPageTitle);
  Vue.component(CsiBanner.name, CsiBanner);
};
