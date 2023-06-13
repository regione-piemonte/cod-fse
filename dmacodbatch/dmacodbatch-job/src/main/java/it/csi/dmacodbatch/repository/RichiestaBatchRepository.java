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
public class RichiestaBatchRepository {
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	Logger logger = LoggerFactory.getLogger(RichiestaBatchRepository.class);
	private static final String INSERT_SEMAFORO_RICHIESTA_BATCH = 
			"INSERT INTO cod_t_richiesta_batch "
			+ "( "
			+ "  batch_id, "
			+ "  validita_inizio, "
			+ "  data_creazione, "
			+ "  utente_creazione, "
			+ " batchric_richiedente"
			+ ") values (:batchId, now(), now(),:utente,:utente)"
			+ "returning batchric_id;";


	private static final String CHIUDI_ESECUZIONE=
			"update COD_T_RICHIESTA_BATCH set validita_fine=NOW() where batchric_id=:batchRequestid";
	
	public Integer insertRichiestaBatch(Integer batchId, String utente)  {
		logger.info("insertRichiestaBatch Begin");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchId", batchId)
				.addValue("utente", utente)
				;
		jdbcTemplate.update(INSERT_SEMAFORO_RICHIESTA_BATCH, namedParameters, keyHolder, new String[] { "batchric_id" });
		logger.info("insertRichiestaBatch End");
		return (keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1);

	}

	public void chiudiEsecuzione(Integer batchRequestid) {
		logger.info("chiudiEsecuzione Begin");
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchRequestid", batchRequestid)
				;
		jdbcTemplate.update(CHIUDI_ESECUZIONE, namedParameters);
		logger.info("chiudiEsecuzione END");
		
	}
}
