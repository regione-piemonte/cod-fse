/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.Soggetto;

public class SoggettoMapper implements RowMapper<Soggetto> {

	@Override
    public Soggetto mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new Soggetto();
		
		result.setSoggettoId(resultSet.getInt("soggetto_id"));	
		result.setSoggettoCf(resultSet.getString("soggetto_cf"));
		result.setSoggettoNome(resultSet.getString("soggetto_nome"));
		result.setSoggettoCognome(resultSet.getString("soggetto_cognome"));
		result.setSoggettoDataDiNascita(resultSet.getTimestamp("soggetto_data_di_nascita"));
		result.setSoggettoSesso(resultSet.getString("soggetto_sesso"));
		result.setIdPaziente(resultSet.getLong("id_paziente"));
		result.setDataCreazione(resultSet.getTimestamp("data_creazione"));
		result.setDataModifca(resultSet.getTimestamp("data_modifca"));
		result.setSoggettoIsMedico(resultSet.getBoolean("soggetto_is_medico"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));
		
		return result;
	}

}
