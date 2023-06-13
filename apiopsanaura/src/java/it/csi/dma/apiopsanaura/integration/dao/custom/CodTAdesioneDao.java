/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.dao.custom;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apiopsanaura.dto.custom.Soggetto;
import it.csi.dma.apiopsanaura.dto.custom.TAdesione;
import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.integration.dao.utils.TAdesioneMapper;
import it.csi.dma.apiopsanaura.util.LoggerUtil;

@Repository
public class CodTAdesioneDao extends LoggerUtil {

public static final String SELECT_ADESIONE =	        "SELECT 									" +
												        "CTA.* 										" +
												        "FROM 										" +
												        "	COD_T_ADESIONE CTA, 					" +
												        "	COD_T_SOGGETTO CTS 						" +
												        "WHERE 										" +
												        "	CTS.SOGGETTO_ID = CTA.SOGGETTO_ID 		" +
												        "	AND CTS.SOGGETTO_IS_MEDICO = TRUE 		" +
												        "	AND CTS.SOGGETTO_CF = :cfSoggetto 		" +
												        "	AND CTA.ADESIONE_INIZIO <= :currentDate " +
												        "	AND (CTA.ADESIONE_FINE >= :currentDate 	" +
												        "		OR CTA.ADESIONE_FINE IS NULL) 		" + 
												        "ORDER BY CTA.ADESIONE_ID DESC LIMIT 1      ";
                                                        
public static final String INSERT_ADESIONE =	        "INSERT                                   " +
												        "	INTO                                  " +
												        "	COD_T_ADESIONE                    " +
												        "(SOGGETTO_ID,                            " +
												        "	ADESIONE_INIZIO,                      " +
												        "	ADESIONE_FINE,                        " +
												        "	MOSTRA_LETTURA_MESSAGGI_A_ASSISTITI,  " +
												        "	DATA_CREAZIONE,                       " +
												        "	DATA_MODIFICA,                        " +
												        "	UTENTE_CREAZIONE,                     " +
												        "	UTENTE_MODIFICA)                      " +
												        "VALUES( :soggettoId,                     " +
												        " :adesioneInizio,                        " +
												        " NULL,                                   " +
												        " FALSE,                                  " +
												        " :dataCreazione,                         " +
												        " :dataModifica,                          " +
												        " :utenteCreazione,                        " +
												        " :utenteModifica)                         " ;
                                                        
public static final String UPDATE_ADESIONE 	=	        "UPDATE                          " +
												        "	COD_T_ADESIONE           " +
												        "SET                             " +
												        "	ADESIONE_FINE = :now         " +
												        "WHERE                           " +
												        "	ADESIONE_ID = :adesioneId    " ;

public static final String UPDATE_ADESIONE_NOTIFICA =	"UPDATE							                            "+
														"	COD_T_ADESIONE			                            "+
														"SET							                            "+
														"	DATA_MODIFICA = :now,		                            "+
														"	UTENTE_MODIFICA = :codFiscale,                          "+
														"	MOSTRA_LETTURA_MESSAGGI_A_ASSISTITI = :letturaNotifica  "+
														"WHERE													    "+
														"	ADESIONE_ID = :adesioneId								";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public TAdesione selectAdesione(String cfSoggetto) throws DatabaseException {
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Timestamp sqlTimestamp =  Timestamp.valueOf(df.format(date));
        System.out.println("currrent date              : " + sqlTimestamp.toString());
        		
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("cfSoggetto", cfSoggetto)
				.addValue("currentDate", sqlTimestamp);
		
		try {
			TAdesione selected = jdbcTemplate.queryForObject(SELECT_ADESIONE, namedParameters, new TAdesioneMapper());
			return selected;
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertAdesione(Soggetto soggetto) throws DatabaseException {

		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        Timestamp sqlTimestamp =  Timestamp.valueOf(df.format(date));
        System.out.println("currrent date              : " + sqlTimestamp.toString());
        
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("soggettoId"				, soggetto.getSoggettoId())
				.addValue("utenteCreazione"			, soggetto.getUtenteCreazione())
				.addValue("adesioneInizio"			, sqlTimestamp)
				.addValue("dataCreazione"			, sqlTimestamp)
				.addValue("dataModifica"			, sqlTimestamp)
				.addValue("utenteModifica"			, soggetto.getUtenteModifica());

		try {
			int insert = jdbcTemplate.update(INSERT_ADESIONE, namedParameters);
			return insert;
		} 
		catch (Exception e) {
			var methodName = "insertAdesione";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}	
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public TAdesione updateAdesione(TAdesione adesione) throws DatabaseException {

        Timestamp sqlTimestamp =  getCurrentDate();
        System.out.println("currrent date              : " + sqlTimestamp.toString());
        
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("adesioneId"			, adesione.getAdesioneId())
				.addValue("now"					, getCurrentDate());
				
		try {
			jdbcTemplate.update(UPDATE_ADESIONE, namedParameters);
			adesione.setAdesioneFine(sqlTimestamp);
			return adesione;
		} 
		catch (Exception e) {
			var methodName = "updateAdesione";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public void updateAdesioneNotifica(TAdesione adesione, String codFiscale, Boolean letturaNotifica) throws DatabaseException {
		
		 Timestamp sqlTimestamp =  getCurrentDate();
	     System.out.println("currrent date              : " + sqlTimestamp.toString());
	     
	     //SETTAGGIO PARAMENTRI QUERY
	     SqlParameterSource namedParameters = new MapSqlParameterSource()
					.addValue("adesioneId"			, adesione.getAdesioneId())
					.addValue("now"					, getCurrentDate())
	     			.addValue("codFiscale"			, codFiscale)
	     			.addValue("letturaNotifica"		,letturaNotifica);
	     
	     //ESECUZIONE DELLA QUERY
	     try {
				jdbcTemplate.update(UPDATE_ADESIONE_NOTIFICA, namedParameters);
			} 
			catch (Exception e) {
				var methodName = "updateAdesioneNotifica";
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
	
}
