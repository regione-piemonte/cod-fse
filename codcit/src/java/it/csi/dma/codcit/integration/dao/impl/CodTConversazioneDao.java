/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.impl;

import java.sql.Types;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.dto.custom.Conversazione;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodTConversazioneDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("${encryptionkey}")
	private String encryptionkey;

	private static final String SQL_INSERT_T_CONVERSAZIONE = "INSERT INTO cod_t_conversazione "
							+ " (conversazione_cod, conversazione_oggetto, soggetto_id_autore, soggetto_id_partecipante,   data_creazione, utente_creazione, utente_modifica) "
							+ " VALUES(:codiceConversazione, pgp_sym_encrypt_bytea(:oggettoConversazione, :encryptionkey), :idAutore, :idPartecipante,  now(),  :utenteCreazione, :utenteModifica) ";


	private static final String SQL_SELECT_T_CONVERSAZIONE = " SELECT conversazione_id, conversazione_cod,  convert_from(pgp_sym_decrypt_bytea(conversazione_oggetto, :encryptionkey),'UTF8') as conversazione_oggetto, soggetto_id_autore, soggetto_id_partecipante, conversazione_data_blocco, disabilitazione_motivo_id, data_creazione, data_modifica, utente_creazione, utente_modifica "
			+ " FROM cod_t_conversazione where ";

	public long insertTConversazione(Conversazione conversazione) throws DatabaseException{


		KeyHolder keyHolder = new GeneratedKeyHolder();


		/*

		String query="INSERT INTO cod_t_conversazione "
				+ " (conversazione_cod, conversazione_oggetto, soggetto_id_autore, soggetto_id_partecipante,   data_creazione, utente_creazione) "
				+ " VALUES(?,?,?,?,now(),?) ";
	    return jdbcTemplate.execute(query,new PreparedStatementCallback<Boolean>(){
	    @Override
	    public Boolean doInPreparedStatement(PreparedStatement ps)
	            throws SQLException, DataAccessException {

			ps.setObject(1, UUID.randomUUID());
			ps.setString(2, conversazione.getConversazioneOggetto());
			ps.setInt(3, conversazione.getSoggettoIdAutore());
			ps.setInt(4, conversazione.getSoggettoIdPartecipante());
			ps.setString(5, conversazione.getUtente_creazione());

	        return ps.execute();

	    }
	    });
		*/


		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);

		params.addValue("codiceConversazione", UUID.randomUUID().toString(), Types.VARCHAR);
		params.addValue("oggettoConversazione", conversazione.getConversazioneOggetto().getBytes() , Types.BINARY);
		params.addValue("idAutore", conversazione.getSoggettoIdAutore());
		params.addValue("idPartecipante", conversazione.getSoggettoIdPartecipante());
		params.addValue("utenteCreazione", conversazione.getUtente_creazione());
		params.addValue("utenteModifica",  conversazione.getUtente_modifica());

		jdbcTemplate.update(SQL_INSERT_T_CONVERSAZIONE, params, keyHolder, new String[] { "conversazione_id" });
		return keyHolder.getKey().longValue();


	}

	public Conversazione selectConversazioneFromUuid(String uuid) throws DatabaseException{

		String select = SQL_SELECT_T_CONVERSAZIONE+" conversazione_cod = :conversazione_cod";
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey).addValue("conversazione_cod", uuid);
		return selectConversazione(select, namedParameters);
	}


	public Conversazione selectConversazioneFromId(Long id) throws DatabaseException{

		StringBuilder select = new StringBuilder(SQL_SELECT_T_CONVERSAZIONE).append(" conversazione_id = :conversazione_id");
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey).addValue("conversazione_id", id);
		return selectConversazione(select.toString(), namedParameters);
	}

	private Conversazione selectConversazione(String select, SqlParameterSource namedParameters) throws DatabaseException{
		try {
			return jdbcTemplate.queryForObject(select, namedParameters, (rs, rowNum) ->
					new Conversazione(rs.getLong("conversazione_id"), rs.getString("conversazione_cod"), rs.getString("conversazione_oggetto"), rs.getInt("soggetto_id_autore"), rs.getInt("soggetto_id_partecipante"), rs.getTimestamp("conversazione_data_blocco"),
							rs.getInt("disabilitazione_motivo_id"), rs.getTimestamp("data_creazione"), rs.getTimestamp("data_modifica"), rs.getString("utente_creazione"), rs.getString("utente_modifica") ) );

		} catch(EmptyResultDataAccessException e) {
			//TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		}
		catch (Exception e) {
			var methodName = "selectConversazioneFromId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}
