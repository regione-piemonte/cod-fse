/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.ModelMotivoBlocco;

public class DDisabilitazioneMotivoMapper implements RowMapper<ModelMotivoBlocco> {

	@Override
    public ModelMotivoBlocco mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new ModelMotivoBlocco();
		result.setCodice(resultSet.getString("disabilitazione_motivo_cod"));
		result.setDescrizione(resultSet.getString("disabilitazione_motivo_desc"));
		
		return result;
	}

}
