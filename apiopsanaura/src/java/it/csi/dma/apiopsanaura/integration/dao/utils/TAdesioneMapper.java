/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apiopsanaura.dto.custom.TAdesione;

public class TAdesioneMapper implements RowMapper<TAdesione> {

	@Override
    public TAdesione mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new TAdesione();
		System.out.println(resultSet.getTimestamp("adesione_inizio"));
		result.setAdesioneId(resultSet.getInt("adesione_id"));
		result.setSoggettoId(resultSet.getInt("soggetto_id"));		
		result.setAdesioneInizio(resultSet.getTimestamp("adesione_inizio"));
		result.setAdesioneFine(resultSet.getTimestamp("adesione_fine"));
		result.setDataCreazione(resultSet.getTimestamp("data_creazione"));
		result.setDataModifica(resultSet.getTimestamp("data_modifica"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));
		result.setMostraLetturaMessaggiAAssistiti(resultSet.getBoolean("mostra_lettura_messaggi_a_assistiti"));
		return result;
	}

}
