/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.repository;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dmacodbatch.entity.ModelAssistito;
import it.csi.dmacodbatch.entity.RispostaFunzione;
import it.csi.dmacodbatch.util.Constants;

@Repository
public class FunctionCallRepository {

	Logger logger = LoggerFactory.getLogger(FunctionCallRepository.class);

	private static final String CALL_FUNCTION_ELIMINA_CONVERSAZIONI = "SELECT * FROM fnc_cod_elimina_conversazioni_messaggi()";
	private static final String CALL_FUNCTION_DISABILITA = "SELECT * FROM fnc_cod_disabilita(:nome_batch, :in_batchparam, :in_elenco_cf, :in_cf_medico, :in_motivazione);";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<RispostaFunzione> eliminaConversazioniMessaggi() {

		logger.info("eliminaConversazioniMessaggi Begin");

		SqlParameterSource namedParameters = new MapSqlParameterSource();
		// .addValue("in_cf_medico",in_cf_medico)
		// .addValue("in_elenco_cf",
		// buildAssistitiString(payloadAbilitazione.getAssistiti()));
		List<RispostaFunzione> resFromCall = jdbcTemplate.query(CALL_FUNCTION_ELIMINA_CONVERSAZIONI, namedParameters,
				(rs, rowNum) -> new RispostaFunzione(rs.getInt("out_esito_cod"), rs.getString("out_esito_desc")));

		logger.info("eliminaConversazioniMessaggi End");
		return resFromCall;

	}

	
	public List<ModelAssistito> callDisabilita(String in_cf_medico){
		logger.info("callDisabilita begin");
		List<ModelAssistito> resFromCall = new ArrayList<ModelAssistito>();
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("nome_batch",Constants.CODICE_BATCH_DISABILITA_ASSISTITI)
				.addValue("in_batchparam",Constants.BATCH_PARAM)
				.addValue("in_elenco_cf", in_cf_medico)
				.addValue("in_cf_medico",Constants.CODICE_BATCH_DISABILITA_ASSISTITI)
				.addValue("in_motivazione",  null);
		resFromCall=jdbcTemplate.query(CALL_FUNCTION_DISABILITA, namedParameters,(rs, rowNum) -> new ModelAssistito(rs.getString("cf"), 
				rs.getString("stato")));
		logger.info("callDisabilita end");
		return resFromCall;

	}
}
