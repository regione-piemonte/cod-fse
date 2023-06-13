/*
 * Copyright Regione Piemonte - 2023
 * SPDX-License-Identifier: EUPL-1.2
 */

import {GENDER_OPTIONS} from "src/services/config";
export const DEFAULT_SCROLL_DURATION = 500;

import {getScrollTarget, setScrollPosition} from "quasar/src/utils/scroll";

export const getGenderLabel = (code)=>{
  return GENDER_OPTIONS.find(g => g.value === code)?.label
}

export const  getPatientSelectionMessage = (nPatients, limit) =>{
  let msg = ''
  if(nPatients >0){
    if(nPatients === limit){
      msg = `Tutti gli assistiti possibili in questa pagina sono stati selezionati: ${nPatients} `
    }else if(nPatients===1){
      msg = `${nPatients} assistito selezionato `
    }else{
      msg = `${nPatients} assistiti selezionati `
    }
  }
  return msg
}

export const scrollToElement = (element, duration = DEFAULT_SCROLL_DURATION) => {
  let target = getScrollTarget(element);
  let offset = element.offsetTop;
  setScrollPosition(target, offset, duration)
};

export const searchMessageRegex = (str) =>{
  let escapeRegEx = str.replace(/[-[/\]{}()*+?.,\\^$|#\s]/g, '\\$&');
  return new RegExp('\\b(' + escapeRegEx + ')\\w*', 'gi')

}


export const convertToBlobUrl = (binary, type) =>{
  const blob = new Blob([binary], {type: "application/pdf"});
  return URL.createObjectURL(blob);
}

