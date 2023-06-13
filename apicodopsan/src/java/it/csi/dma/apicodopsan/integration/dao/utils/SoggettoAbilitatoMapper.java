/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.custom.SoggettoAbilitato;

public class SoggettoAbilitatoMapper implements RowMapper<SoggettoAbilitato> {

	@Override
    public SoggettoAbilitato mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new SoggettoAbilitato();
		result.setAbilitazioneId(resultSet.getInt("ABILITAZIONE_ID"));
		result.setAbilitazioneId(resultSet.getInt("soggetto_id_abilitato"));
		result.setAbilitazioneId(resultSet.getInt("soggetto_id_abilitante"));
		result.setAbilitazioneInizio(resultSet.getDate("abilitazione_inizio"));
		result.setDataCreazione(resultSet.getDate("data_creazione"));
		result.setDataModifica(resultSet.getDate("data_modifica"));
		result.setUtenteCreazione(resultSet.getString("utente_creazione"));
		result.setUtenteModifica(resultSet.getString("utente_modifica"));
		return result;
	}

}
