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
import org.springframework.dao.IncorrectResultSizeDataAccessException;
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
public class CodTSoggettoDisabilitatoDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String INSERT_SOGGETTO_DISABILITATO =
					"INSERT												          " +
					"	INTO                                                      " +
					"	COD_T_SOGGETTO_DISABILITATO                               " +
					"(SOGGETTO_ID_ABILITATO,                                      " +
					"	SOGGETTO_ID_ABILITANTE,                                   " +
					"	DISABILITAZIONE_MOTIVO_ID,                                " +
					"	DISABILITAZIONE_MOTIVAZIONE,                              " +
					"	ABILITAZIONE_INIZIO,                                      " +
					"	ABILITAZIONE_FINE,                                        " +
					"	DATA_CREAZIONE,                                           " +
					"	DATA_MODIFICA,                                            " +
					"	UTENTE_CREAZIONE,                                         " +
					"	UTENTE_MODIFICA)                                          " +
					"SELECT                                                       " +
					"	SOGGETTO_ID_ABILITATO,                                    " +
					"	SOGGETTO_ID_ABILITANTE,                                   " +
					"	(                                                         " +
					"	SELECT 													  " +
					"		DISABILITAZIONE_MOTIVO_ID                             " +
					"	FROM													  " +
					"		COD_D_DISABILITAZIONE_MOTIVO                          " +
					"	WHERE													  " +
					"		DISABILITAZIONE_MOTIVO_COD = 'REVADE' ),              " +
					"	(                                                         " +
					"	SELECT 													  " +
					"		DISABILITAZIONE_MOTIVO_DESC                           " +
					"	FROM													  " +
					"		COD_D_DISABILITAZIONE_MOTIVO                          " +
					"	WHERE													  " +
					"		DISABILITAZIONE_MOTIVO_COD = 'REVADE' ),			  " +
					"	ABILITAZIONE_INIZIO,                                      " +
					"	:now,                                                     " +
					"	DATA_CREAZIONE,                                           " +
					"	DATA_MODIFICA,                                            " +
					"	UTENTE_CREAZIONE,                                         " +
					"	UTENTE_MODIFICA                                           " +
					"FROM                                                         " +
					"	COD_T_SOGGETTO_ABILITATO                                  " +
					"WHERE                                                        " +
					"	SOGGETTO_ID_ABILITANTE = :soggettoId					  " ;

	public static final String SELECT_MOTIVAZIONE_BLOCCO = 	"SELECT DISABILITAZIONE_MOTIVAZIONE	" +
															"FROM 								" +
															"	COD_T_SOGGETTO_DISABILITATO		" +
															" WHERE	soggetto_id_abilitante=:medicoId " +
															" and SOGGETTO_ID_ABILITATO = :soggId	order by abilitazione_id desc limit 1;";

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int insertSoggettoDisabilitato(TAdesione adesione) throws DatabaseException {


		DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long now = System.currentTimeMillis();
        Date date = new Date(now - 1000l);
        Timestamp sqlTimestamp =  Timestamp.valueOf(df.format(date));

		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("soggettoId",adesione.getSoggettoId())
						.addValue("now",sqlTimestamp);
		try {
			int insert = jdbcTemplate.update(INSERT_SOGGETTO_DISABILITATO, namedParameters);
			return insert;
		}
		catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

public String selectMotivazioneBlocco(long soggettoId, long medicoId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("soggId",soggettoId).addValue("medicoId", medicoId);
		try {
			return jdbcTemplate.queryForMap(SELECT_MOTIVAZIONE_BLOCCO, namedParameters).get("disabilitazione_motivazione")+"";
		}
		catch (IncorrectResultSizeDataAccessException  e) {
			return null;
		}
		catch (Exception e) {
			var methodName = "selectMotivazioneBlocco";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}
