/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.dto.custom.CodDErrore;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.CodDErroreIdAndDescMapper;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodDErroreDao extends LoggerUtil {

	public static final String SELECT_ERRORE_DESC = "SELECT errore_desc, errore_id FROM cod_d_errore where errore_cod= :erroreCod and now() BETWEEN validita_inizio and COALESCE(validita_fine, now()); ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public CodDErrore selectErroreDescFromErroreCod(String erroreCod) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("erroreCod", erroreCod);
		
		try {
			return jdbcTemplate.queryForObject(SELECT_ERRORE_DESC, namedParameters,
					new CodDErroreIdAndDescMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	private static final String SQL_INSERT = "INSERT INTO cod_d_errore (errore_cod, errore_desc, validita_inizio, validita_fine, data_creazione, data_modifica, utente_creazione, utente_modifica) VALUES"
			+ "(:errore_cod, :errore_desc, NOW(), NULL, NOW(), NOW(), :utente_creazione, :utente_modifica);";

	public int insert(CodDErrore codDErrore) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		var params = new MapSqlParameterSource();
		params.addValue("errore_cod", codDErrore.getErroreCod(), Types.VARCHAR);
		params.addValue("errore_desc", codDErrore.getErroreDesc() , Types.VARCHAR); 
		params.addValue("utente_creazione", codDErrore.getUtenteCreazione() , Types.VARCHAR);  
		params.addValue("utente_modifica", codDErrore.getUtenteModifica() , Types.VARCHAR);  

		jdbcTemplate.update(SQL_INSERT, params, keyHolder, new String[] { "errore_id" });
		return keyHolder.getKey().intValue();
	}

}
