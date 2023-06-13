/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import it.csi.dma.apiopsanaura.dto.custom.MessaggioErrore;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.util.LoggerUtil;


@Repository
public class CodRMessaggioErroreDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SQL_INSERT_R_MESSAGGIO_ERRORE = "INSERT INTO cod_r_messaggio_errore (messaggio_id, errore_id, informazioni_aggiuntive, data_creazione, data_modifica) "
			+ "VALUES(:messaggioId, :erroreId, :informazioniAggiuntive, NOW(), NOW())";

	public void insert(MessaggioErrore messaggioErrore) throws DatabaseException {

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("messaggioId", messaggioErrore.getMessaggioId(), Types.BIGINT);
		params.addValue("erroreId", messaggioErrore.getErroreId(), Types.INTEGER);
		params.addValue("informazioniAggiuntive", messaggioErrore.getInformazioniAggiuntive(), Types.VARCHAR);
		
		try {
		jdbcTemplate.update(SQL_INSERT_R_MESSAGGIO_ERRORE, params);
		} 
		catch (Exception e) {
			var methodName = "insert";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
}
