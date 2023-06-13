/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import * as config from "src/services/config";

export default ({ app, router, store, Vue }) => {
  Vue.prototype.$c = config;
};
