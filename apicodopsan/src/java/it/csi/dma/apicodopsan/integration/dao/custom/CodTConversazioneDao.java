/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.custom.TAdesione;
import it.csi.dma.apicodopsan.dto.custom.TConversazione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodTConversazioneDao extends LoggerUtil {

	public static final String UPDATE_CONVERSAZIONE_REVOCA =  "UPDATE							                        " +
															  "	COD_T_CONVERSAZIONE	CTC									" +
															  "SET							                            " +
															  "	CONVERSAZIONE_DATA_BLOCCO = :now,		            	" +
															  "	DISABILITAZIONE_MOTIVO_ID = (							" +
															  "	SELECT 													" +
															  "		DISABILITAZIONE_MOTIVO_ID							" +
															  "	FROM													" +
															  "		COD_D_DISABILITAZIONE_MOTIVO						" +
															  "	WHERE													" +
															  "		DISABILITAZIONE_MOTIVO_COD = 'REVADE' )				" +
															  "WHERE													" +
															  " CTC.SOGGETTO_ID_PARTECIPANTE = :soggettoId   					" +
															  " AND   CTC.CONVERSAZIONE_DATA_BLOCCO IS NULL  			";


	public static final String SELECT_LST_CONVERSAZIONI = 	"SELECT											  	" +
															"	CTC.*,                                        	" +
															"	CTS.SOGGETTO_ID,                              	" +
															"	CTS.SOGGETTO_CF ,                             	" +
															"	CTS.SOGGETTO_NOME,                            	" +
															"	CTS.SOGGETTO_COGNOME                          	" +
															"FROM												" +
															"	COD_T_CONVERSAZIONE CTC, COD_T_SOGGETTO CTS   	" +
															"WHERE                                            	" +
															"	CTC.SOGGETTO_ID_PARTECIPANTE = CTS.SOGGETTO_ID	" +
															"	AND CTS.SOGGETTO_CF = :soggettoCf		      	" +
															"	AND CTS.SOGGETTO_IS_MEDICO = TRUE             	";

	public static final String SELECT_CONVERSAZIONE_BY_COD_AND_SOGGETTO_CF= "SELECT												" +
    		 															"	CTC.*                  							" +
    		 															"FROM												" +
    		 															"	COD_T_CONVERSAZIONE CTC, 						" +
    		 															"	COD_T_SOGGETTO CTS   							" +
    		 															"WHERE		                                        " +
    		 															"	CTC.SOGGETTO_ID_PARTECIPANTE = CTS.SOGGETTO_ID	" +
    		 															"	AND CTS.SOGGETTO_CF = :soggettoCf		      	" +
    		 															"	AND CTS.SOGGETTO_IS_MEDICO = TRUE   			" +
    		 															"	AND CTC.CONVERSAZIONE_COD =:conversazioneCod 	";


	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("${encryptionkey}")
	private String encryptionkey;


	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int updateConversazioniRevoca(TAdesione adesione) throws DatabaseException {

		Timestamp sqlTimestamp = getCurrentDate();
		System.out.println("currrent date              : " + sqlTimestamp.toString());

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoId", adesione.getSoggettoId()).addValue("now", getCurrentDate());

		try {
			return jdbcTemplate.update(UPDATE_CONVERSAZIONE_REVOCA, namedParameters);
		} catch (Exception e) {
			var methodName = "updateConversazioniRevoca";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<Map<String, Object>> selectLstConversazioni(String soggettoCf, String solaLettura) throws DatabaseException, SQLException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		SqlParameterSource namedParameters = mapSqlParameterSource;

		StringBuilder sb = new StringBuilder(SELECT_LST_CONVERSAZIONI);

		namedParameters = mapSqlParameterSource.addValue("soggettoCf", soggettoCf);
		if (solaLettura.equals("A")) {
			sb.append("AND CTC.CONVERSAZIONE_DATA_BLOCCO IS NULL ");
		}

		if (solaLettura.equals("B")) {
			sb.append("AND CTC.CONVERSAZIONE_DATA_BLOCCO IS NOT NULL");
		}

		try {
			List<Map<String, Object>> rstQuery = jdbcTemplate.queryForList(sb.toString(), namedParameters);
			if(rstQuery.isEmpty()) return new ArrayList<>();
			return rstQuery;
		}  catch (Exception e) {
			var methodName = "selectLstConversazioni";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<TConversazione> selectListConversazioneByCodAndSoggettoCf(String conversazioneCod,String soggettoCf) throws DatabaseException{
		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		SqlParameterSource namedParameters = mapSqlParameterSource;
		namedParameters = mapSqlParameterSource
				.addValue("soggettoCf", soggettoCf)
				.addValue("conversazioneCod", conversazioneCod);
		StringBuilder sb = new StringBuilder(SELECT_CONVERSAZIONE_BY_COD_AND_SOGGETTO_CF);
			try {
				return jdbcTemplate.query(sb.toString(), namedParameters, (rs, rowNum) ->
				new TConversazione(rs.getLong(1), rs.getString(2) , rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getTimestamp(6), rs.getInt(7), rs.getTimestamp(8), rs.getTimestamp(9), rs.getString(10), rs.getString(11)));
			} catch (EmptyResultDataAccessException e) {
				return new ArrayList<>();
			} catch (Exception e) {
				var methodName = "selectListConversazioneByCodAndSoggettoCf";
				logError(methodName, e.getMessage());
				throw new DatabaseException(e);
			}
	}


	private Timestamp getCurrentDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		long now = System.currentTimeMillis();
		Date date = new Date(now);
		return Timestamp.valueOf(df.format(date));
	}

	public List<Map<String, Object>> selectLstConversazioniCompleta(String shibIdentitaCodiceFiscale, String solaLettura,
			String cognome, String codiceFiscale, String idConversazione, Integer limit, Integer offset) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("medicoCF", shibIdentitaCodiceFiscale)
				.addValue("offset", offset).addValue("limit", limit).addValue("encryptionkey", encryptionkey);
		StringBuilder sql = new StringBuilder(LST_CONVERSAZIONI_BASE_SELECT);
		if(StringUtils.isNotBlank(cognome)|| StringUtils.isNotBlank(codiceFiscale)) {

			sql.append(LST_CONVERSAZIONI_JOIN_AUTORE);
		}

		sql.append(LST_CONVERSAZIONI_BASE_WHERE);

		if(StringUtils.isNotBlank(cognome)|| StringUtils.isNotBlank(codiceFiscale)) {

			//sql.append(LST_CONVERSAZIONI_WHERE_AUTORE_BASE);
			if(StringUtils.isNotBlank(cognome)){
				sql.append(LST_CONVERSAZIONI_WHERE_AUTORE_COGNOME);
				params.addValue("autoreCognome", cognome, Types.VARCHAR);
			}
			if(StringUtils.isNotBlank(codiceFiscale)){
				sql.append(LST_CONVERSAZIONI_WHERE_AUTORE_CF);
				params.addValue("autoreCf", codiceFiscale, Types.VARCHAR);
			}
		}

		if (solaLettura.equals("A")) {
			sql.append("AND CTC.CONVERSAZIONE_DATA_BLOCCO IS NULL ");
		}

		if (solaLettura.equals("B")) {
			sql.append("AND CTC.CONVERSAZIONE_DATA_BLOCCO IS NOT NULL");
		}

		if(StringUtils.isNotBlank(idConversazione)) {
			sql.append(" AND CTC.CONVERSAZIONE_COD =:idConversazione  " );
			params.addValue("idConversazione", idConversazione);
		}
		sql.append(ORDER_BY_ID_CONVERSAZIONE);
		if (offset != null) {
			sql.append(BASE_OFFSET);
		}
		if (limit != null) {
			sql.append(BASE_LIMIT);
		}
		sql.append(BASE_FINALE_CHIUSURA);
		System.out.println(sql.toString());
		try {
			List<Map<String, Object>> rstQuery = jdbcTemplate.queryForList(sql.toString(), params);
			if(rstQuery.isEmpty())  {
				return new ArrayList<>();
			}
			return rstQuery;
		} catch (EmptyResultDataAccessException e) {
			return new  ArrayList<Map<String,Object>>();
		}  catch (Exception e) {
			var methodName = "selectLstConversazioniCompleta";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	public Integer countTotalLstConversazioniCompleta(String shibIdentitaCodiceFiscale, String solaLettura,
			String cognome, String codiceFiscale, String idConversazione) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("medicoCF", shibIdentitaCodiceFiscale);
		StringBuilder sql = new StringBuilder(LST_CONVERSAZIONI_BASE_COUNT);
		if(StringUtils.isNotBlank(cognome)|| StringUtils.isNotBlank(codiceFiscale)) {

			sql.append(LST_CONVERSAZIONI_JOIN_AUTORE);
		}

		sql.append(LST_CONVERSAZIONI_BASE_WHERE);

		if(StringUtils.isNotBlank(cognome)|| StringUtils.isNotBlank(codiceFiscale)) {

			//sql.append(LST_CONVERSAZIONI_WHERE_AUTORE_BASE);
			if(StringUtils.isNotBlank(cognome)){
				sql.append(LST_CONVERSAZIONI_WHERE_AUTORE_COGNOME);
				params.addValue("autoreCognome", cognome, Types.VARCHAR);
			}
			if(StringUtils.isNotBlank(codiceFiscale)){
				sql.append(LST_CONVERSAZIONI_WHERE_AUTORE_CF);
				params.addValue("autoreCf", codiceFiscale, Types.VARCHAR);
			}
		}

		if (solaLettura.equals("A")) {
			sql.append("AND CTC.CONVERSAZIONE_DATA_BLOCCO IS NULL ");
		}

		if (solaLettura.equals("B")) {
			sql.append("AND CTC.CONVERSAZIONE_DATA_BLOCCO IS NOT NULL");
		}

		if(StringUtils.isNotBlank(idConversazione)) {
			sql.append(" AND CTC.CONVERSAZIONE_COD =:idConversazione  " );
			params.addValue("idConversazione", idConversazione);
		}


		sql.append(BASE_FINALE_CHIUSURA);

		try {
			return jdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		}  catch (Exception e) {
			var methodName = "countTotalLstConversazioniCompleta";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
		public static final String LST_CONVERSAZIONI_BASE_COUNT =
				"SELECT	COUNT(CTC.*)			" +
				"FROM							" +
				"COD_T_CONVERSAZIONE CTC      " +
				"JOIN COD_T_SOGGETTO CTS  ON CTC.SOGGETTO_ID_PARTECIPANTE = CTS.SOGGETTO_ID	";

	public static final String LST_CONVERSAZIONI_BASE_SELECT =
			"	SELECT											" +
			"	CTC.*,                                        	" +
			"	convert_from(pgp_sym_decrypt_bytea(CTC.conversazione_oggetto,:encryptionkey), 'UTF8') as conversazione_oggetto_dec, "+
			"	CTS.SOGGETTO_ID,                              	" +
			"	CTS.SOGGETTO_CF ,                             	" +
			"	CTS.SOGGETTO_NOME,                            	" +
			"	CTS.SOGGETTO_COGNOME                          	" +
			"FROM												" +
			"	COD_T_CONVERSAZIONE CTC                         " +
			"JOIN COD_T_SOGGETTO CTS  ON CTC.SOGGETTO_ID_PARTECIPANTE = CTS.SOGGETTO_ID	  ";
	public static final String LST_CONVERSAZIONI_BASE_WHERE =
			"WHERE                                            	" +
			"	CTC.SOGGETTO_ID_PARTECIPANTE = CTS.SOGGETTO_ID	" +
			"	AND CTS.SOGGETTO_CF = :medicoCF		      	" +
			"	AND CTS.SOGGETTO_IS_MEDICO = TRUE             	";
	public static final String LST_CONVERSAZIONI_JOIN_AUTORE=
			" JOIN COD_T_SOGGETTO CTA ON CTC.SOGGETTO_ID_AUTORE= CTA.SOGGETTO_ID     ";


	/*public static final String LST_CONVERSAZIONI_WHERE_AUTORE_BASE=
			"	AND CTA.SOGGETTO_IS_MEDICO = FALSE    ";*/


	public static final String LST_CONVERSAZIONI_WHERE_AUTORE_CF=
			"	AND CTA.SOGGETTO_CF like concat('%',:autoreCf,'%')		      	";
	public static final String LST_CONVERSAZIONI_WHERE_AUTORE_COGNOME=
			"	AND CTA.SOGGETTO_COGNOME like concat('%',:autoreCognome,'%')		      	";

	private static final String ORDER_BY_ID_CONVERSAZIONE=" ORDER BY 1 DESC ";
	private static final String BASE_OFFSET = " OFFSET   :offset ";
	private static final String BASE_LIMIT = " LIMIT :limit ";
	private static final String BASE_FINALE_CHIUSURA = "; ";
}
