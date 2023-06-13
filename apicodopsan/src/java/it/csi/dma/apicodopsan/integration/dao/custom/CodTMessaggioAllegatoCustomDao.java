/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ModelAutore;
import it.csi.dma.apicodopsan.dto.ModelAutore.TipoEnum;
import it.csi.dma.apicodopsan.dto.ModelCodifica;
import it.csi.dma.apicodopsan.dto.ModelDocumento;
import it.csi.dma.apicodopsan.dto.ModelMessaggio;
import it.csi.dma.apicodopsan.dto.ModelMessaggioNuovo;
import it.csi.dma.apicodopsan.dto.custom.CodTMessaggio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodTMessaggioAllegatoCustomDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("${encryptionkey}")
	private String encryptionkey;

	private static final String SQL_INSERT_T_MESSAGGIO = "INSERT INTO cod_t_messaggio "
			+ " (messaggio_testo_cifrato, soggetto_id_da, soggetto_id_a, messaggio_data_invio,  conversazione_id, data_creazione, data_modifica, "
			+ " utente_creazione, utente_modifica) "
			+ " VALUES(pgp_sym_encrypt_bytea(:messaggioTestoCifrato, :encryptionkey), :soggettoIdDa, :soggettoIdA, now(), :conversazioneId, now(), now(), :utenteCreazione, :utenteModifica)";

//	private static final String SQL_INSERT_T_ALLEGATO = "INSERT INTO cod_t_allegato "
//			+ "(messaggio_id, id_documento_ilec, cod_cl,  codice_azienda_sanitaria, descrizione_azienda_sanitaria, descrizione_struttura, data_validazione, cod_tipo_documento,"
//			+ " descr_tipo_documento, tipo_contributo, id_episodio_ilec, codice_documento_dipartimentale, id_repository_cl, accession_number, data_creazione, utente_creazione)"
//			+ " VALUES(:messaggio_id, :id_documento_ilec, :cod_cl, :codice_azienda_sanitaria, pgp_sym_encrypt_bytea(:descrizione_azienda_sanitaria, :encryptionkey), "
//			+ "pgp_sym_encrypt_bytea(:descrizione_struttura, :encryptionkey),  now(), :cod_tipo_documento, :descr_tipo_documento, :tipo_contributo, :id_episodio_ilec, "
//			+ ":codice_documento_dipartimentale, :id_repository_cl,:accession_number, now(),  :utente_creazione)";
//	// + ":codice_documento_dipartimentale, :id_repository_cl,
//	// ARRAY[:accession_number], now(), :utente_creazione)" ;

//	private static final String SQL_SELECT_MESSAGGIO_ALLEGATI = "SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
//			+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
//			+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
//			+ "alleg.allegato_id, alleg.messaggio_id, alleg.id_documento_ilec, alleg.cod_cl, categoria, alleg.codice_azienda_sanitaria, \r\n"
//			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_azienda_sanitaria, :encryptionkey), 'UTF8') as descrizione_azienda_sanitaria,\r\n"
//			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_struttura, :encryptionkey), 'UTF8') as descrizione_struttura,\r\n"
//			+ "alleg.data_validazione, alleg.cod_tipo_documento, alleg.descr_tipo_documento, alleg.tipo_contributo, \r\n"
//			+ "alleg.id_episodio_ilec, alleg.codice_documento_dipartimentale, alleg.id_repository_cl, alleg.accession_number, alleg.data_creazione, \r\n"
//			+ "alleg.data_cancellazione, alleg.utente_creazione, alleg.utente_cancellazione, \r\n"
//			+ "soggAutore.soggetto_cf " + "FROM cod_t_messaggio mess\r\n"
//			+ "left outer join cod_t_allegato alleg on (mess.messaggio_id = alleg.messaggio_id)\r\n"
//			+ "inner join cod_t_soggetto soggAutore on (mess.soggetto_id_da = soggAutore.soggetto_id ) \r\n"
//			+ "where mess.messaggio_id = :messaggio_id";
//
//	private static final String SQL_SELECT_MESSAGGIO = "SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
//			+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
//			+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
//			+ "soggAutore.soggetto_cf " + "FROM cod_t_messaggio mess\r\n"
//			+ "inner join cod_t_soggetto soggAutore on (mess.soggetto_id_da = soggAutore.soggetto_id ) \r\n"
//			+ "where mess.messaggio_id = :messaggio_id";
	private static final String SQL_SELECT_NUOVO_MESSAGGIO_BY_ID="with autore_ultimo_messaggio as ( \r\n"
			+ "	select soggautore.soggetto_is_medico as is_medico,\r\n"
			+ "	mess.messaggio_id as ultimo_messaggio\r\n"
			+ "	from cod_t_messaggio mess\r\n"
			+ "	join cod_t_soggetto soggautore on (mess.soggetto_id_da = soggautore.soggetto_id ) \r\n"
			+ "	where\r\n"
			+ "	mess.conversazione_id = :conversazione_id\r\n"
			+ "	order by mess.messaggio_id desc limit 1 )\r\n"
			+ "select mess.messaggio_id,\r\n"
			+ "mess.data_creazione,\r\n"
			+ "convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as testo,\r\n"
			+ "case when autore_ultimo_messaggio.is_medico is null then 'false' else autore_ultimo_messaggio.is_medico end as modificabile,\r\n"
			+ "case when mess.data_creazione <> mess.data_modifica then 'true' else 'false' end as modificato,\r\n"
			+ "case when mess.data_creazione <> mess.data_modifica then mess.data_modifica end as data_modifica\r\n"
			+ "from cod_t_messaggio mess\r\n"
			+ "left join autore_ultimo_messaggio on mess.messaggio_id = autore_ultimo_messaggio.ultimo_messaggio\r\n"
			+ "where mess.messaggio_id=:messaggioID ;";

