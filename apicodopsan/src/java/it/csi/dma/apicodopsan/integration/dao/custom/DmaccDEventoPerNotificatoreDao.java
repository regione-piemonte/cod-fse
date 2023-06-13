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

import it.csi.dma.apicodopsan.dto.custom.DmaccDEventoPerNotificatore;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.utils.DmaccDEventoPerNotificatoreMapper;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class DmaccDEventoPerNotificatoreDao extends LoggerUtil {

	private static final String SELECT_EVENTO_FROM_CODICE = "select evento.* from "
			+ "dmacc_d_evento_per_notificatore evento"
			+ " where evento.codice_evento = :codiceEvento ;";
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public DmaccDEventoPerNotificatore selectDmaccDEventoPerNotificatoreByCodiceEvento(String codiceEvento) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codiceEvento", codiceEvento);
		try {
			return jdbcTemplate.queryForObject(SELECT_EVENTO_FROM_CODICE, namedParameters, new DmaccDEventoPerNotificatoreMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectDmaccDEventoPerNotificatoreByCodiceEvento";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}
}
