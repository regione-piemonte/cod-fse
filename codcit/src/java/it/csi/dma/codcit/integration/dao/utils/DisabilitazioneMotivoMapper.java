/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.DisabilitazioneMotivo;

public class DisabilitazioneMotivoMapper implements RowMapper<DisabilitazioneMotivo> {

	@Override
    public DisabilitazioneMotivo mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new DisabilitazioneMotivo();
		result.setDisabilitazioneMotivoCod(resultSet.getString("disabilitazione_motivo_cod"));
		result.setDisabilitazioneMotivoDesc(resultSet.getString("disabilitazione_motivo_desc"));
		setSpecificElements(resultSet, result);
		return result;
	}

	protected void setSpecificElements(ResultSet resultSet, DisabilitazioneMotivo result) throws SQLException {
		result.setDisabilitazioneMotivoId(resultSet.getInt("disabilitazione_motivo_id"));
		result.setValiditaInizio(resultSet.getTimestamp("validita_inizio"));
		result.setValiditaFine(resultSet.getTimestamp("validita_fine"));
		result.setDataCreazione(resultSet.getTimestamp("data_creazione"));
		result.setDataModifica(resultSet.getTimestamp("data_modifica"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));
	}

}
