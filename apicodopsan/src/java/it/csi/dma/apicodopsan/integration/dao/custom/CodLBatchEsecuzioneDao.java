/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.dma.apicodopsan.dto.PayloadAbilitazione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodLBatchEsecuzioneDao extends LoggerUtil {

	private static final String INSERT_AFTER_LOCK="INSERT INTO cod_l_batch_esecuzione("
			+ "batchesecstato_id, batch_id, batchparam_id, batchric_id, batchparam_value, batchesec_inizio,  data_creazione,utente_creazione)"
			+ "	VALUES ((SELECT batchesecstato_id FROM cod_d_batch_stato where batchesecstato_cod='IN_ELAB'),"
			+ "	(SELECT batch_id FROM cod_d_batch where batch_cod=:in_codice_batch),"
			+ " (SELECT batchparam_id FROM cod_d_batch_parametro where batchparam_cod=:in_batchparam_cod and batch_id=(SELECT batch_id FROM cod_d_batch where batch_cod=:in_codice_batch limit 1)),"
			+ "	:batchric_id,:in_batchparam_value, now(), now(),:in_codice_batch);";

	private static final String UPDATE_AFTER_LOCK="UPDATE cod_l_batch_esecuzione SET batchesecstato_id=(SELECT batchesecstato_id FROM cod_d_batch_stato where batchesecstato_cod=:batchesecstato_cod), batchesec_fine=now() WHERE batchric_id=:batchric_id;";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;


	public Long insertAfterLock(MapSqlParameterSource namedParameters,long batchric_id) throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		namedParameters.addValue("batchric_id", batchric_id);

		try {
			jdbcTemplate.update(INSERT_AFTER_LOCK, namedParameters, keyHolder, new String[] { "batchric_id" });
			return (keyHolder.getKey().longValue());
		}
		catch (Exception e) {
			var methodName = "insertLockRichiestaBatch";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public void updateAfterLock(long batchric_id, boolean esito) {
		log.debug("Sono in removeLockRichiestaBatchForError");
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("batchric_id",batchric_id).addValue("batchesecstato_cod", esito?"OK":"ERR");
		jdbcTemplate.update(UPDATE_AFTER_LOCK,namedParameters);

	}




}
