/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class BatchEsecuzioneDetRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	Logger logger = LoggerFactory.getLogger(BatchEsecuzioneDetRepository.class);
	
	private static final String INSERT_BATCH_ESECUZIONE_DET=
			"INSERT into cod_l_batch_esecuzione_det "
			+ "(BATCHESEC_ID, BATCHESECDET_STEP, BATCHESECDET_NOTE, BATCHESECSTATO_ID, DATA_CREAZIONE, UTENTE_CREAZIONE) "
			+ "values(:batchEsecId, 'FINE ELABORAZIONE', :note, :stato_id, now(), :utente);";
	
	public void insertRisultatoBatch(Integer batchId, String note, Integer stato, String utente)  {
		logger.info("insertRisultatoBatch Begin");
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchEsecId", batchId)
				.addValue("note", note)
				.addValue("stato_id", stato)
				.addValue("utente", utente)
				;
		jdbcTemplate.update(INSERT_BATCH_ESECUZIONE_DET, namedParameters);
		logger.info("insertRisultatoBatch End");

	}
}
