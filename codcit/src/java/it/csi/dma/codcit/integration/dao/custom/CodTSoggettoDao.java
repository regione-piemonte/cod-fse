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

import it.csi.dma.codcit.dto.custom.Soggetto;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.SoggettoMapper;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodTSoggettoDao extends LoggerUtil {

private static final String SELECT_SOGGETTO_MEDICO =					"SELECT 								" +
																"CTS.* 									" +
																"FROM 									" +
																"	COD_T_SOGGETTO CTS 					" +
																"WHERE 				 					" +
																"	CTS.SOGGETTO_CF = :soggettoCf 		" +
																"	AND CTS.SOGGETTO_IS_MEDICO = TRUE 	" ;
private static final String SELECT_SOGGETTO_QUALUNQUE =	"SELECT 								" +
														"CTS.* 									" +
														"FROM 									" +
														"	COD_T_SOGGETTO CTS 					" +
														"WHERE 				 					" +
														"	CTS.SOGGETTO_CF = :soggettoCf 		" ;


private static final String SELECT_SOGGETTO_BY_ID =				"SELECT 								" +
																"CTS.* 									" +
																"FROM 									" +
																"	COD_T_SOGGETTO CTS 					" +
																"WHERE 				 					" +
																"	CTS.SOGGETTO_ID = :soggettoId ; 		" ;



	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public Soggetto selectSoggetto(String cfSoggetto) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoCf", cfSoggetto);
		try {
			Soggetto selected = jdbcTemplate.queryForObject(SELECT_SOGGETTO_MEDICO, namedParameters, new SoggettoMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggetto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	public Soggetto selectSoggettoByCF(String cfSoggetto) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoCf", cfSoggetto);
		try {
			Soggetto selected = jdbcTemplate.queryForObject(SELECT_SOGGETTO_QUALUNQUE, namedParameters, new SoggettoMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggetto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Soggetto selectSoggettoById(Integer soggettoId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoId", soggettoId);
		try {
			Soggetto selected = jdbcTemplate.queryForObject(SELECT_SOGGETTO_BY_ID, namedParameters, new SoggettoMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggettoById";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	

	
	
}
