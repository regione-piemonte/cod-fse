/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.custom.TConversazione;

public class TConversazioneMapper implements RowMapper<TConversazione> {

	@Override
    public TConversazione mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new TConversazione();		

		result.setConversazioneId(resultSet.getLong("conversazione_id"));
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
		
		return result;
	}

}
