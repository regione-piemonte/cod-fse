/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.apicodopsan.dto.custom.CountSoggetti;

public class CountSoggettiMapper implements RowMapper<CountSoggetti> {

	@Override
	public CountSoggetti mapRow(ResultSet rs, int arg1) throws SQLException {
		var result = new CountSoggetti();
		result.setSoggettiAbilitati(rs.getInt("abilitati"));
		result.setSoggettiDisabilitati(rs.getInt("disabilitati"));
		return result;
	}

}
