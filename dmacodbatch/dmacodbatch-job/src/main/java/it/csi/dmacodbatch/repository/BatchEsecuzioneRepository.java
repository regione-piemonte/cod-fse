/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.repository;

import java.util.Date;
import java.util.List;

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
public class BatchEsecuzioneRepository {
	private static final String SELECT_ULTIMA_ESECUZIONE = 
			"select max(batchesec_fine) as max_data from cod_l_batch_esecuzione "
			+ "where BATCH_ID = :batch_id "
			+ "and BATCHESECSTATO_ID=:stato_id";
	
	private static final String INSERT_NUOVA_ESECUZIONE=
			"insert into cod_l_batch_esecuzione ( "
					+ "batch_id, "
					+ "batchric_id, "
					+ "batchesec_inizio, "
					+ "data_creazione, "
					+ "utente_creazione, "
					+ "batchesecstato_id"
					+ ") values (:batch_id, :batch_rich_id, now(), now(), :utente, :statoId)"
					+ "returning batchesec_id;";
	
	private static final String CHIUDI_ESECUZIONE=
			"update cod_l_batch_esecuzione set BATCHESEC_FINE=NOW(), batchesecstato_id=:statoId where batchesec_id=:batchEsecId";
	
	Logger logger = LoggerFactory.getLogger(BatchEsecuzioneRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<Date> cercaUltimaEsecuzione(Integer batchId, Integer statoId) {
		logger.info("cercaUltimaEsecuzione Begin");
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batch_id", batchId)
				.addValue("stato_id",statoId);
		List<Date> resFromCall = jdbcTemplate.query(SELECT_ULTIMA_ESECUZIONE, namedParameters,
				(rs, rowNum) -> {
					java.sql.Timestamp date = rs.getTimestamp("max_data");
					if(date!=null) {
						return new Date(date.getTime());
					}else {
						return null;
					}
				});

		logger.info("cercaUltimaEsecuzione End");
		return resFromCall;
	}

	public Integer inserisciNuovaEsecuzione(Integer batchId, Integer richiestaId,String utente,Integer statoId) {
		logger.info("inserisciNuovaEsecuzione Begin");
		KeyHolder keyHolder = new GeneratedKeyHolder();
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batch_id", batchId)
				.addValue("batch_rich_id", richiestaId)
				.addValue("statoId", statoId)
				.addValue("utente", utente)
				;
		jdbcTemplate.update(INSERT_NUOVA_ESECUZIONE, namedParameters, keyHolder, new String[] { "batchric_id" });
		logger.info("inserisciNuovaEsecuzione END");
		return (keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1);
	}
	public void chiudiEsecuzione(Integer batchEsecId, Integer statoId) {
		logger.info("chiudiEsecuzione Begin");
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchEsecId", batchEsecId)
				.addValue("statoId", statoId)
				;
		jdbcTemplate.update(CHIUDI_ESECUZIONE, namedParameters);
		logger.info("chiudiEsecuzione END");
	}
}
