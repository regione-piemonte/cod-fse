/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dmacodbatch.notifica.core.dto.custom.DmaccREvnotDestMsg;
import it.csi.dmacodbatch.notifica.core.mapper.DmaccREvnotDestMsgMapper;
import it.csi.dmacodbatch.notifica.core.util.DatabaseException;
import it.csi.dmacodbatch.notifica.core.util.LoggerUtil;

@Repository
public class DmaccREvnotDestMsgDao extends LoggerUtil {

	private static final String SELECT_EVENTO_FROM_CODICE = "select dmare.* "
			+ " from dmacc_r_evnot_dest_msg dmare join dmacc_d_evento_per_notificatore ddepn on ddepn.id =dmare.id_evento "
			+ " where ddepn.codice_evento =:codiceEvento";
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public DmaccREvnotDestMsg selectDmaccREvnotDestMsgDaoByCodiceEvento(String codiceEvento) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("codiceEvento", codiceEvento);
		try {
			return jdbcTemplate.queryForObject(SELECT_EVENTO_FROM_CODICE, namedParameters, new DmaccREvnotDestMsgMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectDmaccREvnotDestMsgDaoByCodiceEvento";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}
}
