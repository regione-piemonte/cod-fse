/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.custom.DmaccDEventoPerNotificatore;

public class DmaccDEventoPerNotificatoreMapper implements RowMapper<DmaccDEventoPerNotificatore> {

	@Override
    public DmaccDEventoPerNotificatore mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new DmaccDEventoPerNotificatore();
		result.setId(resultSet.getLong("id"));
		result.setCodiceEvento(resultSet.getString("codice_evento"));
		result.setDescrizioneEvento(resultSet.getString("descrizione_evento"));
		result.setMsg_mail(resultSet.getString("msg_mail"));
		result.setMsg_sms(resultSet.getString("msg_sms"));
		result.setMsg_push(resultSet.getString("msg_push"));
		result.setMsg_mex(resultSet.getString("msg_mex"));
		result.setDataInserimento(resultSet.getTimestamp("data_inserimento"));
		result.setDataAggiornamento(resultSet.getTimestamp("data_aggiornamento"));
		return result;
	}
}