//	private static final String SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID="with autore_ultimo_messaggio as ( \r\n"
//			+ "	select soggautore.soggetto_is_medico as is_medico,\r\n"
//			+ "	mess.messaggio_id as ultimo_messaggio\r\n"
//			+ "	from cod_t_messaggio mess\r\n"
//			+ "	join cod_t_soggetto soggautore on (mess.soggetto_id_da = soggautore.soggetto_id ) \r\n"
//			+ "	where\r\n"
//			+ "	mess.conversazione_id = :conversazione_id\r\n"
//			+ "	order by mess.messaggio_id desc limit 1 )\r\n"
//			+ "select mess.messaggio_id,\r\n"
//			+ "mess.data_creazione,\r\n"
//			+ "case when mess.messaggio_lettura_data is null then 'false' else 'true' end as letto,\r\n"
//			+ "mess.messaggio_lettura_data,\r\n"
//			+ "convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as testo,\r\n"
//			+ "case when autore_ultimo_messaggio.is_medico is null then 'false' else autore_ultimo_messaggio.is_medico end as modificabile,\r\n"
//			+ "mess.utente_creazione as autore_cf,\r\n"
//			+ "case \r\n"
//			+ "when mess.utente_creazione = soggautore.soggetto_cf and soggautore.soggetto_is_medico = false then 'ASSISTITO'\r\n"
//			+ "when mess.utente_creazione != soggautore.soggetto_cf and soggautore.soggetto_is_medico = false then 'DELEGATO'\r\n"
//			+ "else 'MEDICO' end as autore_tipo,\r\n"
//			+ "case when mess.data_creazione <> mess.data_modifica then 'true' else 'false' end as modificato,\r\n"
//			+ "case when mess.data_creazione <> mess.data_modifica then mess.utente_modifica end as autore_modifica,\r\n"
//			+ "case when mess.data_creazione <> mess.data_modifica then mess.data_modifica end as data_modifica,\r\n"
//			+ "alleg.allegato_id, \r\n"
//			+ "alleg.id_documento_ilec,\r\n"
//			+ "alleg.cod_cl,\r\n"
//			+ "alleg.codice_azienda_sanitaria, \r\n"
//			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_azienda_sanitaria, :encryptionkey), 'UTF8') as descrizione_azienda_sanitaria,\r\n"
//			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_struttura, :encryptionkey), 'UTF8') as descrizione_struttura,\r\n"
//			+ "alleg.data_validazione,\r\n"
//			+ "alleg.cod_tipo_documento, \r\n"
//			+ "alleg.descr_tipo_documento, \r\n"
//			+ "alleg.tipo_contributo,\r\n"
//			+ "alleg.id_episodio_ilec, \r\n"
//			+ "alleg.codice_documento_dipartimentale,\r\n"
//			+ "alleg.id_repository_cl,\r\n"
//			+ "alleg.accession_number,\r\n"
//			+ "alleg.categoria \r\n"
//			+ "from cod_t_messaggio mess\r\n"
//			+ "join cod_t_soggetto soggautore on (mess.soggetto_id_da = soggautore.soggetto_id ) \r\n"
//			+ "left join autore_ultimo_messaggio on mess.messaggio_id = autore_ultimo_messaggio.ultimo_messaggio\r\n"
//			+ "left outer join cod_t_allegato alleg on (mess.messaggio_id = alleg.messaggio_id and alleg.data_cancellazione is null)\r\n"
//			+ "where mess.messaggio_id in (select i.messaggio_id FROM cod_t_messaggio i "
//            + "where i.conversazione_id = :conversazione_id order by 1 desc OFFSET :offset LIMIT :limit ) order by 1 DESC;";
	private static final String SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID="with attori_conv as \r\n"
