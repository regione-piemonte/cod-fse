/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.custom.TAdesione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodSConversazioneDao extends LoggerUtil {

public static final String SELECT_COUNT_CONVERSAZIONI =
													"SELECT COUNT(*)" +
													" FROM " +
													"	COD_S_CONVERSAZIONE CSC " +
													" WHERE " +
													"	CSC.SOGGETTO_ID_AUTORE = :soggettoId ";
	
public static final String INSERT_S_CONVERSAZIONI =	
													"INSERT                                       " +
													"	INTO                                      " +
													"	COD_S_CONVERSAZIONE                   " +
													"(CONVERSAZIONE_ID,                           " +
													"	CONVERSAZIONE_COD,                        " +
													"	CONVERSAZIONE_OGGETTO,                    " +
													"	SOGGETTO_ID_AUTORE,                       " +
													"	SOGGETTO_ID_PARTECIPANTE,                 " +
													"	CONVERSAZIONE_DATA_BLOCCO,                " +
													"	DISABILITAZIONE_MOTIVO_ID,                " +
													"	DATA_CREAZIONE,                           " +
													"	DATA_MODIFICA,                            " +
													"	UTENTE_CREAZIONE,                         " +
													"	UTENTE_MODIFICA,                          " +
													"	VALIDITA_INIZIO,                          " +
													"	VALIDITA_FINE)                            " +
													" SELECT                                      " +
													"	CTC.CONVERSAZIONE_ID,                     " +
													"	CTC.CONVERSAZIONE_COD::UUID,              " +
													"	CTC.CONVERSAZIONE_OGGETTO,                " +
													"	CTC.SOGGETTO_ID_AUTORE,                   " +
													"	CTC.SOGGETTO_ID_PARTECIPANTE,             " +
													"	CTC.CONVERSAZIONE_DATA_BLOCCO,            " +
													"	CTC.DISABILITAZIONE_MOTIVO_ID,            " +
													"	CTC.DATA_CREAZIONE,                       " +
													"	CTC.DATA_MODIFICA,                        " +
													"	CTC.UTENTE_CREAZIONE,                     " +
													"	CTC.UTENTE_MODIFICA,                      " +
													"	%s				                          " +
													"	:validitaFine                             " +
													"FROM                                         " +
													"	COD_T_CONVERSAZIONE CTC               " +
													"WHERE CTC.SOGGETTO_ID_AUTORE = :soggettoId   " +
													"AND   CTC.CONVERSAZIONE_DATA_BLOCCO IS NULL  " ;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertSConversazione(TAdesione adesione) throws DatabaseException {
		
		int countConversazaioni = countConversazioni(adesione.getSoggettoId());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now - 1000l);
        Timestamp sqlTimestamp =  Timestamp.valueOf(df.format(date));
        
		String parsedSql  = String.format(INSERT_S_CONVERSAZIONI, countConversazaioni == 0 ? "CTC.DATA_CREAZIONE, " : "CTC.DATA_MODIFICA, ");
		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("soggettoId",adesione.getSoggettoId())
						.addValue("validitaFine",sqlTimestamp);
		try {
			int insert = jdbcTemplate.update(parsedSql, namedParameters);
			return insert;
		} 
		catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}	
	
	private int countConversazioni(Integer soggettoId) throws DatabaseException {
        
		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("soggettoId",soggettoId);
		try {
			Integer count = jdbcTemplate.queryForObject(SELECT_COUNT_CONVERSAZIONI, namedParameters,Integer.class);
			return count.intValue();
		} 
		catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}	
}
