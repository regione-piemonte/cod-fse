/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.CodTMessaggio;

public class CodTMessaggioRowMapper implements RowMapper<CodTMessaggio> {

	@Override
	public CodTMessaggio mapRow(ResultSet rs, int rowNumber) throws SQLException {
		var result = new CodTMessaggio();
		result.setMessaggioId(rs.getLong("messaggio_id"));
		result.setMessaggioTestoCifrato(rs.getString("messaggio_testo_cifrato"));
		result.setSoggettoIdDa(rs.getInt("soggetto_id_da"));
		result.setSoggettoIdA(rs.getInt("soggetto_id_a"));
		result.setMessaggioDataInvio(rs.getTimestamp("messaggio_data_invio"));
		result.setMessaggioLetturaData(rs.getTimestamp("messaggio_lettura_data"));
		result.setMessaggioLetturaDaCf(rs.getString("messaggio_lettura_da_cf"));
		result.setConversazioneId(rs.getLong("conversazione_id"));
		result.setDataCreazione(rs.getTimestamp("data_creazione"));
		result.setDataModifica(rs.getTimestamp("data_modifica"));
		result.setUtenteCreazione(rs.getString("utente_creazione"));
		result.setUtenteModifica(rs.getString("utente_modifica"));
		
		
		return result;
	}

}
