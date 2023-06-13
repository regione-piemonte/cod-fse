/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.dto.custom.LMessaggi;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodLMessaggiDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String SQL_INSERT_T_LOG_CHIAMATA = "INSERT INTO cod_l_messaggi "
			+ " (x_request_id, cf_chiamante, x_codice_servizio, ip_chiamante, cf_richiedente, componente,  request_payload,  "
			+ " request_date,  metodo, request_uri, data_creazione,  soggetto_cf) "
			+ " VALUES(:x_request_id, :cf_chiamante, :x_codice_servizio, :ip_chiamante, :cf_richiedente, :componente, "
			+ "   pgp_sym_encrypt_bytea(:request_payload::bytea, :encryption_key ), now(), :metodo, :request_uri, now(),  :soggetto_cf )";
	private static final String SQL_UPDATE_T_LOG_CHIAMATA = "UPDATE cod_l_messaggi set response_payload = pgp_sym_encrypt_bytea(:response_payload::bytea, :encryption_key ), "
			+ "response_date = now(), esito_chiamata = :esito_chiamata,  ms_response = :ms_response "
			+ "where messaggio_id = :messaggio_id";

	public long insertMessaggi(LMessaggi lMessaggi) {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("x_request_id", lMessaggi.getxRequestId(), Types.VARCHAR);
		params.addValue("cf_chiamante", lMessaggi.getCfChiamante(), Types.VARCHAR);
		params.addValue("x_codice_servizio", lMessaggi.getxCodiceServizio(), Types.VARCHAR);
		params.addValue("ip_chiamante", lMessaggi.getIpChiamante(), Types.VARCHAR);
		params.addValue("cf_richiedente", lMessaggi.getCfRichiedente(), Types.VARCHAR);
		params.addValue("componente", lMessaggi.getComponente(), Types.VARCHAR);
		params.addValue("esito_chiamata", lMessaggi.getEsitoChiamata(), Types.INTEGER);

		params.addValue("request_payload", lMessaggi.getRequestPayload(), Types.BINARY);
		/**
		 * TODO Verificare encryption key
		 */
		params.addValue("encryption_key", "mypass", Types.VARCHAR);
		params.addValue("response_payload", lMessaggi.getResponsePayload(), Types.BINARY);

		params.addValue("metodo", lMessaggi.getMetodo(), Types.VARCHAR);
		params.addValue("request_uri", lMessaggi.getRequestUri(), Types.VARCHAR);
		params.addValue("soggetto_cf", lMessaggi.getSoggettoCf(), Types.VARCHAR);

		jdbcTemplate.update(SQL_INSERT_T_LOG_CHIAMATA, params, keyHolder, new String[] { "messaggio_id" });
		return keyHolder.getKey().longValue();
	}

	public int updateMessaggi(LMessaggi lMessaggi, Long id) {

		MapSqlParameterSource params = new MapSqlParameterSource();

		params.addValue("response_payload", lMessaggi.getResponsePayload(), Types.BINARY);
		params.addValue("encryption_key", "mypass", Types.VARCHAR);
		params.addValue("esito_chiamata", lMessaggi.getEsitoChiamata(), Types.INTEGER);
		params.addValue("ms_response", lMessaggi.getMsResponse(), Types.BIGINT);
		params.addValue("messaggio_id", id, Types.BIGINT);

		return jdbcTemplate.update(SQL_UPDATE_T_LOG_CHIAMATA, params);
	}
}
