/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.util.LoggerUtil;

@Repository
public class CodDErroreDao extends LoggerUtil {

	public static final String SELECT_ERRORE_DESC = "SELECT errore_desc FROM cod_d_errore where errore_cod= :erroreCod and now() BETWEEN validita_inizio and COALESCE(validita_fine, now()); ";

	public static final String SELECT_ERROR_ID = "SELECT errore_id FROM cod_d_errore WHERE errore_cod= :erroreCod AND now() BETWEEN validita_inizio and COALESCE(validita_fine, now())";
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public String selectErroreDescFromErroreCod(String erroreCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("erroreCod", erroreCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_ERRORE_DESC, namedParameters, String.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	public Integer selectErrorIdFromErrorCode(String erroreCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("erroreCod", erroreCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_ERROR_ID, namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectErrorIdFromErrorCode";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
}
