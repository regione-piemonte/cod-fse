/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.mapper;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import it.csi.dmacodbatch.notifica.core.dto.ModelAssistito;

public class AssistitiAbilitatiMapper {
    
    public ModelAssistito mapOBJ(Map<String, Object> mapObj,String statoAbilitazione) throws SQLException {

		var response = new ModelAssistito();

		response.setNome((String)mapObj.get("soggetto_nome"));
		response.setCognome((String)mapObj.get("soggetto_cognome"));
		response.setCodiceFiscale((String)mapObj.get("soggetto_cf"));
		java.sql.Date bornDateS = ((java.sql.Date) mapObj.get("soggetto_data_di_nascita"));
		response.setDataNascita(bornDateS != null  ? new Date(bornDateS.getTime()): null);
		response.setSesso((String)mapObj.get("soggetto_sesso"));
		response.setStatoAbilitazione(statoAbilitazione);


		return response;
}

}
