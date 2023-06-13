/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

@Repository
public class BatchStatoRepository {
	private static final String SELECT_STATO = "select batchesecstato_id from cod_d_batch_stato where batchesecstato_cod=:stato_batch";
	Logger logger = LoggerFactory.getLogger(BatchStatoRepository.class);
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<Integer> cercaStatoBatchId(String statoBatch) {
		logger.info("cercaStatoBatchId Begin");

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("stato_batch", statoBatch);
		List<Integer> resFromCall = jdbcTemplate.query(SELECT_STATO, namedParameters, (rs, rowNum) -> rs.getInt("batchesecstato_id"));

		logger.info("cercaStatoBatchId End");
		return resFromCall;
	}
}
