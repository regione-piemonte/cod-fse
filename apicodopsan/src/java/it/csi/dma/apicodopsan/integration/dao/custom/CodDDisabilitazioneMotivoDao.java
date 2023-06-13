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

import it.csi.dma.apicodopsan.dto.ModelMotivoBlocco;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.utils.DDisabilitazioneMotivoMapper;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodDDisabilitazioneMotivoDao extends LoggerUtil {

	private static final String SELECT_MOTIVO_DISABILITAZIONE = 	"SELECT														" +
																	"	DISABILITAZIONE_MOTIVO_COD, DISABILITAZIONE_MOTIVO_DESC 	" +
																	"FROM														" +
																	"	COD_D_DISABILITAZIONE_MOTIVO 							" +
																	"WHERE														" +
																	 "	DISABILITAZIONE_MOTIVO_ID = :convMotDisId				";


	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public ModelMotivoBlocco selectMotivoDisabilitazione(int convMotDisId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("convMotDisId", convMotDisId);
		try {
			return jdbcTemplate.queryForObject(SELECT_MOTIVO_DISABILITAZIONE, namedParameters, new DDisabilitazioneMotivoMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectMotivoDisabilitazione";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
}
