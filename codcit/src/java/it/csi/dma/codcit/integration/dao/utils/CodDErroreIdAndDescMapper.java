/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.CodDErrore;

public class CodDErroreIdAndDescMapper extends CodDErroreMapper implements RowMapper<CodDErrore>  {

	@Override
	protected void setSpecificElements(ResultSet resultSet, CodDErrore result) throws SQLException {
		//rimuoverespecifici elementi da non mappare
	}

}