+ "( \r\n"
+ "select convers.soggetto_id_partecipante as id_med, convers.soggetto_id_autore as id_assistito  from cod_t_conversazione convers where convers.conversazione_id=:conversazione_id \r\n"
+ "), autore_ultimo_messaggio as ( \r\n"
+ "	select soggautore.soggetto_is_medico as is_medico,\r\n"
+ "	mess.messaggio_id as ultimo_messaggio\r\n"
+ "	from cod_t_messaggio mess\r\n"
+ "	join cod_t_soggetto soggautore on (mess.soggetto_id_da = soggautore.soggetto_id ) \r\n"
+ "	where\r\n"
+ "	mess.conversazione_id = :conversazione_id\r\n"
+ "	order by mess.messaggio_id desc limit 1 )\r\n"
+ "select mess.messaggio_id,\r\n"
+ "mess.data_creazione,\r\n"
+ "case when mess.messaggio_lettura_data is null then 'false' else 'true' end as letto,\r\n"
+ "mess.messaggio_lettura_data,\r\n"
+ "convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as testo,\r\n"
+ "case when autore_ultimo_messaggio.is_medico is null then 'false' else autore_ultimo_messaggio.is_medico end as modificabile,\r\n"
+ "mess.utente_creazione as autore_cf,\r\n"
+ "case when mess.utente_creazione = medico.soggetto_cf then 'MEDICO'\r\n"
+ " when mess.utente_creazione = assistito.soggetto_cf then 'ASSISTITO' else 'DELEGATO' end as autore_tipo ,\r\n"
+ "case when mess.data_creazione <> mess.data_modifica then 'true' else 'false' end as modificato,\r\n"
+ "case when mess.data_creazione <> mess.data_modifica then mess.utente_modifica end as autore_modifica,\r\n"
+ "case when mess.data_creazione <> mess.data_modifica then mess.data_modifica end as data_modifica,\r\n"
+ "alleg.allegato_id, \r\n"
+ "alleg.id_documento_ilec,\r\n"
+ "alleg.cod_cl,\r\n"
+ "alleg.codice_azienda_sanitaria, \r\n"
+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_azienda_sanitaria, :encryptionkey), 'UTF8') as descrizione_azienda_sanitaria,\r\n"
+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_struttura, :encryptionkey), 'UTF8') as descrizione_struttura,\r\n"
+ "alleg.data_validazione,\r\n"
+ "alleg.cod_tipo_documento, \r\n"
+ "alleg.descr_tipo_documento, \r\n"
+ "alleg.tipo_contributo,\r\n"
+ "alleg.id_episodio_ilec, \r\n"
+ "alleg.codice_documento_dipartimentale,\r\n"
+ "alleg.id_repository_cl,\r\n"
+ "alleg.accession_number,\r\n"
+ "alleg.categoria \r\n"
+ "from cod_t_messaggio mess\r\n"
+ "join attori_conv on (1=1)\r\n"
+ "join cod_t_soggetto soggautore on (mess.soggetto_id_da = soggautore.soggetto_id ) \r\n"
+ "left join autore_ultimo_messaggio on mess.messaggio_id = autore_ultimo_messaggio.ultimo_messaggio\r\n"
+ "left outer join cod_t_allegato alleg on (mess.messaggio_id = alleg.messaggio_id and alleg.data_cancellazione is null)\r\n"
+ "inner join cod_t_soggetto medico on attori_conv.id_med = medico.soggetto_id \r\n"
+ "inner join cod_t_soggetto assistito on attori_conv.id_assistito = assistito.soggetto_id \r\n"
+ "where mess.messaggio_id in (select i.messaggio_id FROM cod_t_messaggio i \r\n"
+ "where i.conversazione_id = :conversazione_id order by 1 desc OFFSET :offset LIMIT :limit ) order by 1 DESC;";

	private static final String SQL_CHECK_MESSAGGIO_ESISTENTE = "select count(m.*) from cod_t_messaggio m where m.messaggio_id =:messaggioId";
	
	private static final String SQL_CHECK_MESSAGGIO_PROPRIETARIO_MODIFICABILE="	with autore_ultimo_messaggio as ( \r\n"
			+ "		select soggautore.soggetto_is_medico as is_medico,\r\n"
			+ "		mess.messaggio_id as ultimo_messaggio\r\n"
			+ "		from cod_t_messaggio mess\r\n"
			+ "		join cod_t_soggetto soggautore on (mess.soggetto_id_da = soggautore.soggetto_id ) \r\n"
			+ "		where\r\n"
			+ "		mess.conversazione_id = :conversazione_id\r\n"
			+ "		order by mess.messaggio_id desc limit 1 )\r\n"
			+ "	select mess.messaggio_id,\r\n"
			+ "	mess.data_creazione,\r\n"
			+ "	convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as testo,\r\n"
			+ "	case when autore_ultimo_messaggio.is_medico is null then 'false' else autore_ultimo_messaggio.is_medico end as modificabile,\r\n"
			+ "	case when mess.data_creazione <> mess.data_modifica then 'true' else 'false' end as modificato,\r\n"
			+ "	case when mess.data_creazione <> mess.data_modifica then mess.data_modifica end as data_modifica\r\n"
			+ "	from cod_t_messaggio mess\r\n"
			+ "	left join autore_ultimo_messaggio on mess.messaggio_id = autore_ultimo_messaggio.ultimo_messaggio\r\n"
			+ "	join cod_t_soggetto sog on (mess.soggetto_id_da = sog.soggetto_id ) \r\n"
			+ "	where mess.messaggio_id=:messaggioId and sog.soggetto_cf=:soggettoCF ;";
	
	private static final String SQL_UPDATE_T_MESSAGGIO = "UPDATE cod_t_messaggio SET "+
	"utente_modifica= :utente_modifica, data_modifica= now(),messaggio_testo_cifrato=(pgp_sym_encrypt(:messaggioTestoCifrato, :encryptionkey)) where messaggio_id = :messaggio_id";
	
	private static final String SQL_VERIFICA_ALLEGATO = "SELECT count(*) \r\n" + 
			"FROM cod_t_allegato alleg\r\n" + 
			"inner join cod_t_messaggio mess on (alleg.messaggio_id  = mess.messaggio_id )\r\n" + 
			"inner join cod_t_soggetto soggettoCittadino on (mess.soggetto_id_da = soggettoCittadino.soggetto_id) \r\n" + 
			"inner join cod_t_soggetto soggettoMedico on (mess.soggetto_id_a = soggettoMedico.soggetto_id )\r\n" + 
			"where  exists \r\n" + 
			"	(select 1 from cod_t_soggetto_abilitato ctsa  \r\n" + 
			"		inner join cod_t_soggetto sogg on (ctsa.soggetto_id_abilitante = sogg.soggetto_id )\r\n" + 
			"		where ctsa.soggetto_id_abilitato = soggettoCittadino.soggetto_id  \r\n" + 
			"		and sogg.soggetto_cf = :medicoCF and sogg.soggetto_is_medico is true)\r\n" + 
			"and \r\n" + 
