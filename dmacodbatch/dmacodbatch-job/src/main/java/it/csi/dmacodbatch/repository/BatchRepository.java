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
import org.springframework.stereotype.Repository;

@Repository
public class BatchRepository {
	private static final String SELECT_BATCH_ID = "select batch_id from cod_d_batch where batch_cod=:codice_batch";
	Logger logger = LoggerFactory.getLogger(BatchRepository.class);

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	public List<Integer> cercaBatchId(String batchCode) {
		logger.info("cercaBatchId Begin");

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("codice_batch",batchCode);
		List<Integer> resFromCall = jdbcTemplate.query(SELECT_BATCH_ID, namedParameters,
				(rs, rowNum) -> rs.getInt("batch_id"));

		logger.info("cercaBatchId End");
		return resFromCall;
	}
}
