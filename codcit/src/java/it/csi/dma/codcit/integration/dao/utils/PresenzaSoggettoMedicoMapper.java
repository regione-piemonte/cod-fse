/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.PresenzaSoggettoMedico;

public class PresenzaSoggettoMedicoMapper implements RowMapper<PresenzaSoggettoMedico> {

	@Override
	public PresenzaSoggettoMedico mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
		var result = new PresenzaSoggettoMedico();
		result.setSoggetto(resultSet.getInt("countCittadino"));
		result.setMedico(resultSet.getInt("countMedico"));
		
		return result;
	}

}
