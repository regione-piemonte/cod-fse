/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodCParametroDao extends LoggerUtil {

	private static final String SELECT_PARAMETRO_VALORE = "select parametro_valore from "
			+ "	cod_c_parametro ccp where 	ccp.parametro_nome = :parametroNome "
			+ "	and now() between validita_inizio and coalesce(validita_fine, now());";
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public String selectValoreParametroFromParametroNome(String parametroNome) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("parametroNome", parametroNome);
		try {
			return jdbcTemplate.queryForObject(SELECT_PARAMETRO_VALORE, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectValoreParametroFromParametroNome";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

}
