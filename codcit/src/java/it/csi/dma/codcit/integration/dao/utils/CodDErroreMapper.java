/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.CodDErrore;

public class CodDErroreMapper implements RowMapper<CodDErrore> {

	@Override
    public CodDErrore mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new CodDErrore();
		result.setErroreId(resultSet.getInt("errore_id"));
		result.setErroreDesc(resultSet.getString("errore_desc"));
		
		setSpecificElements(resultSet, result);
		return result;
	}

	protected void setSpecificElements(ResultSet resultSet, CodDErrore result) throws SQLException {
		result.setErroreCod(resultSet.getString("errore_cod"));
		result.setValiditaInizio(resultSet.getTimestamp("validita_inizio"));
		result.setValiditaFine(resultSet.getTimestamp("validita_fine"));
		result.setDataCreazione(resultSet.getTimestamp("data_creazione"));
		result.setDataModifica(resultSet.getTimestamp("data_modifica"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));
	}

}
