/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.custom.TMessaggio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodSMessaggioDao extends LoggerUtil {

public static final String INSERT_S_MESSAGGIO =  		 "INSERT							" +
														 "	INTO							" +
														 "COD_S_MESSAGGIO(					" +
														 "	MESSAGGIO_ID,					" +
														 "	MESSAGGIO_TESTO_CIFRATO,		" +
														 "	SOGGETTO_ID_DA,					" +
														 "	SOGGETTO_ID_A,					" +
														 "	MESSAGGIO_DATA_INVIO,			" +
														 "	MESSAGGIO_LETTURA_DATA,			" +
														 "	MESSAGGIO_LETTURA_DA_CF,		" +
														 "	CONVERSAZIONE_ID,				" +
														 "	DATA_CREAZIONE,					" +
														 "	DATA_MODIFICA,					" +
														 "	UTENTE_CREAZIONE,				" +
														 "	UTENTE_MODIFICA,				" +
														 "	VALIDITA_INIZIO,				" +
														 "	VALIDITA_FINE)					" +
														 "VALUES( :messaggioId,				" +
														 " 	:messaggioTestoCifrato,			" +
														 " 	:soggettoIdDa,					" +
														 " 	:soggettoIdA,					" +
														 " 	:messaggioDataInvio,			" +
														 " 	:messaggioLetturaData,			" +
														 " 	:messaggioLetturadaCf,			" +
														 " 	:conversazioneId,				" +
														 " 	:dataCreazione,					" +
														 " 	:dataModifica,					" +
														 " 	:utenteCreazione,				" +
														 " 	:utenteModifica,				" +
														 " 	:validitaInizio,				" +
														 " 	:validitaFine)					";
                                                                                                         							
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public Boolean insertSMessaggio(TMessaggio tMess) throws DatabaseException{
		
		//DATA CORRENTE -1SEC
		Calendar cal = Calendar.getInstance();
	    cal.setTimeInMillis(getCurrentDate().getTime());
	    cal.add(Calendar.SECOND, -1);
	    Timestamp nowMinus1Sec = new Timestamp(cal.getTime().getTime());
	    
		 //SETTAGGIO PARAMENTRI QUERY
	     SqlParameterSource namedParameters = new MapSqlParameterSource()
	    		 .addValue("messaggioId", 			tMess.getMessaggioId())
	    		 .addValue("messaggioTestoCifrato", tMess.getMessaggioTestoCifrato())
	    		 .addValue("soggettoIdDa", 			tMess.getSoggettoIdDa())
	    		 .addValue("soggettoIdA", 			tMess.getSoggettoIdA())
	    		 .addValue("messaggioDataInvio", 	tMess.getMessaggioDataInvio())
	    		 .addValue("messaggioLetturaData", 	tMess.getMessaggioLetturaData())
	    		 .addValue("messaggioLetturadaCf", 	tMess.getMessaggioLetturaDaCf())
	    		 .addValue("conversazioneId", 		tMess.getConversazioneId())
	    		 .addValue("dataCreazione", 		tMess.getDataCreazione())
	    		 .addValue("dataModifica", 			tMess.getDataModifica())
	    		 .addValue("utenteCreazione", 		tMess.getUtenteCreazione())
	    		 .addValue("utenteModifica", 		tMess.getUtenteModifica())
	    		 .addValue("validitaInizio", 		tMess.getDataModifica())
	    		 .addValue("validitaFine", 			nowMinus1Sec);
	     try {
				jdbcTemplate.update(INSERT_S_MESSAGGIO, namedParameters);
				return true;
			} 
			catch (Exception e) {
				var methodName = "selectErroreDescFromErroreCod";
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
