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
public class CodSAdesioneDao extends LoggerUtil {

public static final String SELECT_COUNT_REVOCA =	"SELECT COUNT(*)" +
													" FROM " +
													"	COD_S_ADESIONE CSA " +
													" WHERE " +
													"	CSA.SOGGETTO_ID = :soggettoId ";
	
public static final String INSERT_S_ADESIONE =	" INSERT                                   " +
											" 	INTO                                   " +
											" 	COD_S_ADESIONE                     " +
											" ( ADESIONE_ID,                           " +
											" 	SOGGETTO_ID,                           " +
											" 	ADESIONE_INIZIO,                       " +
											" 	ADESIONE_FINE,                         " +
											" 	MOSTRA_LETTURA_MESSAGGI_A_ASSISTITI,   " +
											" 	DATA_CREAZIONE,                        " +
											" 	DATA_MODIFICA,                         " +
											" 	UTENTE_CREAZIONE,                      " +
											" 	UTENTE_MODIFICA,                       " +
											" 	VALIDITA_INIZIO,                       " +
											" 	VALIDITA_FINE)                         " +
											" VALUES( :adesioneId,                     " +
											" :soggettoId,                             " +
											" :adesioneInizio,                         " +
											" :adesioneFine,                           " +
											" :mostraLetturaMessaggiAAssistiti,        " +
											" :dataCreazione,                          " +
											" :dataModifica,                           " +
											" :utenteCreazione,                        " +
											" :utenteModifica,                         " +
											" :validitaInizio,                         " +
											" :validitaFine)                           " ;

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertSAdesione(TAdesione adesione) throws DatabaseException {

		
		int countRevoca = countRevoca(adesione.getSoggettoId());
		
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now - 1000l);
        Timestamp sqlTimestamp =  Timestamp.valueOf(df.format(date));
        
		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("adesioneId",adesione.getAdesioneId())
						.addValue("soggettoId",adesione.getSoggettoId())
						.addValue("adesioneInizio",adesione.getAdesioneInizio())
						.addValue("adesioneFine",adesione.getAdesioneFine())
						.addValue("mostraLetturaMessaggiAAssistiti",adesione.isMostraLetturaMessaggiAAssistiti())
						.addValue("dataCreazione",adesione.getDataCreazione())
						.addValue("dataModifica",adesione.getDataModifica())
						.addValue("utenteCreazione",adesione.getUtenteCreazione())
						.addValue("utenteModifica",adesione.getUtenteModifica())
						.addValue("validitaInizio",countRevoca == 0 ? adesione.getDataCreazione() : adesione.getDataModifica())
						.addValue("validitaFine",sqlTimestamp);
		try {
			int insert = jdbcTemplate.update(INSERT_S_ADESIONE, namedParameters);
			return insert;
		} 
		catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}	
	
	private int countRevoca(Integer soggettoId) throws DatabaseException {
        
		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("soggettoId",soggettoId);
		try {
			Integer count = jdbcTemplate.queryForObject(SELECT_COUNT_REVOCA, namedParameters,Integer.class);
			return count.intValue();
		} 
		catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}	
}
