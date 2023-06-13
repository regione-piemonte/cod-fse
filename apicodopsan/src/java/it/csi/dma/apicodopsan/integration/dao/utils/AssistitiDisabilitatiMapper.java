/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import it.csi.dma.apicodopsan.dto.ModelAssistito;
import it.csi.dma.apicodopsan.dto.ModelMotivoBlocco;

public class AssistitiDisabilitatiMapper {

	public ModelAssistito mapOBJ(Map<String, Object> mapObj,String stato) throws SQLException {

			var response = new ModelAssistito();

			response.setNome((String)mapObj.get("soggetto_nome"));
    		response.setCognome((String)mapObj.get("soggetto_cognome"));
    		response.setCodiceFiscale((String)mapObj.get("soggetto_cf"));
    		java.sql.Date bornDateS = ((java.sql.Date) mapObj.get("soggetto_data_di_nascita"));
			response.setDataNascita(bornDateS != null  ? new Date(bornDateS.getTime()): null);
    		response.setSesso((String)mapObj.get("soggetto_sesso"));
			response.setStatoAbilitazione(stato);
			response.setMotivazioneMedico((String)mapObj.get("disabilitazione_motivazione"));
			ModelMotivoBlocco mb = new ModelMotivoBlocco();
			
			mb.setCodice((String)mapObj.get("disabilitazione_motivo_cod"));
			mb.setDescrizione((String)mapObj.get("disabilitazione_motivo_desc"));
		
			response.setMotivoBlocco(mb);


		return response;
	}
}
