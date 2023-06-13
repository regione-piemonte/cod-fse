/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.ModelAutore;
import it.csi.dma.apicodopsan.dto.ModelAutore.TipoEnum;
import it.csi.dma.apicodopsan.dto.ModelConversazione;
import it.csi.dma.apicodopsan.dto.ModelMedico;
import it.csi.dma.apicodopsan.dto.custom.SConversazione;

public class SConversazioneMapper implements RowMapper<SConversazione> {

	@Override
    public SConversazione mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new SConversazione();		

		result.setsConversazioneId(resultSet.getInt("s_conversazione_id"));  
		result.setConversazioneId(resultSet.getInt("conversazione_id"));
		result.setConversazioneCod(resultSet.getString("conversazione_cod"));		
		result.setConversazioneOggetto(resultSet.getString("conversazione_oggetto"));
		result.setSoggettoIdAutore(resultSet.getInt("soggetto_id_autore"));
		result.setSoggettoIdPartecipante(resultSet.getInt("soggetto_id_partecipante"));
		result.setConversazioneDataBlocco(resultSet.getTimestamp("conversazione_data_blocco"));
		result.setDisabilitazioneMotivoId(resultSet.getInt("disabilitazione_motivo_id"));
		
		result.setDataCreazione(resultSet.getTimestamp("data_creazione"));
		result.setDataModifica(resultSet.getTimestamp("data_modifica"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));

		result.setValiditaInizio(resultSet.getTimestamp("validita_inizio"));
		result.setValiditaFine(resultSet.getTimestamp("validita_fine"));
		
		return result;
	}
	
	
	public ModelConversazione mapObj(Map<String, Object> obj) throws SQLException {
		
		var res = new ModelConversazione();
		
		res.setId((String)obj.get("conversazione_cod"));
		
		var med = new ModelMedico();
		med.setNome((String)obj.get("soggetto_nome"));
		med.setCognome((String)obj.get("soggetto_cognome"));
		med.setCodiceFiscale((String)obj.get("soggetto_cf"));
		res.setMedico(med);
//		byte[] conversazioneOggetto = (byte[])obj.get("conversazione_oggetto");
		res.setArgomento((String) obj.get("conversazione_oggetto_dec"));	
		
		res.setSolaLettura((java.sql.Timestamp)obj.get("conversazione_data_blocco") != null);
		
		java.sql.Timestamp creationDate = ((java.sql.Timestamp)obj.get("data_creazione"));
//		res.setDataCreazione(creationDate != null  ? creationDate.toString(): null);
		//05/09/2022 conversione in data 
		res.setDataCreazione(creationDate != null  ? creationDate: null);
		
		var autore = new ModelAutore();
		autore.setCodiceFiscale((String)obj.get("utente_creazione"));
		autore.setTipo(obj.get("soggetto_cf") == obj.get("utente_creazione") && obj.get("soggetto_id") == obj.get("soggetto_id_autore") ? TipoEnum.ASSISTITO : TipoEnum.DELEGATO);
		res.setAutore(autore);
		return res;
	}
}
