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

import it.csi.dma.apiopsanaura.dto.custom.Ruolo;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.integration.dao.utils.RuoloMapper;
import it.csi.dma.apiopsanaura.util.LoggerUtil;

@Repository
public class DmaccTDecodificaRuoliPuaDao extends LoggerUtil {

	public static final String SELECT_RUOLO = "SELECT " 
											+ "* " 
											+ "FROM " 
											+ "	DMACC_T_DECODIFICA_RUOLI_PUA DTDRP "
											+ "WHERE " 
											+ "DTDRP.SOL = :sol " 
											+ "AND DTDRP.CODICE_RUOLO_PUA = :ruolo ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public Ruolo selectRuolo(String ruolo, String xCodiceServizio) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("ruolo", ruolo).addValue("sol",
				xCodiceServizio);

		try {
			Ruolo selected = jdbcTemplate.queryForObject(SELECT_RUOLO, namedParameters, new RuoloMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}
