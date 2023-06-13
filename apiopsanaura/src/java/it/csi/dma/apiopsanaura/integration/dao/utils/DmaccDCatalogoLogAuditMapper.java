/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apiopsanaura.dto.custom.DmaccDCatalogoLogAudit;

public class DmaccDCatalogoLogAuditMapper implements RowMapper<DmaccDCatalogoLogAudit> {

	@Override
    public DmaccDCatalogoLogAudit mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new DmaccDCatalogoLogAudit();
		
		result.setId(resultSet.getLong("id"));	
		result.setCodice(resultSet.getString("codice"));
		result.setDescrizione(resultSet.getString("descrizione"));
		result.setDataInserimento(resultSet.getTimestamp("data_inserimento"));
		result.setDescrizioneCodice(resultSet.getString("descrizione_codice"));
		result.setVisualizzaWebapp(resultSet.getString("visualizza_webapp"));
		result.setFlagNotificaCit(resultSet.getString("flag_notifica_cit"));
		result.setDescrizionebck(resultSet.getString("descrizione_bck"));
		return result;
	}

}
