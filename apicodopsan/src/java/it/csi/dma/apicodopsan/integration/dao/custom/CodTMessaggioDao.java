/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ModelAutore.TipoEnum;
import it.csi.dma.apicodopsan.dto.ModelUltimoMessaggio;
import it.csi.dma.apicodopsan.dto.custom.TMessaggio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.integration.dao.utils.TMessaggioMapper;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodTMessaggioDao extends LoggerUtil {

public static final String SELECT_T_MESSAGGIO =	        "SELECT 										" +
												        "CTM.* 											" +
												        "FROM 											" +
												        "	COD_T_MESSAGGIO CTM, 						" +
												        "	COD_T_SOGGETTO CTS,							" +
												        "	COD_T_CONVERSAZIONE	CTC						" +
												        "WHERE 									   		" +
												        "	CTC.CONVERSAZIONE_COD  =:conversazioneId  	" +
												        "   AND CTC.CONVERSAZIONE_ID= CTC.CONVERSAZIONE_ID "+
												        "	AND CTM.MESSAGGIO_ID =:messaggioId			" +	
												        "	AND CTM.SOGGETTO_ID_A =	CTS.SOGGETTO_ID		" + 
												        "	AND CTS.SOGGETTO_CF = :cfSoggetto			" +
												        "	AND CTS.SOGGETTO_IS_MEDICO = TRUE			";


public static final String UPDATE_T_MESSAGGIO = 		"UPDATE								" +
														"	COD_T_MESSAGGIO					" +
														"SET								" +
														"	MESSAGGIO_LETTURA_DA_CF=:cf,	" +
														"	MESSAGGIO_LETTURA_DATA =:now	" +
														"WHERE								" +
														" MESSAGGIO_ID = :messaggioId		";

public static final String COUNT_MESS_NON_LETTI =		"SELECT COUNT(*)                            " +
														"FROM                                       " +
														"	COD_T_MESSAGGIO CTM, COD_T_SOGGETTO CTS " +
														"WHERE                                      " +
														"	CTM.CONVERSAZIONE_ID = :convId          " +
														"	AND CTM.SOGGETTO_ID_A=CTS.SOGGETTO_ID   " +
														"	AND (CTM.MESSAGGIO_LETTURA_DATA  IS NULL " +
														" OR CTM.MESSAGGIO_LETTURA_DATA < CTM.DATA_MODIFICA ) "+
														"	AND CTS.SOGGETTO_CF = :soggettoCf       " +
														"	AND CTS.SOGGETTO_IS_MEDICO = TRUE       ";

public static final String SELECT_ULTIMO_MESSAGGIO =	"SELECT                               " +
														"	CTM.*                             " +
														"FROM                                 " +
														"	COD_T_MESSAGGIO CTM               " +
														"WHERE                                " +
														"	DATA_CREAZIONE = (                " +
														"	SELECT                            " +
														"		MAX(DATA_CREAZIONE)           " +
														"	FROM                              " +
														"		COD_T_MESSAGGIO               " +
														"	WHERE                             " +
														"		CONVERSAZIONE_ID = :convId)   ";


public static final String SELECT_MESSAGGIO_DECODIFICATO = 	"SELECT																			" +
															"convert_from(pgp_sym_decrypt_bytea(MESSAGGIO_TESTO_CIFRATO,:encryptionkey), 'UTF8') as messaggio_testo_cifrato "+
															"FROM																			" +
															"COD_T_MESSAGGIO																" + 
															"WHERE																			" +
															"MESSAGGIO_ID = :messId";

public static final String SELECT_VALORE_PARAMETRO =    "SELECT						" +
														"	PARAMETRO_VALORE		" +
														"FROM						" +
														"	COD_C_PARAMETRO CCP		" +
														"WHERE						" +
														"	PARAMETRO_NOME = :nome	";         

public static final String LAST_MESS_ASSISTITO = 	"SELECT COUNT(*)								" +
													"FROM											" +
													"	COD_T_SOGGETTO CTS, COD_T_MESSAGGIO CTM		" +
													"WHERE											" +
													"	CTS.SOGGETTO_CF = CTM.UTENTE_MODIFICA		" +
													"	AND CTM.SOGGETTO_ID_DA = CTS.SOGGETTO_ID	" +
													"	AND CTM.conversazione_id = :convId			" +
													"	AND CTM.data_creazione = (					" +
													"		SELECT MAX(DATA_CREAZIONE)				" +
													"		FROM									" +
													"			COD_T_MESSAGGIO						" +
													"		WHERE									" +
													"			CONVERSAZIONE_ID = :convId)			";

public static final String LAST_MESS_DELEGATO = 	"SELECT COUNT(*)								" +
													"FROM											" +
													"	COD_T_SOGGETTO CTS, COD_T_MESSAGGIO CTM		" +
													"WHERE											" +
													"	CTS.SOGGETTO_CF != CTM.UTENTE_MODIFICA		" +
													"	AND CTM.SOGGETTO_ID_DA = CTS.SOGGETTO_ID	" +
													"	AND CTM.conversazione_id = :convId			" +
													"	AND CTM.data_creazione = (					" +
													"		SELECT MAX(DATA_CREAZIONE)				" +
													"		FROM									" +
													"			COD_T_MESSAGGIO						" +
													"		WHERE									" +
													"			CONVERSAZIONE_ID = :convId)			";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Value("${encryptionkey}")
	private String encryptionkey;
	
	public TMessaggio selectTMessaggio(String conversazioneId, String messaggioId, String cfSoggetto) throws DatabaseException {
        		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("conversazioneId", conversazioneId)
				.addValue("messaggioId", 	 Integer.parseInt(messaggioId))
				.addValue("cfSoggetto", 	 cfSoggetto);
		
		try {
			return jdbcTemplate.queryForObject(SELECT_T_MESSAGGIO, namedParameters, new TMessaggioMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectTMessaggio";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateTMessaggio(int messaggioId, String cf) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("cf",			 cf)
				.addValue("now", 		 getCurrentDate())
				.addValue("messaggioId", messaggioId);
		
		try {
			jdbcTemplate.update(UPDATE_T_MESSAGGIO, namedParameters);
		} 
		catch (Exception e) {
			var methodName = "updateTMessaggio";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	public Integer countMessNonLetti(Integer convId, String soggettoCf) throws DatabaseException {
        
		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("convId",convId)
						.addValue("soggettoCf", soggettoCf);
		try {
			return jdbcTemplate.queryForObject(COUNT_MESS_NON_LETTI, namedParameters, Integer.class);
		} 
		catch (Exception e) {
			var methodName = "countMessNonLetti";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
	
	public ModelUltimoMessaggio selectUltimoMess(int convId) throws DatabaseException {
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("convId", convId); 
		try {
			Map<String, Object> rstQuery = jdbcTemplate.queryForMap(SELECT_ULTIMO_MESSAGGIO, namedParameters);
			
			
			namedParameters = new MapSqlParameterSource().addValue("messId", Integer.parseInt(rstQuery.get("messaggio_id")+"")).addValue("encryptionkey", encryptionkey);
			String testoDecifrato = jdbcTemplate.queryForMap(SELECT_MESSAGGIO_DECODIFICATO, namedParameters).get("messaggio_testo_cifrato")+"";
			
			TipoEnum tipo = getTipoUltimoMessaggio(convId);
			
			TMessaggioMapper mapper = new TMessaggioMapper();
			return mapper.mapMess(rstQuery, testoDecifrato, tipo);
		}
		catch (IncorrectResultSizeDataAccessException  e) {
			return null;
		}		
		catch (Exception e) {
			var methodName = "selectUltimoMess";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	
	public TipoEnum getTipoUltimoMessaggio(int convId) throws DatabaseException {
		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("convId", convId);
		
		try {
			log.info(LAST_MESS_ASSISTITO);
			return jdbcTemplate.queryForObject(LAST_MESS_ASSISTITO, namedParameters, Integer.class) == 1 ? TipoEnum.ASSISTITO 
					: jdbcTemplate.queryForObject(LAST_MESS_DELEGATO, namedParameters, Integer.class) == 1 ? TipoEnum.DELEGATO : TipoEnum.MEDICO;	
		} catch (Exception e) {
			var methodName = "getTipoUltimoMessaggio";
			logError(methodName, e.getMessage());
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}
	private Timestamp getCurrentDate() {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        return Timestamp.valueOf(df.format(date));
	}
}
