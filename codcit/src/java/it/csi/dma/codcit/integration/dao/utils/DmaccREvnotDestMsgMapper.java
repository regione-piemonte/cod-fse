/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.DmaccREvnotDestMsg;

public class DmaccREvnotDestMsgMapper implements RowMapper<DmaccREvnotDestMsg> {

	@Override
    public DmaccREvnotDestMsg mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new DmaccREvnotDestMsg();
		result.setId(resultSet.getLong("id"));
		result.setIdEvento(resultSet.getLong("id_evento"));
		result.setIdTipoDestinatario(resultSet.getLong("id_tipo_destinatario"));
		result.setMsg_mail(resultSet.getString("msg_mail"));
		result.setMsg_sms(resultSet.getString("msg_sms"));
		result.setMsg_push(resultSet.getString("msg_push"));
		result.setMsg_mex(resultSet.getString("msg_mex"));
		result.setDataInserimento(resultSet.getTimestamp("data_inserimento"));
		result.setDataAggiornamento(resultSet.getTimestamp("data_aggiornamento"));
		return result;
	}
}
