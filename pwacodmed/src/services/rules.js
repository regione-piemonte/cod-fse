/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import {FORMAT_DATE, POST_FORMAT_DATE} from "src/services/config";

const EMAIL_REGEX = /^(?:[A-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\.[A-z0-9!#$%&'*+/=?^_`{|}~-]+)*|"(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21\x23-\x5b\x5d-\x7f]|[\x01-\x09\x0b\x0c\x0e-\x7f])*")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\.)+[a-z0-9]{2,}(?:[a-z0-9-]*[a-z0-9])?|\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\x01-\x08\x0b\x0c\x0e-\x1f\x21-\x5a\x53-\x7f]|\\[\x01-\x09\x0b\x0c\x0e-\x7f])+)\])$/i;
const ALPHA_REGEX = /^[a-zA-Z]*$/;
const NUMERIC_REGEX = /^[0-9]*$/;
const ALPHA_NUM_REGEX = /^[a-zA-Z0-9]*$/;
import {date as _date} from "quasar";

export const required = v => !!v;
export const minLength = (v, length = 10) =>  !v || v?.length >= length;
export const maxLength = (v, length = 10) =>  !v || v?.length <= length;
export const length = (v, length = 10) =>  !v || v?.length === length;
export const lengthIn = (v, lengthList = []) =>  !v || lengthList.includes(v?.length);

export const email = v => !v || EMAIL_REGEX.test(v);
export const alpha = v => !v || ALPHA_REGEX.test(v);
export const numeric = v => !v || NUMERIC_REGEX.test(v);
export const alphaNum = v => !v || ALPHA_NUM_REGEX.test(v);
