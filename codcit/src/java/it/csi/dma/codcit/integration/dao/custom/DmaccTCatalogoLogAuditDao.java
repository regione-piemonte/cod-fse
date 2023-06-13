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

import it.csi.dma.codcit.dto.custom.DmaccDCatalogoLogAudit;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.DmaccDCatalogoLogAuditMapper;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class DmaccTCatalogoLogAuditDao extends LoggerUtil {

public static final String SELECT_CATALOGO_DESCRIZIONE ="(select cat.* from dmacc_d_catalogo_log_audit cat where cat.codice=:codiceAudit )";
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public DmaccDCatalogoLogAudit selectCatalogoDescrizioneByCodice(String codiceAudit) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("codiceAudit", codiceAudit);
		try {
			DmaccDCatalogoLogAudit result = jdbcTemplate.queryForObject(SELECT_CATALOGO_DESCRIZIONE, namedParameters, new DmaccDCatalogoLogAuditMapper());
			return result;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectCatalogoDescrizioneByCodice";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

}
