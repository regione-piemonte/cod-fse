/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.custom.SMessaggio;

public class SMessaggioMapper implements RowMapper<SMessaggio> {

	@Override
    public SMessaggio mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new SMessaggio();
		
		result.setsMessaggioId(resultSet.getInt("s_messaggio_id"));
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
		result.setValiditaInizio(resultSet.getTimestamp("validita_inizio"));
		result.setValiditaFine(resultSet.getTimestamp("validita_fine"));
		return result;
	}
}
