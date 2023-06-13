/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import java.sql.Types;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSourceUtils;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.dto.custom.MessaggioErrore;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodRMessaggioErroreDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SQL_INSERT_R_MESSAGGIO_ERRORE = "INSERT INTO cod_r_messaggio_errore (messaggio_id, errore_id, informazioni_aggiuntive, data_creazione, data_modifica) "
			+ "VALUES(:messaggioId , :erroreId, :informazioniAggiuntive, NOW(), NOW())";

	public long insert(MessaggioErrore messaggioErrore) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("messaggioId", messaggioErrore.getMessaggioId(), Types.BIGINT);
		params.addValue("erroreId", messaggioErrore.getErroreId(), Types.INTEGER);
		params.addValue("informazioniAggiuntive", messaggioErrore.getInformazioniAggiuntive(), Types.VARCHAR);

		jdbcTemplate.update(SQL_INSERT_R_MESSAGGIO_ERRORE, params, keyHolder, new String[] { "messaggio_errore_id" });
		return keyHolder.getKey().longValue();
	}
	
	public int[] insert(List<MessaggioErrore> messaggioErroreList) {
		
		SqlParameterSource[] parameters = SqlParameterSourceUtils.createBatch(messaggioErroreList.toArray());	
		return jdbcTemplate.batchUpdate(SQL_INSERT_R_MESSAGGIO_ERRORE ,parameters);
	}


}
