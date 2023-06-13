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
import it.csi.dma.apicodopsan.dto.ModelUltimoMessaggio;
import it.csi.dma.apicodopsan.dto.custom.TMessaggio;
import it.csi.dma.apicodopsan.exception.DatabaseException;

public class TMessaggioMapper implements RowMapper<TMessaggio> {

	@Override
    public TMessaggio mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new TMessaggio();
		result.setMessaggioId(resultSet.getInt("messaggio_id"));
		result.setMessaggioTestoCifrato(resultSet.getBytes("messaggio_testo_cifrato"));
		result.setSoggettoIdDa(resultSet.getInt("soggetto_id_da"));
		result.setSoggettoIdA(resultSet.getInt("soggetto_id_a"));
		result.setMessaggioDataInvio(resultSet.getTimestamp("messaggio_data_invio"));
		result.setMessaggioLetturaData(resultSet.getTimestamp("messaggio_lettura_data"));
		result.setMessaggioLetturaDaCf(resultSet.getString("messaggio_lettura_da_cf"));
		result.setConversazioneId(resultSet.getInt("conversazione_id"));
		result.setDataCreazione(resultSet.getTimestamp("data_creazione"));
		result.setDataModifica(resultSet.getTimestamp("data_modifica"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));
		return result;
	}
	
	public ModelUltimoMessaggio mapMess(Map<String, Object> obj, String testoDecifrato, TipoEnum tipo)  throws SQLException, DatabaseException {
		var result = new ModelUltimoMessaggio();
		
		result.setId(String.valueOf((Long)obj.get("messaggio_id")));
		
		java.sql.Timestamp creationDate = ((java.sql.Timestamp)obj.get("data_creazione"));
		result.setDataCreazione(creationDate != null  ? creationDate: null);
		
		result.setLetto(obj.get("messaggio_lettura_data") != null);
		
		java.sql.Timestamp messageReadDate = ((java.sql.Timestamp)obj.get("messaggio_lettura_data"));
		result.setDataLettura(messageReadDate != null  ? messageReadDate: null);
		result.setTesto(testoDecifrato);
		
		ModelAutore autore = new ModelAutore();
		autore.setCodiceFiscale((String)obj.get("utente_creazione"));
		autore.setTipo(tipo);
		
		result.setAutore(autore);
		
		result.setModificato(!obj.get("data_modifica").equals(obj.get("data_creazione")));
		if(result.isModificato()) {
			result.setAutoreModifica((String)obj.get("utente_modifica"));
			java.sql.Timestamp updateDate = ((java.sql.Timestamp)obj.get("data_modifica"));
			result.setDataModifica(updateDate != null  ? updateDate: null);
		}
		return result;
	}
}
