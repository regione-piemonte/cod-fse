/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.repository;

import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dmacodbatch.notifica.core.dto.ModelAssistito;
import it.csi.dmacodbatch.notifica.core.dto.custom.CountSoggetti;
import it.csi.dmacodbatch.notifica.core.dto.custom.Soggetto;
import it.csi.dmacodbatch.notifica.core.mapper.AssistitiAbilitatiMapper;
import it.csi.dmacodbatch.notifica.core.mapper.AssistitiDisabilitatiMapper;
import it.csi.dmacodbatch.notifica.core.mapper.CountSoggettiMapper;
import it.csi.dmacodbatch.notifica.core.mapper.SoggettoMapper;
import it.csi.dmacodbatch.notifica.core.util.DatabaseException;
import it.csi.dmacodbatch.notifica.core.util.LoggerUtil;

@Repository
public class CodTSoggettoDao extends LoggerUtil {

public static final String SELECT_SOGGETTO_MEDICO =					"SELECT 								" +
																"CTS.* 									" +
																"FROM 									" +
																"	COD_T_SOGGETTO CTS 					" +
																"WHERE 				 					" +
																"	CTS.SOGGETTO_CF = :soggettoCf 		" +
																"	AND CTS.SOGGETTO_IS_MEDICO = TRUE 	" ;
public static final String SELECT_SOGGETTO_QUALUNQUE =	"SELECT 								" +
														"CTS.* 									" +
														"FROM 									" +
														"	COD_T_SOGGETTO CTS 					" +
														"WHERE 				 					" +
														"	CTS.SOGGETTO_CF = :soggettoCf 		" ;


public static final String SELECT_SOGGETTO_BY_ID =				"SELECT 								" +
																"CTS.* 									" +
																"FROM 									" +
																"	COD_T_SOGGETTO CTS 					" +
																"WHERE 				 					" +
																"	CTS.SOGGETTO_ID = :soggettoId ; 		" ;

public static final String INSERT_SOGGETTO =					"INSERT                        " +
																"	INTO                       " +
																"	COD.COD_T_SOGGETTO         " +
																"(SOGGETTO_CF,                 " +
																"	SOGGETTO_NOME,             " +
																"	SOGGETTO_COGNOME,          " +
																"	SOGGETTO_DATA_DI_NASCITA,  " +
																"	SOGGETTO_SESSO,            " +
																"	ID_PAZIENTE,               " +
																"	SOGGETTO_IS_MEDICO,        " +
																"	DATA_CREAZIONE,            " +
																"	DATA_MODIFCA,              " +
																"	UTENTE_CREAZIONE,          " +
																"	UTENTE_MODIFICA)           " +
																"VALUES( :soggettoCf,          " +
																" :soggettoNome,               " +
																" :soggettoCognome,            " +
																" :soggettoDataDiNascita,      " +
																" NULL,                        " +
																" NULL,                        " +
																" TRUE,                        " +
																" :dataCreazione,              " +
																" :dataModifica,               " +
																" :utenteCreazione,            " +
																" :utenteModifica)             " ;

public static final String SELECT_LST_SOGGETTO_ABILITATO = 		"SELECT												" +
																"   CTS.*											" +
																"FROM												" +
																"   COD_T_SOGGETTO CTS,								" +
																"	COD_T_SOGGETTO_ABILITATO CTSA					" +
																"WHERE												" + 
																"	CTSA.SOGGETTO_ID_ABILITATO = CTS.SOGGETTO_ID	" +
																"	AND CTSA.SOGGETTO_ID_ABILITANTE =(				" +
																"		SELECT										" +
																"			SOGGETTO_ID								" +
																"		FROM										" +
																"			COD_T_SOGGETTO							" +
																"		WHERE										" +
																"			SOGGETTO_CF = :soggettoCf				" +
																"			AND SOGGETTO_IS_MEDICO = TRUE			" +
																"	)												";

public static final String COUNT_SOGGETTO_ABILITATO = 			"SELECT												" +
																"   COUNT (CTS.*)									" +
																"FROM												" +
																"   COD_T_SOGGETTO CTS,								" +
																"	COD_T_SOGGETTO_ABILITATO CTSA					" +
																"WHERE												" + 
																"	CTSA.SOGGETTO_ID_ABILITATO = CTS.SOGGETTO_ID	" +
																"	AND CTSA.SOGGETTO_ID_ABILITANTE =(				" +
																"		SELECT										" +
																"			SOGGETTO_ID								" +
																"		FROM										" +
																"			COD_T_SOGGETTO							" +
																"		WHERE										" +
																"			SOGGETTO_CF = :soggettoCf				" +
																"			AND SOGGETTO_IS_MEDICO = TRUE			" +
																"	)												";

public static final String SELECT_LST_SOGGETTO_DISABILITATO =  " SELECT DISTINCT ON (CTSD.SOGGETTO_ID_ABILITATO)\r\n"
		+ "  CTS.*, \r\n"
		+ " CDDM.*,\r\n"
		+ " CTSD.DISABILITAZIONE_MOTIVAZIONE	\r\n"
		+ "  FROM COD_T_SOGGETTO_DISABILITATO CTSD\r\n"
		+ "  JOIN COD_T_SOGGETTO MEDICO ON  CTSD.SOGGETTO_ID_ABILITANTE=MEDICO.SOGGETTO_ID \r\n"
		+ "  JOIN COD_T_SOGGETTO CTS ON CTSD.SOGGETTO_ID_ABILITATO =CTS.SOGGETTO_ID \r\n"
		+ "  JOIN COD_D_DISABILITAZIONE_MOTIVO CDDM ON CTSD.DISABILITAZIONE_MOTIVO_ID = CDDM.DISABILITAZIONE_MOTIVO_ID \r\n"
		+ "  WHERE MEDICO.SOGGETTO_CF=:soggettoCf\r\n"
		+ "  AND MEDICO.SOGGETTO_IS_MEDICO IS TRUE\r\n"
		+ "AND NOT EXISTS (SELECT 1 FROM COD_T_SOGGETTO_ABILITATO Z WHERE Z.SOGGETTO_ID_ABILITATO=CTSD.SOGGETTO_ID_ABILITATO\r\n"
		+ "AND Z.SOGGETTO_ID_ABILITANTE=MEDICO.SOGGETTO_ID) ";

public static final String COUNT_SOGGETTO_DISABILITATO =  	"with disabilitati_res as (\r\n"
		+ "  select distinct on (a.soggetto_id_abilitato)\r\n"
		+ "  a.soggetto_id_abilitato from cod_t_soggetto_disabilitato a\r\n"
		+ "  join cod_t_soggetto b\r\n"
		+ "on  a.soggetto_id_abilitante=b.soggetto_id where b.soggetto_cf=:soggettoCf\r\n"
		+ "  and b.soggetto_is_medico is true\r\n"
		+ "and not exists (select 1 from cod_t_soggetto_abilitato z where z.soggetto_id_abilitato=a.soggetto_id_abilitato\r\n"
		+ "and z.soggetto_id_abilitante=b.soggetto_id\r\n"
		+ ")\r\n"
		+ ")\r\n"
		+ "select\r\n"
		+ "  (select count(*) from disabilitati_res) ";

public static final String SELECT_SOGGETTO_ASSISTITO = 			"SELECT	DISTINCT								" +
																"	CTS.*										" +
																"FROM											" +
																"	COD_T_SOGGETTO CTS, COD_T_CONVERSAZIONE	CTC	" +
																"WHERE											" +
																"	CTC.SOGGETTO_ID_AUTORE = CTS.SOGGETTO_ID	" +
																"	AND CTC.SOGGETTO_ID_AUTORE = :soggettoId	";


public static final String SELECT_COUNT_SOGGETTI_ABILITATI_DISABILITATI = "SELECT MAX(countAbilitato) AS abilitati, MAX(countDisabilitato) as disabilitati FROM ( "
																	+ " SELECT count(*) as countAbilitato, 0 as countDisabilitato "
																	+ "	FROM cod.cod_t_soggetto_abilitato ctsa "
																	+ " inner join cod_t_soggetto cts on (ctsa.soggetto_id_abilitante = cts.soggetto_id ) " 
																	+ " where cts.soggetto_cf = :soggettoCf and cts.soggetto_is_medico is true "
																	+" union "
																	+" SELECT 0 as countAbilitato, count(*) as countDisabilitato "
																	+" FROM cod.cod_t_soggetto_disabilitato ctsd " 
																	+" inner join cod_t_soggetto cts on (ctsd.soggetto_id_abilitante = cts.soggetto_id ) " 
																	+" where cts.soggetto_cf = :soggettoCf and cts.soggetto_is_medico is true) as soggetti ";

public static final String SELECT_COUNT_SOGGETTI_ABILITATI_DISABILITATI_MODIFICATA="with  abilitati_res as (\r\n"
		+ "select a.soggetto_id_abilitato From cod_t_soggetto_abilitato a\r\n"
		+ "join  cod_t_soggetto b  on a.soggetto_id_abilitante=b.soggetto_id\r\n"
		+ "where b.soggetto_cf=:soggettoCf\r\n"
		+ "and b.soggetto_is_medico is true\r\n"
		+ ")\r\n"
		+ ",\r\n"
		+ "disabilitati_res as (\r\n"
		+ "  select distinct on (a.soggetto_id_abilitato)\r\n"
		+ "  a.soggetto_id_abilitato from cod_t_soggetto_disabilitato a\r\n"
		+ "  join cod_t_soggetto b\r\n"
		+ "on  a.soggetto_id_abilitante=b.soggetto_id where b.soggetto_cf=:soggettoCf\r\n"
		+ "  and b.soggetto_is_medico is true\r\n"
		+ "and not exists (select 1 from cod_t_soggetto_abilitato z where z.soggetto_id_abilitato=a.soggetto_id_abilitato\r\n"
		+ "and z.soggetto_id_abilitante=b.soggetto_id\r\n"
		+ ")\r\n"
		+ "order by a.soggetto_id_abilitato, a.abilitazione_id desc\r\n"
		+ ")\r\n"
		+ "select\r\n"
		+ "(select count(*) from abilitati_res) abilitati,\r\n"
		+ "  (select count(*) from disabilitati_res) disabilitati ; ";
private static final String BASE_OFFSET = " OFFSET   :offset ";
private static final String BASE_LIMIT = " LIMIT :limit  ";
private static final String BASE_FINALE_CHIUSURA = "; ";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public Soggetto selectSoggetto(String cfSoggetto) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoCf", cfSoggetto);
		try {
			Soggetto selected = jdbcTemplate.queryForObject(SELECT_SOGGETTO_MEDICO, namedParameters, new SoggettoMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggetto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	public Soggetto selectSoggettoByCF(String cfSoggetto) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoCf", cfSoggetto);
		try {
			Soggetto selected = jdbcTemplate.queryForObject(SELECT_SOGGETTO_QUALUNQUE, namedParameters, new SoggettoMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggetto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Soggetto selectSoggettoById(Long soggettoId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoId", soggettoId);
		try {
			Soggetto selected = jdbcTemplate.queryForObject(SELECT_SOGGETTO_BY_ID, namedParameters, new SoggettoMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggettoById";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertSoggetto(Soggetto soggetto) throws DatabaseException {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Timestamp sqlTimestamp =  Timestamp.valueOf(df.format(date));
        System.out.println("currrent date              : " + sqlTimestamp.toString());
        
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoCf"				, soggetto.getSoggettoCf())
				.addValue("soggettoNome"			, soggetto.getSoggettoNome())
				.addValue("soggettoCognome"			, soggetto.getSoggettoCognome())
				.addValue("soggettoDataDiNascita"	, soggetto.getSoggettoDataDiNascita())
				.addValue("utenteModifica"			, soggetto.getUtenteModifica())
				.addValue("utenteCreazione"			, soggetto.getUtenteCreazione())
				.addValue("dataModifica"			, sqlTimestamp)
				.addValue("dataCreazione"			, sqlTimestamp);

		try {
			int insert = jdbcTemplate.update(INSERT_SOGGETTO, namedParameters);
			return insert;
		} 
		catch (Exception e) {
			var methodName = "insertSoggetto";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	public Soggetto selectSoggettoAssistito(Integer soggettoId, String cognome, String codiceFiscale) throws DatabaseException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		SqlParameterSource namedParameters = mapSqlParameterSource;

		StringBuilder sb = new StringBuilder(SELECT_SOGGETTO_ASSISTITO);

		namedParameters = mapSqlParameterSource.addValue("soggettoId", soggettoId);
		
		if (cognome != null && cognome != "") {
			mapSqlParameterSource.addValue("cognome", "%" + cognome + "%");
			sb.append("AND CTS.SOGGETTO_COGNOME like :cognome ");
		}

		if (codiceFiscale != null && codiceFiscale != "") {
			mapSqlParameterSource.addValue("codiceFiscale", "%" + codiceFiscale + "%");
			sb.append("AND CTS.SOGGETTO_CF LIKE :codiceFiscale ");
		}

		if (cognome != null || codiceFiscale != null) {
			sb.append("AND CTS.SOGGETTO_IS_MEDICO = false ");
		}
		try {
			return jdbcTemplate.queryForObject(sb.toString(), namedParameters, new SoggettoMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggettoAssistito";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	public List<ModelAssistito> selectLstAssistitiAbilitati(String soggettoCf, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso,String stato) throws DatabaseException{
		
		MapSqlParameterSource mapSqlParameterSource  = new MapSqlParameterSource();
		SqlParameterSource namedParameters = mapSqlParameterSource.addValue("soggettoCf", soggettoCf);
		StringBuilder sb = new StringBuilder(SELECT_LST_SOGGETTO_ABILITATO);

		try {
			String query = customQuery(cognome, nome, codiceFiscale, etaMin, etaMax, sesso, sb, mapSqlParameterSource);
			List<Map<String, Object>> rstQueryLst =jdbcTemplate.queryForList(query, namedParameters);
			
			List<ModelAssistito> rstLst = new ArrayList<>();
			rstQueryLst.forEach(res ->{
			try {
					AssistitiAbilitatiMapper aAM = new AssistitiAbilitatiMapper();;
					rstLst.add(aAM.mapOBJ(res,stato));
				} catch (SQLException e) {
					var methodName = "selectLstAssistitiAbilitati";
					logError(methodName, e.getMessage());
				}
			});
			return rstLst;
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			var methodName = "selectLstAssistitiAbilitati";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	public List<ModelAssistito> selectLstAssistitiAbilitatiPag(String soggettoCf, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso,String stato,Integer limit, Integer offset) throws DatabaseException{
		
		MapSqlParameterSource mapSqlParameterSource  = new MapSqlParameterSource()
				.addValue("soggettoCf", soggettoCf).addValue("offset", offset).addValue("limit", limit);
		
		StringBuilder sb = new StringBuilder(SELECT_LST_SOGGETTO_ABILITATO);

		try {
			StringBuilder sbparam = customQuerySB(cognome, nome, codiceFiscale, etaMin, etaMax, sesso, sb, mapSqlParameterSource);

			if (offset != null) {
				sbparam.append(BASE_OFFSET);
			}
			if (limit != null) {
				sbparam.append(BASE_LIMIT);
			}
			sbparam.append(BASE_FINALE_CHIUSURA);
			System.out.println(sbparam.toString());
			
			
			List<Map<String, Object>> rstQueryLst =jdbcTemplate.queryForList(sbparam.toString(), mapSqlParameterSource);
			
			List<ModelAssistito> rstLst = new ArrayList<>();
			rstQueryLst.forEach(res ->{
			try {
					AssistitiAbilitatiMapper aAM = new AssistitiAbilitatiMapper();;
					rstLst.add(aAM.mapOBJ(res,stato));
				} catch (SQLException e) {
					var methodName = "selectLstAssistitiAbilitati";
					logError(methodName, e.getMessage());
				}
			});
			return rstLst;
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			var methodName = "selectLstAssistitiAbilitati";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	public Integer countTotalAssistitiAbilitatiPag(String soggettoCf, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso,String stato) throws DatabaseException{
		
		MapSqlParameterSource mapSqlParameterSource  = new MapSqlParameterSource()
				.addValue("soggettoCf", soggettoCf);
		
		StringBuilder sb = new StringBuilder(COUNT_SOGGETTO_ABILITATO);

		try {
			StringBuilder sbparam = customQuerySB(cognome, nome, codiceFiscale, etaMin, etaMax, sesso, sb, mapSqlParameterSource);

			sbparam.append(BASE_FINALE_CHIUSURA);
			System.out.println(sbparam.toString());
			
			
			return jdbcTemplate.queryForObject(sbparam.toString(),mapSqlParameterSource,Integer.class);
			
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			var methodName = "countTotalAssistitiAbilitatiPag";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	public List<ModelAssistito> selectLstAssistitiiDisabilitati(String soggettoCf, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso,String stato) throws DatabaseException, SQLException {
		
		MapSqlParameterSource mapSqlParameterSource  = new MapSqlParameterSource();
		SqlParameterSource namedParameters = mapSqlParameterSource;
		
		namedParameters =mapSqlParameterSource.addValue("soggettoCf", soggettoCf);
		StringBuilder sb = new StringBuilder(SELECT_LST_SOGGETTO_DISABILITATO);
		try {
			String query = customQuery(cognome, nome, codiceFiscale, etaMin, etaMax, sesso, sb, mapSqlParameterSource);
			// ordinamento per permettere di restituire solo la max abilitazione_fine
			query += "	order by (CTS.soggetto_id , CTSD.abilitazione_id, CTSD.abilitazione_fine) DESC ";

			List<Map<String, Object>> rstQueryLst =jdbcTemplate.queryForList(query, namedParameters);
			List<ModelAssistito> rstLst = new ArrayList<>();
			Hashtable<String, String> htMaxAbilitazioneFine = new Hashtable<>();
			rstQueryLst.forEach(res ->{
				try {
					AssistitiDisabilitatiMapper aDM = new AssistitiDisabilitatiMapper();
					String tmpCf = (String)res.get("soggetto_cf");
					if(!htMaxAbilitazioneFine.containsKey(tmpCf)) {
						htMaxAbilitazioneFine.put(tmpCf, "");
						rstLst.add(aDM.mapOBJ(res,stato));
					}
				} catch (SQLException e) {
					var methodName = "selectLstAssistitiiDisabilitati";
					logError(methodName, e.getMessage());
				}
			});

			return rstLst;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectLstAssistitiiDisabilitati";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	public List<ModelAssistito> selectLstAssistitiiDisabilitatiPag(String soggettoCf, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso,String stato,
			Integer limit, Integer offset) throws DatabaseException {
		
		MapSqlParameterSource mapSqlParameterSource  = new MapSqlParameterSource()
				.addValue("soggettoCf", soggettoCf).addValue("offset", offset).addValue("limit", limit);
		
		StringBuilder sb = new StringBuilder(SELECT_LST_SOGGETTO_DISABILITATO);
		try {
			StringBuilder sbParams= customQuerySB(cognome, nome, codiceFiscale, etaMin, etaMax, sesso, sb, mapSqlParameterSource);
//			sbParams.append("	order by (CTS.soggetto_id , CTSD.abilitazione_id, CTSD.abilitazione_fine) DESC ");
			if (offset != null) {
				sbParams.append(BASE_OFFSET);
			}
			if (limit != null) {
				sbParams.append(BASE_LIMIT);
			}
			
			
			sbParams.append(BASE_FINALE_CHIUSURA);
			System.out.println(sbParams.toString());
			
			// ordinamento per permettere di restituire solo la max abilitazione_fine

			List<Map<String, Object>> rstQueryLst =jdbcTemplate.queryForList(sbParams.toString(), mapSqlParameterSource);
			List<ModelAssistito> rstLst = new ArrayList<>();
			Hashtable<String, String> htMaxAbilitazioneFine = new Hashtable<>();
			rstQueryLst.forEach(res ->{
				try {
					AssistitiDisabilitatiMapper aDM = new AssistitiDisabilitatiMapper();
					String tmpCf = (String)res.get("soggetto_cf");
					if(!htMaxAbilitazioneFine.containsKey(tmpCf)) {
						htMaxAbilitazioneFine.put(tmpCf, "");
						rstLst.add(aDM.mapOBJ(res,stato));
					}
				} catch (SQLException e) {
					var methodName = "selectLstAssistitiiDisabilitatiPag";
					logError(methodName, e.getMessage());
				}
			});

			return rstLst;
		} catch (EmptyResultDataAccessException e) {
			 return new ArrayList<>();
		} catch (Exception e) {
			var methodName = "selectLstAssistitiiDisabilitatiPag";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
	}
	}
	
	public Integer countTotalAssistitiiDisabilitatiPag(String soggettoCf, String cognome, String nome,
			String codiceFiscale, Integer etaMin, Integer etaMax, String sesso,String stato
			) throws DatabaseException {
		
		MapSqlParameterSource mapSqlParameterSource  = new MapSqlParameterSource()
				.addValue("soggettoCf", soggettoCf);
		
		StringBuilder sb = new StringBuilder(COUNT_SOGGETTO_DISABILITATO);
		try {
			StringBuilder sbParams= customQuerySB(cognome, nome, codiceFiscale, etaMin, etaMax, sesso, sb, mapSqlParameterSource);
			
			sbParams.append(BASE_FINALE_CHIUSURA);
			

			return jdbcTemplate.queryForObject(sbParams.toString(), mapSqlParameterSource,Integer.class);
		} catch (EmptyResultDataAccessException e) {
			 return 0;
		} catch (Exception e) {
			var methodName = "countTotalAssistitiiDisabilitatiPag";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
	}
	}
	
	
	// CUSTOM DELLA QUERY IN BASE ALLA VALORIZZAZIONE DEI PARAMETRI IN INPUT
	private String customQuery(String cognome, String nome, String codiceFiscale, Integer etaMin,
			Integer etaMax, String sesso, StringBuilder sb, MapSqlParameterSource mapSqlParameterSource) {
		

		if (cognome != null && cognome != "") {
			
			mapSqlParameterSource.addValue("cognome", "%"+cognome.toUpperCase()+"%");
			sb.append("AND CTS.SOGGETTO_COGNOME like :cognome ");
		}
		if (nome != null && nome != "") {
			mapSqlParameterSource.addValue("nome", "%"+nome.toUpperCase()+"%");
			sb.append("AND CTS.SOGGETTO_NOME LIKE :nome ");
		}

		if (codiceFiscale != null && codiceFiscale != "") {
			mapSqlParameterSource.addValue("codiceFiscale", "%"+codiceFiscale+"%");
			sb.append("AND CTS.SOGGETTO_CF LIKE :codiceFiscale ");
		}
		if (etaMin != null) {
			System.out.println(calculateMinAge(etaMin));
			mapSqlParameterSource.addValue("etaMin", calculateMinAge(etaMin));
			sb.append(" AND CTS.SOGGETTO_DATA_DI_NASCITA <= :etaMin ");
		}
		if (etaMax != null) {
			System.out.println(caluclateMaxAge(etaMax));
			mapSqlParameterSource.addValue("etaMax", caluclateMaxAge(etaMax));
			sb.append("AND CTS.SOGGETTO_DATA_DI_NASCITA >= :etaMax ");
		}
		if (sesso != null && sesso != "") {
			mapSqlParameterSource.addValue("sesso", sesso);
			sb.append("AND CTS.SOGGETTO_SESSO = :sesso ");
		}
		if (cognome != null || nome != null || codiceFiscale != null || etaMin != null || etaMax != null
				|| sesso != null) {
			sb.append("AND CTS.SOGGETTO_IS_MEDICO = false");
		}
		System.out.println(sb.toString());
		return sb.toString();
	}
	
	private StringBuilder customQuerySB(String cognome, String nome, String codiceFiscale, Integer etaMin,
			Integer etaMax, String sesso, StringBuilder sb, MapSqlParameterSource mapSqlParameterSource) {
		

		if (cognome != null && cognome != "") {
			
			mapSqlParameterSource.addValue("cognome", "%"+cognome.toUpperCase()+"%");
			sb.append("AND CTS.SOGGETTO_COGNOME like :cognome ");
		}
		if (nome != null && nome != "") {
			mapSqlParameterSource.addValue("nome", "%"+nome.toUpperCase()+"%");
			sb.append("AND CTS.SOGGETTO_NOME LIKE :nome ");
		}

		if (codiceFiscale != null && codiceFiscale != "") {
			mapSqlParameterSource.addValue("codiceFiscale", "%"+codiceFiscale+"%");
			sb.append("AND CTS.SOGGETTO_CF LIKE :codiceFiscale ");
		}
		if (etaMin != null) {
			System.out.println(calculateMinAge(etaMin));
			mapSqlParameterSource.addValue("etaMin", calculateMinAge(etaMin));
			sb.append(" AND CTS.SOGGETTO_DATA_DI_NASCITA <= :etaMin ");
		}
		if (etaMax != null) {
			System.out.println(caluclateMaxAge(etaMax));
			mapSqlParameterSource.addValue("etaMax", caluclateMaxAge(etaMax));
			sb.append("AND CTS.SOGGETTO_DATA_DI_NASCITA >= :etaMax ");
		}
		if (sesso != null && sesso != "") {
			mapSqlParameterSource.addValue("sesso", sesso);
			sb.append("AND CTS.SOGGETTO_SESSO = :sesso ");
		}
		if (cognome != null || nome != null || codiceFiscale != null || etaMin != null || etaMax != null
				|| sesso != null) {
			sb.append("AND CTS.SOGGETTO_IS_MEDICO = false");
		}
		System.out.println(sb.toString());
		return sb;
	}
	
	public CountSoggetti selectCountSoggettiAbilitatiDisabilitati(String codiceFiscale) throws DatabaseException {

		MapSqlParameterSource mapSqlParameterSource = new MapSqlParameterSource();
		SqlParameterSource namedParameters = mapSqlParameterSource;
		StringBuilder sb = new StringBuilder(SELECT_COUNT_SOGGETTI_ABILITATI_DISABILITATI_MODIFICATA);
		namedParameters = mapSqlParameterSource.addValue("soggettoCf", codiceFiscale);
	
		try {
			return jdbcTemplate.queryForObject(sb.toString(), namedParameters, new CountSoggettiMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectCountSoggettiAbilitatiDisabilitati";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}	
	
	private Timestamp calculateMinAge(Integer etaMin) {
		return Timestamp.valueOf((calculateYear() - etaMin)+"-01-01 00:00:00.000");
	}
	private Timestamp caluclateMaxAge(Integer etaMax) {
		return Timestamp.valueOf((calculateYear() - etaMax)+"-12-31 00:00:00.000");
	}
	private Integer calculateYear() {
		Date date = new Date();
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
		simpleDateFormat.applyPattern("yyyy");
		return  Integer.parseInt(simpleDateFormat.format(date));
	}

	
	
}
