/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodDBatchDao extends LoggerUtil {

     							

	public static final String SELECT_BATCH_ID = "SELECT batch_id FROM cod_d_batch WHERE batch_cod= :batchCod AND now() BETWEEN validita_inizio and COALESCE(validita_fine, now())";
	
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	
	public Integer selectBatchIdFromBatchCode(String batchCod) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("batchCod", batchCod);
		try {
			return jdbcTemplate.queryForObject(SELECT_BATCH_ID, namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectBatchIdFromBatchCode";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	
}