//			"soggettoCittadino.soggetto_is_medico is false \r\n" + 
			"and soggettoCittadino.soggetto_cf = :soggettoCF\r\n" + 
			"and soggettoMedico.soggetto_cf = :medicoCF \r\n" + 
			"and soggettoMedico.soggetto_is_medico is true\r\n" + 
			"and alleg.cod_cl = :codCL \r\n" + 
			"and alleg.id_documento_ilec = :idDocumentoIlec;";
	
	private static final String BASE_OFFSET = " OFFSET   :offset ";
	private static final String BASE_LIMIT = " LIMIT :limit ";
	private static final String BASE_FINALE_CHIUSURA = "; ";
	private static final String SQL_COUNT_MESSAGGI_BY_CONVERSAZIONE_ID = " SELECT count(*) FROM cod_t_messaggio mess where mess.conversazione_id = :conversazione_id ";

	@Transactional
	public long insertTMessaggio(CodTMessaggio messaggio) throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);

		params.addValue("messaggioTestoCifrato", messaggio.getMessaggioTestoCifrato().getBytes(), Types.BINARY);
		params.addValue("soggettoIdDa", messaggio.getSoggettoIdDa(), Types.INTEGER);
		params.addValue("soggettoIdA", messaggio.getSoggettoIdA(), Types.INTEGER);
		params.addValue("conversazioneId", messaggio.getConversazioneId(), Types.INTEGER);
		params.addValue("utenteCreazione", messaggio.getUtenteCreazione(), Types.VARCHAR);
		params.addValue("utenteModifica", messaggio.getUtenteModifica());

		jdbcTemplate.update(SQL_INSERT_T_MESSAGGIO, params, keyHolder, new String[] { "messaggio_id" });
		return keyHolder.getKey().longValue();

	}

	public Integer checkMessaggioEsistente(String messaggioId) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("messaggioId", messaggioId  ,Types.INTEGER);
		
		try {
			return jdbcTemplate.queryForObject(SQL_CHECK_MESSAGGIO_ESISTENTE, params, Integer.class);

		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			var methodName = "checkMessaggioEsistente";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	
//	@Transactional
//	public long insertTAllegato(ModelDocumento documentoAllegato, Long messaggioId, String cfUtente)
//			throws DatabaseException {
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
//
//		params.addValue("messaggio_id", messaggioId, Types.INTEGER);
//		params.addValue("id_documento_ilec", documentoAllegato.getIdDocumentoIlec(), Types.NUMERIC);
//		params.addValue("cod_cl", documentoAllegato.getCodiceCl());
//		if (documentoAllegato.getAzienda() != null) {
//			params.addValue("codice_azienda_sanitaria", documentoAllegato.getAzienda().getCodice());
//			params.addValue("descrizione_azienda_sanitaria", documentoAllegato.getAzienda().getDescrizione().getBytes(),
//					Types.BINARY);
//		} else {
//			params.addValue("codice_azienda_sanitaria", null);
//			params.addValue("descrizione_azienda_sanitaria", null);
//		}
//		params.addValue("descrizione_struttura", documentoAllegato.getDescrizioneStruttura().getBytes(), Types.BINARY);
//		if (documentoAllegato.getTipoDocumento() != null) {
//			params.addValue("cod_tipo_documento", documentoAllegato.getTipoDocumento().getCodice());
//			params.addValue("descr_tipo_documento", documentoAllegato.getTipoDocumento().getDescrizione());
//		} else {
//			params.addValue("cod_tipo_documento", null);
//			params.addValue("descr_tipo_documento", null);
//		}
//		params.addValue("tipo_contributo", documentoAllegato.getTipoContributo());
//		params.addValue("id_episodio_ilec", documentoAllegato.getIdEpisodio(), Types.NUMERIC);
//		params.addValue("codice_documento_dipartimentale", documentoAllegato.getCodiceDocumentoDipartimentale());
//		params.addValue("id_repository_cl", documentoAllegato.getIdRepositoryCl());
//		// TODO verificare come si inserisce
//		// jdbcTemplate.getJdbcOperations().getD
//		if(documentoAllegato.getAccessionNumbers()!=null && documentoAllegato.getAccessionNumbers().size()>0) {
//			//String[] x=(String[])documentoAllegato.getAccessionNumbers().toArray();
//			//List<Object> accessionNumbers=Arrays.asList(documentoAllegato.getAccessionNumbers().toArray());
//			//String[] x = {"Hello", "World"};
//			String[] x =documentoAllegato.getAccessionNumbers().toArray(new String[0]);
//			Array array = jdbcTemplate.getJdbcOperations().execute(new ConnectionCallback<Array>() {
//	                @Override
//	                public Array doInConnection(Connection con) throws SQLException, DataAccessException {
//	                    return con.createArrayOf("TEXT", x);
//	                }
//	            });
//			 params.addValue("accession_number",array,Types.ARRAY);
//			//params.addValue("accession_number", documentoAllegato.getAccessionNumbers().toArray(),Types.ARRAY);
//		}else {
//			params.addValue("accession_number",null,Types.ARRAY);
//		}
//		params.addValue("utente_creazione", cfUtente);
//
//		jdbcTemplate.update(SQL_INSERT_T_ALLEGATO, params, keyHolder, new String[] { "allegato_id" });
//		// TODO Verificare come inserire Array
//		// jdbcTemplate.get
//		// Array numbers = documentoAllegato
//		return keyHolder.getKey().longValue();
//	}
//
	// ESTRAZIONE SINGOLO MESSAGGIO POST CREAZIONE
	public ModelMessaggioNuovo selectMessaggioFromIdSelectSingola(Long idMessaggio,Long conversazioneId) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("messaggioID", idMessaggio, Types.INTEGER).addValue("conversazione_id", conversazioneId);
		try {

			return jdbcTemplate.query(SQL_SELECT_NUOVO_MESSAGGIO_BY_ID, params, new ResultSetExtractor<ModelMessaggioNuovo>() {

				@Override
				public ModelMessaggioNuovo extractData(ResultSet rs) throws SQLException, DataAccessException {
					int i = 0;
					// List<ModelMessaggio> messaggioAllegati = new ArrayList<ModelMessaggio>();
					ModelMessaggioNuovo result = null;
					while (rs.next()) {
							// creo il modelMEssaggio
							result = new ModelMessaggioNuovo();
							result.setId("" + rs.getInt("messaggio_id"));
							result.setDataCreazione(rs.getTimestamp("data_creazione"));
							result.setTesto(rs.getString("testo"));
							result.setModificabile(rs.getBoolean("modificabile"));
							result.setModificato(rs.getBoolean("modificato"));
							result.setDataModifica(rs.getTimestamp("data_modifica"));
					}
					return result;
				}
			});

		} catch (EmptyResultDataAccessException e) {
			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return  new ModelMessaggioNuovo();
		} catch (Exception e) {
			var methodName = "selectMessaggioFromIdSelectSingola";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}
//
//	public List<ModelMessaggio> selectListaMessaggiFromIdConversazione(Long conversazione_id) throws DatabaseException {
//		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
//		params.addValue("conversazione_id", conversazione_id, Types.INTEGER);
//		try {
//			return jdbcTemplate.query(SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID, params,
//					new ResultSetExtractor<List<ModelMessaggio>>() {
//
//						@Override
//						public List<ModelMessaggio> extractData(ResultSet rs) throws SQLException, DataAccessException {
//							int msgid = -1;
//							List<ModelMessaggio> messaggioAllegati = new ArrayList<ModelMessaggio>();
//							ModelMessaggio result = null;
//							while (rs.next()) {
//								if (msgid != rs.getInt("messaggio_id")) {
//									msgid = rs.getInt("messaggio_id");
//									// creo il modelMEssaggio
//									result = new ModelMessaggio();
//									result.setId("" + rs.getInt("messaggio_id"));
//									result.setTesto(rs.getString("messaggio_testo_cifrato"));
//									ModelAutore autore = new ModelAutore();
//									autore.setCodiceFiscale(rs.getString("soggetto_cf"));
//									autore.setTipo(TipoEnum.ASSISTITO);
//									result.setAutore(autore);
//									result.setDataCreazione(rs.getTimestamp("data_creazione"));
//									// TODO verificare
//									// result.setAutoreModifica(rs.getString("soggetto_cf"));
//									// result.setLetto(rs.get);
//									// result.setModificabile(modificabile);
//									// result.setModificato(modificato);
//									result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
//									result.setDataModifica(rs.getTimestamp("data_modifica"));
//
//									messaggioAllegati.add(result);
//								}
//								// Aggiungo allegati se ci sono
//								if (result != null && rs.getInt("allegato_id") > 0) {
//									ModelDocumento doc = new ModelDocumento();
//									doc.setIdDocumentoIlec(""+rs.getString("id_documento_ilec"));
//									doc.setCodiceCl(rs.getString("cod_cl"));
//									//verifico se esiste un azienda
//									if (StringUtils.isNotEmpty(rs.getString("codice_azienda_sanitaria"))) {
//										ModelCodifica modelCodice=new ModelCodifica();
//										modelCodice.setCodice(rs.getString("codice_azienda_sanitaria"));
//										modelCodice.setDescrizione(rs.getString("descrizione_azienda_sanitaria"));
//										doc.setAzienda(modelCodice);
//									}
//									doc.setDescrizioneStruttura(rs.getString("descrizione_struttura"));
//									doc.setDataValidazione(new Date(rs.getTimestamp("data_validazione").getTime()));
//									//verifico se esiste tipo_documento
//									if(StringUtils.isNotEmpty(rs.getString("cod_tipo_documento"))) {
//										ModelCodifica tipoDocumento=new ModelCodifica();
//										tipoDocumento.setCodice(rs.getString("cod_tipo_documento"));
//										tipoDocumento.setDescrizione(rs.getString("descr_tipo_documento"));
//										doc.setTipoDocumento(tipoDocumento);
//									}
//									doc.setTipoContributo(rs.getString("tipo_contributo"));
//									doc.setIdEpisodio(rs.getBigDecimal("id_episodio_ilec"));
//									doc.setCodiceDocumentoDipartimentale(rs.getString("codice_documento_dipartimentale"));
//									doc.setIdRepositoryCl(rs.getString("id_repository_cl"));
//									doc.setCategoriaTipologia("categoria");
//									java.sql.Array accessionNumber = rs.getArray("accession_number");
//									if(accessionNumber!=null) {
//										//String[] str_cities = (String[])accessionNumber.getArray();
//										//List<String> accessionNumbers=Arrays.asList(str_cities);
//										doc.setAccessionNumbers(Arrays.asList((String[])accessionNumber.getArray()));
//									}
//									result.getAllegati().add(doc);
//								}
//							}
//							// fine loop
//							return messaggioAllegati;
//						}
//					});
//
//		} catch (EmptyResultDataAccessException e) {
//			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
//			return null;
//		} catch (Exception e) {
//			var methodName = "selectMessaggioFromId";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//	}

	public List<ModelMessaggio> selectListaMessaggiFromIdConversazioneOffsetLimit(Long conversazione_id,Integer offset,Integer limit) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("conversazione_id", conversazione_id, Types.INTEGER).addValue("offset", offset , Types.INTEGER).addValue("limit", limit, Types.INTEGER);

		StringBuilder sql = new StringBuilder().append(SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID);

		try {
			return jdbcTemplate.query(sql.toString(), params,
					new ResultSetExtractor<List<ModelMessaggio>>() {

						@Override
						public List<ModelMessaggio> extractData(ResultSet rs) throws SQLException, DataAccessException {
							int msgid = -1;
							List<ModelMessaggio> messaggioAllegati = new ArrayList<ModelMessaggio>();
							ModelMessaggio result = null;
							while (rs.next()) {
								if (msgid != rs.getInt("messaggio_id")) {
									msgid = rs.getInt("messaggio_id");
									// creo il modelMEssaggio
									result = new ModelMessaggio();
									result.setId("" + rs.getInt("messaggio_id"));
									result.setDataCreazione(rs.getTimestamp("data_creazione"));
									result.setLetto(rs.getBoolean("letto"));
									//MODIFCA LETTO 14112022
									Timestamp dataModifica= rs.getTimestamp("data_modifica");
									Timestamp dataLettura =rs.getTimestamp("messaggio_lettura_data");
									if(dataModifica!=null && dataLettura !=null) {
										result.setLetto(dataModifica.after(dataLettura)?false:true);
									}
									
									
									result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
									result.setTesto(rs.getString("testo"));
									result.setModificabile(rs.getBoolean("modificabile"));
									//model autore
									ModelAutore autore = new ModelAutore();
									autore.setCodiceFiscale(rs.getString("autore_cf"));
									autore.setTipo(TipoEnum.valueOf(rs.getString("autore_tipo").toUpperCase()));
									
									result.setAutore(autore);
									result.setModificato(rs.getBoolean("modificato"));
									result.setAutoreModifica(rs.getString("autore_modifica"));
									result.setDataModifica(rs.getTimestamp("data_modifica"));
								

									messaggioAllegati.add(result);
								}
								// Aggiungo allegati se ci sono
								if (result != null && rs.getInt("allegato_id") > 0) {
									ModelDocumento doc = new ModelDocumento();
									doc.setIdDocumentoIlec(""+rs.getString("id_documento_ilec"));
									doc.setCodiceCl(rs.getString("cod_cl"));
									//verifico se esiste un azienda
									if (StringUtils.isNotEmpty(rs.getString("codice_azienda_sanitaria"))) {
										ModelCodifica modelCodificaAzienda=new ModelCodifica();
										modelCodificaAzienda.setCodice(rs.getString("codice_azienda_sanitaria"));
										modelCodificaAzienda.setDescrizione(rs.getString("descrizione_azienda_sanitaria"));
										doc.setAzienda(modelCodificaAzienda);
									}
									doc.setDescrizioneStruttura(rs.getString("descrizione_struttura"));
									doc.setDataValidazione(new Date(rs.getTimestamp("data_validazione").getTime()));
									//verifico se esiste tipo_documento
									if(StringUtils.isNotEmpty(rs.getString("cod_tipo_documento"))) {
										ModelCodifica tipoDocumento=new ModelCodifica();
										tipoDocumento.setCodice(rs.getString("cod_tipo_documento"));
										tipoDocumento.setDescrizione(rs.getString("descr_tipo_documento"));
										doc.setTipoDocumento(tipoDocumento);
									}
									doc.setTipoContributo(rs.getString("tipo_contributo"));
									doc.setIdEpisodio(rs.getBigDecimal("id_episodio_ilec"));
									doc.setCodiceDocumentoDipartimentale(rs.getString("codice_documento_dipartimentale"));
									doc.setIdRepositoryCl(rs.getString("id_repository_cl"));
									doc.setCategoriaTipologia(rs.getString("categoria"));
//									doc.setCategoriaTipologia("categoria");
									java.sql.Array accessionNumber = rs.getArray("accession_number");
									if(accessionNumber!=null) {
										doc.setAccessionNumbers(Arrays.asList((String[])accessionNumber.getArray()));
									}
									result.getAllegati().add(doc);
								}
							}
							// fine loop
							return messaggioAllegati;
						}
					});

		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<ModelMessaggio>();
		} catch (Exception e) {
			var methodName = "selectListaMessaggiFromIdConversazioneOffsetLimit";
			logError(methodName, e.getMessage());
			e.printStackTrace();
			throw new DatabaseException(e);
		}
	}

	public Integer countListaMessaggiFromIdConversazione(Long conversazione_id) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("conversazione_id", conversazione_id, Types.INTEGER);

		try {
			return jdbcTemplate.queryForObject(SQL_COUNT_MESSAGGI_BY_CONVERSAZIONE_ID, params, Integer.class);

		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			var methodName = "countListaMessaggiFromIdConversazione";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
//
//
//	public ModelMessaggio selectMessaggioFromId(Long idMessaggio) throws DatabaseException {
//		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
//		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);
//
//		try {
//			return jdbcTemplate.queryForObject(SQL_SELECT_MESSAGGIO, params, new ModelMessaggioRowMapper());
//
//		} catch (EmptyResultDataAccessException e) {
//			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
//			return null;
//		} catch (Exception e) {
//			var methodName = "selectMessaggioFromId";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//	}
//
//
//	public CodTMessaggio selectMessaggioFromIdAndCodConversazioneAndSoggetto(String citId, String codConversazione, Long idMessaggio) throws DatabaseException {
//		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
//		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);
//		params.addValue("conversazione_cod", codConversazione, Types.VARCHAR);
//		params.addValue("soggetto_cf", citId, Types.VARCHAR);
//
//		try {
//			return jdbcTemplate.queryForObject(SQL_SELECT_MESSAGGIO_BY_ID_CONVERSAZIONE_ID, params, new CodTMessaggioRowMapper());
//
//		} catch (EmptyResultDataAccessException e) {
//			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
//			return null;
//		} catch (Exception e) {
//			var methodName = "selectMessaggioFromId";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//	}
//
//
//	@Transactional
//	public Long insertSMessaggio(CodTMessaggio messaggio) throws DatabaseException {
//		KeyHolder keyHolder = new GeneratedKeyHolder();
//
//		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
//
//		params.addValue("messaggio_id",  messaggio.getMessaggioId(), Types.INTEGER);
//		params.addValue("messaggioTestoCifrato",  messaggio.getMessaggioTestoCifrato().getBytes(),  Types.BINARY);
//		params.addValue("soggetto_id_da", messaggio.getSoggettoIdDa(),  Types.INTEGER);
//		params.addValue("soggetto_id_a", messaggio.getSoggettoIdA(),  Types.INTEGER);
//		params.addValue("messaggio_data_invio", messaggio.getMessaggioDataInvio(), Types.TIMESTAMP);
//		params.addValue("messaggio_lettura_data", messaggio.getMessaggioLetturaData(), Types.TIMESTAMP);
//		params.addValue("messaggio_lettura_da_cf", messaggio.getMessaggioLetturaDaCf(), Types.VARCHAR);
//		params.addValue("conversazione_id", messaggio.getConversazioneId(), Types.INTEGER);
//		params.addValue("data_creazione", messaggio.getDataCreazione(), Types.TIMESTAMP);
//		params.addValue("data_modifica", messaggio.getDataModifica(), Types.TIMESTAMP);
//		params.addValue("utente_creazione", messaggio.getUtenteCreazione(), Types.VARCHAR);
//		params.addValue("utente_modifica", messaggio.getUtenteModifica(), Types.VARCHAR);
//		params.addValue("validita_inizio", messaggio.getDataModifica(), Types.TIMESTAMP);
//
//		jdbcTemplate.update(SQL_INSERT_S_MESSAGGIO, params, keyHolder, new String[] { "s_messaggio_id" });
//		return keyHolder.getKey().longValue();
//
//	}
//
//
//	@Transactional
//	public int updatePatchTMessaggio(Long idMessaggio, String shibIdentitaCodiceFiscale) throws DatabaseException {
//		MapSqlParameterSource params = new MapSqlParameterSource();
//
//
//		params.addValue("messaggio_lettura_da_cf",  shibIdentitaCodiceFiscale, Types.VARCHAR);
//		params.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
//		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);
//
//		return jdbcTemplate.update(SQL_UPDATE_PATCH_T_MESSAGGIO, params);
//	}
	
	public ModelMessaggioNuovo checkMessaggioProprietario(String messaggioId, String soggettoCF,Long conversazione_id) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("messaggioId", messaggioId, Types.INTEGER).addValue("soggettoCF", soggettoCF).addValue("conversazione_id", conversazione_id);;
		try {
			System.out.println(params.toString());
			return jdbcTemplate.query(SQL_CHECK_MESSAGGIO_PROPRIETARIO_MODIFICABILE, params, new ResultSetExtractor<ModelMessaggioNuovo>() {

				@Override
				public ModelMessaggioNuovo extractData(ResultSet rs) throws SQLException, DataAccessException {
					int i = 0;
					// List<ModelMessaggio> messaggioAllegati = new ArrayList<ModelMessaggio>();
					ModelMessaggioNuovo result = null;
					while (rs.next()) {
							// creo il modelMEssaggio
							result = new ModelMessaggioNuovo();
							result.setId("" + rs.getInt("messaggio_id"));
							result.setDataCreazione(rs.getTimestamp("data_creazione"));
							result.setTesto(rs.getString("testo"));
							result.setModificabile(rs.getBoolean("modificabile"));
							result.setModificato(rs.getBoolean("modificato"));
							result.setDataModifica(rs.getTimestamp("data_modifica"));
					}
					return result;
				}
			});
		} catch (EmptyResultDataAccessException e) {
			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		} catch (Exception e) {
			var methodName = "checkMessaggioProprietario";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	public Integer checkVerificaAllegato(String cfMedico, String cfCittadino, Integer idDocumento, String codCl) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();
		
		params.addValue("medicoCF", cfMedico);
		params.addValue("soggettoCF", cfCittadino);
		params.addValue("idDocumentoIlec", idDocumento  ,Types.INTEGER);
		params.addValue("codCL", codCl);
		
		try {
			return jdbcTemplate.queryForObject(SQL_VERIFICA_ALLEGATO, params, Integer.class);

		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			var methodName = "checkVerificaAllegato";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
	
	
	@Transactional
	public int updateTMessaggioMedico(Long idMessaggio, String shibIdentitaCodiceFiscale,String messaggioTestoCifrato) throws DatabaseException {
		
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("encryptionkey", encryptionkey, Types.VARCHAR)
				.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR)
				.addValue("messaggio_id", idMessaggio, Types.INTEGER).addValue("messaggioTestoCifrato", messaggioTestoCifrato, Types.VARCHAR);

		return jdbcTemplate.update(SQL_UPDATE_T_MESSAGGIO, params);
	}

}
