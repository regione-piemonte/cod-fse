/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.custom.SAdesione;

public class SAdesioneMapper implements RowMapper<SAdesione> {

	@Override
    public SAdesione mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new SAdesione();
		
		result.setsAdesioneId(resultSet.getInt("s_adesione_id"));
		result.setAdesioneId(resultSet.getInt("adesione_id"));
		result.setSoggettoId(resultSet.getInt("soggetto_id"));		
		result.setAdesioneInizio(resultSet.getTimestamp("adesione_inizio"));
		result.setAdesioneFine(resultSet.getTimestamp("adesione_fine"));
		result.setDataCreazione(resultSet.getTimestamp("data_creazione"));
		result.setDataModifica(resultSet.getTimestamp("data_modifica"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));
		result.setMostraLetturaMessaggiAAssistiti(resultSet.getBoolean("mostra_lettura_messaggi_a_assistiti"));
		result.setValiditaInizio(resultSet.getTimestamp("validita_inizio"));
		result.setValiditaFine(resultSet.getTimestamp("validita_validita_fine"));
		
		return result;
	}

}
