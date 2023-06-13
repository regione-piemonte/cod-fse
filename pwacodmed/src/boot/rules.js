/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import * as rules from "src/services/rules";

export default ({ app, router, store, Vue }) => {
  Vue.prototype.$rules = rules;
};
