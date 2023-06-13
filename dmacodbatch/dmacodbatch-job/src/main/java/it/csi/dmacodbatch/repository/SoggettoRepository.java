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
public class SoggettoRepository {
	Logger logger = LoggerFactory.getLogger(SoggettoRepository.class);

	private static final String ELENCO_CF_DA_CHIUDERE_QUERY =
			" select array_to_string(array(" + " select a.soggetto_cf" + " from cod_t_soggetto a," + " cod_t_soggetto_abilitato b," + " cod_t_soggetto c"
					+ " where b.soggetto_id_abilitato = a.soggetto_id and" + " c.soggetto_id = b.soggetto_id_abilitante and" + " exists (" + "     select 1"
					+ "     from dmacc_t_paziente z" + "     where z.id_paziente = a.id_paziente and"
					+ "           z.data_aggiornamento > :ultimaEsecuzione and" 
					+ "           (z.codice_fiscale_mmg is null or"
					+ "           z.codice_fiscale_mmg <> c.soggetto_cf)" + " )), ',') as elenco_cf";
	private static final String ELENCO_CF_DA_CHIUDERE_QUERY_FIRST_TIME =
			" select array_to_string(array(" + " select a.soggetto_cf" + " from cod_t_soggetto a," + " cod_t_soggetto_abilitato b," + " cod_t_soggetto c"
					+ " where b.soggetto_id_abilitato = a.soggetto_id and" + " c.soggetto_id = b.soggetto_id_abilitante and" + " exists (" + "     select 1"
					+ "     from dmacc_t_paziente z" + "     where z.id_paziente = a.id_paziente and"
					+ "           (z.codice_fiscale_mmg is null or"
					+ "           z.codice_fiscale_mmg <> c.soggetto_cf)" + " )), ',') as elenco_cf";
	public static final String SELECT_SOGGETTO_QUALUNQUE = 
			" SELECT CTS.* "
			+ " FROM	COD_T_SOGGETTO CTS 	"
			+ " WHERE CTS.SOGGETTO_CF = :soggettoCf 		";
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public List<String> getCodiciFiscali(Date ultimaEsecuzione) {
		logger.info("getCodiciFiscali Begin");
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("ultimaEsecuzione", ultimaEsecuzione);
		List<String> resFromCall = jdbcTemplate.query(ELENCO_CF_DA_CHIUDERE_QUERY, namedParameters, (rs, rowNum) -> new String(rs.getString("elenco_cf")));
		logger.info("getCodiciFiscali End");
		return resFromCall;
	}

	public List<Long> selectSoggettoByCF(String cfSoggetto) {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("soggettoCf", cfSoggetto);
		List<Long> selected = jdbcTemplate.query(SELECT_SOGGETTO_QUALUNQUE, namedParameters,(rs, rowNum) -> rs.getLong("soggetto_id"));
		return selected;
	}

	public List<String> getCodiciFiscali() {
		logger.info("getCodiciFiscali Begin");
		SqlParameterSource namedParameters = new MapSqlParameterSource();
		List<String> resFromCall = jdbcTemplate.query(ELENCO_CF_DA_CHIUDERE_QUERY_FIRST_TIME, namedParameters, (rs, rowNum) -> new String(rs.getString("elenco_cf")));
		logger.info("getCodiciFiscali End");
		return resFromCall;
	}
}
