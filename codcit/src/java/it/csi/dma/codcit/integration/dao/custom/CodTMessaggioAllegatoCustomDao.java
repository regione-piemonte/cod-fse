/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import java.sql.Array;
import java.sql.Connection;
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
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.codcit.dto.ModelAutore;
import it.csi.dma.codcit.dto.ModelAutore.TipoEnum;
import it.csi.dma.codcit.dto.ModelCodifica;
import it.csi.dma.codcit.dto.ModelDocumento;
import it.csi.dma.codcit.dto.ModelMessaggio;
import it.csi.dma.codcit.dto.custom.CodTMessaggio;
import it.csi.dma.codcit.dto.custom.Soggetto;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.CodTMessaggioRowMapper;
import it.csi.dma.codcit.integration.dao.utils.ModelMessaggioRowMapper;
import it.csi.dma.codcit.util.LoggerUtil;

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

	private static final String SQL_INSERT_T_ALLEGATO = "INSERT INTO cod_t_allegato "
			+ "(messaggio_id, id_documento_ilec, cod_cl, categoria,codice_azienda_sanitaria, descrizione_azienda_sanitaria, descrizione_struttura, data_validazione, cod_tipo_documento,"
			+ " descr_tipo_documento, tipo_contributo, id_episodio_ilec, codice_documento_dipartimentale, id_repository_cl, accession_number, data_creazione, utente_creazione)"
			+ " VALUES(:messaggio_id, :id_documento_ilec, :cod_cl, :categoria,:codice_azienda_sanitaria, pgp_sym_encrypt_bytea(:descrizione_azienda_sanitaria, :encryptionkey), "
			+ "pgp_sym_encrypt_bytea(:descrizione_struttura, :encryptionkey),  :data_validazione, :cod_tipo_documento, :descr_tipo_documento, :tipo_contributo, :id_episodio_ilec, "
			+ ":codice_documento_dipartimentale, :id_repository_cl,:accession_number, now(),  :utente_creazione)";
	// + ":codice_documento_dipartimentale, :id_repository_cl,
	// ARRAY[:accession_number], now(), :utente_creazione)" ;

	private static final String SQL_SELECT_MESSAGGIO_ALLEGATI = "SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
			+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
			+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
			+ "alleg.allegato_id, alleg.messaggio_id, alleg.id_documento_ilec, alleg.cod_cl, categoria, alleg.codice_azienda_sanitaria, \r\n"
			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_azienda_sanitaria, :encryptionkey), 'UTF8') as descrizione_azienda_sanitaria,\r\n"
			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_struttura, :encryptionkey), 'UTF8') as descrizione_struttura,\r\n"
			+ "alleg.data_validazione, alleg.cod_tipo_documento, alleg.descr_tipo_documento, alleg.tipo_contributo, \r\n"
			+ "alleg.id_episodio_ilec, alleg.codice_documento_dipartimentale, alleg.id_repository_cl, alleg.accession_number, alleg.data_creazione, \r\n"
			+ "alleg.data_cancellazione, alleg.utente_creazione, alleg.utente_cancellazione, \r\n"
			+ "soggAutore.soggetto_cf, "
			+ " case when mess.utente_creazione = soggautore.soggetto_cf then 'ASSISTITO'\r\n"
			+ "when mess.utente_creazione != soggautore.soggetto_cf then 'DELEGATO' else 'MEDICO' end as autore_tipo "
			+ "FROM cod_t_messaggio mess\r\n"
			+ "left outer join cod_t_allegato alleg on (mess.messaggio_id = alleg.messaggio_id)\r\n"
			+ "inner join cod_t_soggetto soggAutore on (mess.soggetto_id_da = soggAutore.soggetto_id ) \r\n"
			+ "where mess.messaggio_id = :messaggio_id";

	private static final String SQL_SELECT_MESSAGGIO = "SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
			+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
			+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
			+ "soggAutore.soggetto_cf " + "FROM cod_t_messaggio mess\r\n"
			+ "inner join cod_t_soggetto soggAutore on (mess.soggetto_id_da = soggAutore.soggetto_id ) \r\n"
			+ "where mess.messaggio_id = :messaggio_id";

//	private static final String SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID =
//			"SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
//			+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
//			+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
//			+ "alleg.allegato_id, alleg.messaggio_id, alleg.id_documento_ilec, alleg.cod_cl, categoria, alleg.codice_azienda_sanitaria, \r\n"
//			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_azienda_sanitaria, :encryptionkey), 'UTF8') as descrizione_azienda_sanitaria,\r\n"
//			+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_struttura, :encryptionkey), 'UTF8') as descrizione_struttura,\r\n"
//			+ "alleg.data_validazione, alleg.cod_tipo_documento, alleg.descr_tipo_documento, alleg.tipo_contributo, \r\n"
//			+ "alleg.id_episodio_ilec, alleg.codice_documento_dipartimentale, alleg.id_repository_cl, alleg.accession_number, alleg.data_creazione, \r\n"
//			+ "alleg.data_cancellazione, alleg.utente_creazione, alleg.utente_cancellazione, \r\n"
//			+ "soggAutore.soggetto_cf, "
//			+ " case when mess.utente_creazione = soggautore.soggetto_cf then 'ASSISTITO'\r\n"
//			+ "when mess.utente_creazione != soggautore.soggetto_cf then 'DELEGATO' else 'MEDICO' end as autore_tipo "
//			+ " FROM cod_t_messaggio mess\r\n"
//			+ "left outer join cod_t_allegato alleg on (mess.messaggio_id = alleg.messaggio_id)\r\n"
//			+ "inner join cod_t_soggetto soggAutore on (mess.soggetto_id_da = soggAutore.soggetto_id ) \r\n"
//			+ "where mess.messaggio_id in (select i.messaggio_id FROM cod_t_messaggio i "
//			+ "where i.conversazione_id = :conversazione_id order by 1 desc OFFSET :offset LIMIT :limit ) order by 1 DESC;";
	private static final String SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID=	"with attori_conv as ( \r\n"
	+ "select convers.soggetto_id_partecipante as id_med, convers.soggetto_id_autore as id_assistito  from cod_t_conversazione convers where convers.conversazione_id=:conversazione_id \r\n"
	+ ")\r\n"
	+ "SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
	+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
	+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
	+ "alleg.allegato_id, alleg.messaggio_id, alleg.id_documento_ilec, alleg.cod_cl, categoria, alleg.codice_azienda_sanitaria, \r\n"
	+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_azienda_sanitaria, :encryptionkey), 'UTF8') as descrizione_azienda_sanitaria,\r\n"
	+ "convert_from(pgp_sym_decrypt_bytea(alleg.descrizione_struttura, :encryptionkey), 'UTF8') as descrizione_struttura,\r\n"
	+ "alleg.data_validazione, alleg.cod_tipo_documento, alleg.descr_tipo_documento, alleg.tipo_contributo, \r\n"
	+ "alleg.id_episodio_ilec, alleg.codice_documento_dipartimentale, alleg.id_repository_cl, alleg.accession_number, alleg.data_creazione, \r\n"
	+ "alleg.data_cancellazione, alleg.utente_creazione, alleg.utente_cancellazione, \r\n"
	+ "soggAutore.soggetto_cf, \r\n"
	+ "case when mess.utente_creazione = medico.soggetto_cf then 'MEDICO'\r\n"
	+ " when mess.utente_creazione = assistito.soggetto_cf then 'ASSISTITO' else 'DELEGATO' end as autore_tipo \r\n"
	+ " FROM cod_t_messaggio mess\r\n"
	+ "join attori_conv on (1=1)\r\n"
	+ "left outer join cod_t_allegato alleg on (mess.messaggio_id = alleg.messaggio_id)\r\n"
	+ "inner join cod_t_soggetto soggAutore on (mess.soggetto_id_da = soggAutore.soggetto_id ) \r\n"
	+ "inner join cod_t_soggetto medico on attori_conv.id_med = medico.soggetto_id \r\n"
	+ "inner join cod_t_soggetto assistito on attori_conv.id_assistito = assistito.soggetto_id \r\n"
	+ "where mess.messaggio_id in (select i.messaggio_id FROM cod_t_messaggio i \r\n"
	+ "where i.conversazione_id = :conversazione_id order by 1 desc OFFSET :offset LIMIT :limit ) order by 1 DESC";



	private static final String SQL_SELECT_MESSAGGIO_BY_ID_CONVERSAZIONE_ID = "SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
			+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
			+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
			+ "sogg.soggetto_cf " + "FROM cod_t_messaggio mess\r\n"
			+ "inner join cod_t_soggetto sogg on (mess.soggetto_id_da = sogg.soggetto_id ) \r\n"
			+ "inner join cod_t_conversazione cod on (cod.conversazione_id = mess.conversazione_id) \r\n"
			+ "where mess.messaggio_id = :messaggio_id and cod.conversazione_cod = :conversazione_cod and sogg.soggetto_cf = :soggetto_cf";

	private static final String SQL_SELECT_MESSAGGIO_BY_ID_CONVERSAZIONE_ID_FOR_LETTO = "SELECT mess.messaggio_id, convert_from(pgp_sym_decrypt_bytea(mess.messaggio_testo_cifrato,:encryptionkey), 'UTF8') as messaggio_testo_cifrato, mess.soggetto_id_da, \r\n"
			+ "mess.soggetto_id_a, mess.messaggio_data_invio, mess.messaggio_lettura_data, mess.messaggio_lettura_da_cf, mess.conversazione_id, \r\n"
			+ "mess.data_creazione, mess.data_modifica, mess.utente_creazione, mess.utente_modifica,\r\n"
			+ "sogg.soggetto_cf " + "FROM cod_t_messaggio mess\r\n"
			+ "inner join cod_t_soggetto sogg on (mess.soggetto_id_da = sogg.soggetto_id ) \r\n"
			+ "inner join cod_t_conversazione cod on (cod.conversazione_id = mess.conversazione_id) \r\n"
			+ "where mess.messaggio_id = :messaggio_id and cod.conversazione_cod = :conversazione_cod and mess.soggetto_id_a=(SELECT soggetto_id FROM cod_t_soggetto where soggetto_cf=:soggetto_cf limit 1);";

	private static final String SQL_INSERT_S_MESSAGGIO = "INSERT INTO cod_s_messaggio\r\n" +
			"(messaggio_id, messaggio_testo_cifrato, soggetto_id_da, soggetto_id_a, messaggio_data_invio, messaggio_lettura_data, messaggio_lettura_da_cf, conversazione_id, data_creazione, \r\n" +
			" data_modifica, utente_creazione, utente_modifica, validita_inizio, validita_fine)\r\n" +
			"VALUES(:messaggio_id, pgp_sym_encrypt_bytea(:messaggioTestoCifrato, :encryptionkey), \r\n" +
		    ":soggetto_id_da, :soggetto_id_a, :messaggio_data_invio, :messaggio_lettura_data, :messaggio_lettura_da_cf, :conversazione_id, :data_creazione, :data_modifica, \r\n"
		    + ":utente_creazione, :utente_modifica, :validita_inizio, now() - interval '1 sec' )";

	private static final String SQL_UPDATE_PATCH_T_MESSAGGIO = "UPDATE cod_t_messaggio SET messaggio_lettura_da_cf= :messaggio_lettura_da_cf, messaggio_lettura_data=now(), \r\n" +
					"utente_modifica= :utente_modifica, data_modifica= now() where messaggio_id = :messaggio_id";

	private static final String SQL_UPDATE_PUT_T_MESSAGGIO = "UPDATE cod_t_messaggio SET messaggio_testo_cifrato=pgp_sym_encrypt(:messaggioTestoCifrato, :encryptionkey), messaggio_lettura_da_cf= null, messaggio_lettura_data=null, " +
			"utente_modifica= :utente_modifica, data_modifica= now() where messaggio_id = :messaggio_id";

	private static final String SQL_DELETE_T_ALLEGATO="DELETE FROM cod_t_allegato WHERE messaggio_id =:messaggio_id";
	//restituisco il numero di record successivi
	private static final String SQL_COUNT_NEXT_MSG="SELECT count(*) FROM cod_t_messaggio where messaggio_id>:messaggio_id and conversazione_id=:conversazione_id;";

	private static final String SQL_GET_ID_MESSAGGIO_MODIFICABILE="SELECT coalesce(max(messaggio_id),-2) FROM cod_t_messaggio where conversazione_id=:conversazione_id;";


	/* eliminare
	 * private static final String BASE_OFFSET = " OFFSET   :offset "; private
	 * static final String BASE_LIMIT = " LIMIT :limit "; private static final
	 * String BASE_FINALE_CHIUSURA = "; ";
	 */

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

	@Transactional
	public long insertTAllegato(ModelDocumento documentoAllegato, Long messaggioId, String cfUtente)
			throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);

		params.addValue("messaggio_id", messaggioId, Types.INTEGER);
		params.addValue("id_documento_ilec", documentoAllegato.getIdDocumentoIlec(), Types.NUMERIC);
		params.addValue("cod_cl", documentoAllegato.getCodiceCl());
		params.addValue("categoria", documentoAllegato.getCategoriaTipologia());

		if (documentoAllegato.getAzienda() != null && documentoAllegato.getAzienda().getCodice()!=null && documentoAllegato.getAzienda().getDescrizione()!=null) {
			params.addValue("codice_azienda_sanitaria", documentoAllegato.getAzienda().getCodice());
			params.addValue("descrizione_azienda_sanitaria", documentoAllegato.getAzienda().getDescrizione().getBytes(),
					Types.BINARY);
		} else {
			params.addValue("codice_azienda_sanitaria", null);
			params.addValue("descrizione_azienda_sanitaria", null);
		}
		params.addValue("descrizione_struttura", documentoAllegato.getDescrizioneStruttura().getBytes(), Types.BINARY);
		if (documentoAllegato.getTipoDocumento() != null) {
			params.addValue("cod_tipo_documento", documentoAllegato.getTipoDocumento().getCodice());
			params.addValue("descr_tipo_documento", documentoAllegato.getTipoDocumento().getDescrizione());
		} else {
			params.addValue("cod_tipo_documento", null);
			params.addValue("descr_tipo_documento", null);
		}
		params.addValue("data_validazione", documentoAllegato.getDataValidazione());
		params.addValue("tipo_contributo", documentoAllegato.getTipoContributo());
		params.addValue("id_episodio_ilec", documentoAllegato.getIdEpisodio(), Types.NUMERIC);
		params.addValue("codice_documento_dipartimentale", documentoAllegato.getCodiceDocumentoDipartimentale());
		params.addValue("id_repository_cl", documentoAllegato.getIdRepositoryCl());
		// TODO verificare come si inserisce
		// jdbcTemplate.getJdbcOperations().getD
		if(documentoAllegato.getAccessionNumbers()!=null && documentoAllegato.getAccessionNumbers().size()>0) {
			//String[] x=(String[])documentoAllegato.getAccessionNumbers().toArray();
			//List<Object> accessionNumbers=Arrays.asList(documentoAllegato.getAccessionNumbers().toArray());
			//String[] x = {"Hello", "World"};
			String[] x =documentoAllegato.getAccessionNumbers().toArray(new String[0]);
			Array array = jdbcTemplate.getJdbcOperations().execute(new ConnectionCallback<Array>() {
	                @Override
	                public Array doInConnection(Connection con) throws SQLException, DataAccessException {
	                    return con.createArrayOf("TEXT", x);
	                }
	            });
			 params.addValue("accession_number",array,Types.ARRAY);
			//params.addValue("accession_number", documentoAllegato.getAccessionNumbers().toArray(),Types.ARRAY);
		}else {
			params.addValue("accession_number",null,Types.ARRAY);
		}
		params.addValue("utente_creazione", cfUtente);

		jdbcTemplate.update(SQL_INSERT_T_ALLEGATO, params, keyHolder, new String[] { "allegato_id" });
		// TODO Verificare come inserire Array
		// jdbcTemplate.get
		// Array numbers = documentoAllegato
		return keyHolder.getKey().longValue();
	}

	// TODO Verificare appena torno come tirare su e raggruppare i dati con 1 select
	// sola
	// da fare usando: namedJdbcTemplate.query(sql.toString(), params, new
	// ResultSetExtractor<List<ModelMessaggio
	public ModelMessaggio selectMessaggioFromIdSelectSingola(Long idMessaggio) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);
		try {
			//mi ricavo l'ultimo messaggio della conversazione
			return jdbcTemplate.query(SQL_SELECT_MESSAGGIO_ALLEGATI, params, new ResultSetExtractor<ModelMessaggio>() {

				@Override
				public ModelMessaggio extractData(ResultSet rs) throws SQLException, DataAccessException {
					int i = 0;
					// List<ModelMessaggio> messaggioAllegati = new ArrayList<ModelMessaggio>();
					ModelMessaggio result = null;
					while (rs.next()) {
						if (i == 0) {
							// creo il modelMEssaggio
							result = new ModelMessaggio();
							result.setId("" + rs.getInt("messaggio_id"));
							result.setTesto(rs.getString("messaggio_testo_cifrato"));
							ModelAutore autore = new ModelAutore();
							//autore.setCodiceFiscale(rs.getString("soggetto_cf"));
							autore.setCodiceFiscale(rs.getString("utente_creazione"));
							autore.setTipo(TipoEnum.valueOf(rs.getString("autore_tipo")));
							result.setAutore(autore);
							result.setDataCreazione(rs.getTimestamp("data_creazione"));
							result.setAutoreModifica(rs.getString("utente_modifica"));
							result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
							result.setLetto(rs.getTimestamp("messaggio_lettura_data")==null?false:true);//se messaggio_lettura_data is null allora non letto
							//non si capisce come calcolarlo
							//result.setModificabile(true);

							result.setModificato(rs.getTimestamp("data_modifica").compareTo(rs.getTimestamp("data_creazione"))!=0?true:false);
							result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
							result.setDataModifica(rs.getTimestamp("data_modifica"));

						}
						// Aggiungo allegati se ci sono
						if (result != null && rs.getInt("allegato_id") > 0) {
							ModelDocumento doc = new ModelDocumento();
							doc.setIdDocumentoIlec(""+rs.getString("id_documento_ilec"));
							doc.setCodiceCl(rs.getString("cod_cl"));
							//verifico se esiste un azienda
							if (StringUtils.isNotEmpty(rs.getString("codice_azienda_sanitaria"))) {
								ModelCodifica modelCodice=new ModelCodifica();
								modelCodice.setCodice(rs.getString("codice_azienda_sanitaria"));
								modelCodice.setDescrizione(rs.getString("descrizione_azienda_sanitaria"));
								doc.setAzienda(modelCodice);
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
							java.sql.Array accessionNumber = rs.getArray("accession_number");
							if(accessionNumber!=null) {
								//String[] str_cities = (String[])accessionNumber.getArray();
								//List<String> accessionNumbers=Arrays.asList(str_cities);
								doc.setAccessionNumbers(Arrays.asList((String[])accessionNumber.getArray()));
							}
							result.getAllegati().add(doc);
						}
						i++;
					}
					// messaggioAllegati.add(result);
					return result;
				}
			});

		} catch (EmptyResultDataAccessException e) {
			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		} catch (Exception e) {
			var methodName = "selectMessaggioFromId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<ModelMessaggio> selectListaMessaggiFromIdConversazione(Long conversazione_id) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("conversazione_id", conversazione_id, Types.INTEGER);
		try {
			//mi ricavo l'ultimo messaggio della conversazione
			long ultimoMessaggio=jdbcTemplate.queryForObject(SQL_GET_ID_MESSAGGIO_MODIFICABILE, params,Long.class);
			return jdbcTemplate.query(SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID, params,
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
									result.setTesto(rs.getString("messaggio_testo_cifrato"));
									ModelAutore autore = new ModelAutore();
									//autore.setCodiceFiscale(rs.getString("soggetto_cf"));
									autore.setCodiceFiscale(rs.getString("utente_modifica"));
									autore.setTipo(TipoEnum.valueOf(rs.getString("autore_tipo")));
									result.setAutore(autore);
									result.setDataCreazione(rs.getTimestamp("data_creazione"));
									// TODO verificare
									// result.setAutoreModifica(rs.getString("soggetto_cf"));
									// result.setLetto(rs.get);
									if(ultimoMessaggio==rs.getLong("messaggio_id") && !autore.getTipo().equals(TipoEnum.MEDICO)) {
										result.setModificabile(true);
									} else {
										result.setModificabile(false);
									}
									// result.setModificato(modificato);
									result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
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
										ModelCodifica modelCodice=new ModelCodifica();
										modelCodice.setCodice(rs.getString("codice_azienda_sanitaria"));
										modelCodice.setDescrizione(rs.getString("descrizione_azienda_sanitaria"));
										doc.setAzienda(modelCodice);
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
									java.sql.Array accessionNumber = rs.getArray("accession_number");
									if(accessionNumber!=null) {
										//String[] str_cities = (String[])accessionNumber.getArray();
										//List<String> accessionNumbers=Arrays.asList(str_cities);
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
			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		} catch (Exception e) {
			var methodName = "selectMessaggioFromId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public List<ModelMessaggio> selectListaMessaggiFromIdConversazioneOffsetLimit(Long conversazione_id,Soggetto soggettoMedico,Integer offset,Integer limit) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("conversazione_id", conversazione_id, Types.INTEGER).addValue("offset", offset).addValue("limit", limit);

		StringBuilder sql = new StringBuilder().append(SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID);


		try {
			//mi ricavo l'ultimo messaggio della conversazione
			long ultimoMessaggio=jdbcTemplate.queryForObject(SQL_GET_ID_MESSAGGIO_MODIFICABILE, params,Long.class);

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
									result.setTesto(rs.getString("messaggio_testo_cifrato"));

									ModelAutore autore = new ModelAutore();
									//autore.setCodiceFiscale(rs.getString("soggetto_cf"));
									autore.setCodiceFiscale(rs.getString("utente_creazione"));
									autore.setTipo(TipoEnum.valueOf(rs.getString("autore_tipo")));
									result.setAutore(autore);
									result.setDataCreazione(rs.getTimestamp("data_creazione"));
									result.setAutoreModifica(rs.getString("utente_modifica"));
									result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
									result.setLetto(rs.getTimestamp("messaggio_lettura_data")==null?false:true);//se messaggio_lettura_data is null allora non letto
									//MODIFCA LETTO 14112022
									Timestamp dataModifica= rs.getTimestamp("data_modifica");
									Timestamp dataLettura =rs.getTimestamp("messaggio_lettura_data");
									if(dataModifica!=null && dataLettura !=null) {
										result.setLetto(dataModifica.after(dataLettura)?false:true);
									}

									
//									if((ultimoMessaggio==rs.getLong("messaggio_id") && !autore.getTipo().equals(TipoEnum.MEDICO))&& !result.isLetto()) {
									if((ultimoMessaggio==rs.getLong("messaggio_id") && !autore.getCodiceFiscale().equalsIgnoreCase(soggettoMedico.getSoggettoCf())) ) {
										result.setModificabile(true);
									} else {
										result.setModificabile(false);
									}
									result.setModificato(rs.getTimestamp("data_modifica").compareTo(rs.getTimestamp("data_creazione"))!=0?true:false);
									result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
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
										ModelCodifica modelCodice=new ModelCodifica();
										modelCodice.setCodice(rs.getString("codice_azienda_sanitaria"));
										modelCodice.setDescrizione(rs.getString("descrizione_azienda_sanitaria"));
										doc.setAzienda(modelCodice);
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
									java.sql.Array accessionNumber = rs.getArray("accession_number");
									if(accessionNumber!=null) {
										//String[] str_cities = (String[])accessionNumber.getArray();
										//List<String> accessionNumbers=Arrays.asList(str_cities);
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
			throw new DatabaseException(e);
		}
	}

	public Integer countListaMessaggiFromIdConversazione(Long conversazione_id) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("conversazione_id", conversazione_id, Types.INTEGER);

		StringBuilder sql = new StringBuilder().append(SQL_SELECT_MESSAGGIO_ALLEGATI_BY_CONVERSAZIONE_ID);

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


	public ModelMessaggio selectMessaggioFromId(Long idMessaggio) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);

		try {
			return jdbcTemplate.queryForObject(SQL_SELECT_MESSAGGIO, params, new ModelMessaggioRowMapper());

		} catch (EmptyResultDataAccessException e) {
			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		} catch (Exception e) {
			var methodName = "selectMessaggioFromId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}


	public CodTMessaggio selectMessaggioFromIdAndCodConversazioneAndSoggetto(String citId, String codConversazione, Long idMessaggio) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);
		params.addValue("conversazione_cod", codConversazione, Types.VARCHAR);
		params.addValue("soggetto_cf", citId, Types.VARCHAR);

		try {
			return jdbcTemplate.queryForObject(SQL_SELECT_MESSAGGIO_BY_ID_CONVERSAZIONE_ID, params, new CodTMessaggioRowMapper());

		} catch (EmptyResultDataAccessException e) {
			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		} catch (Exception e) {
			var methodName = "selectMessaggioFromId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public CodTMessaggio selectMessaggioFromIdAndCodConversazioneAndSoggettoForLetto(String citId, String codConversazione, Long idMessaggio) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);
		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);
		params.addValue("conversazione_cod", codConversazione, Types.VARCHAR);
		params.addValue("soggetto_cf", citId, Types.VARCHAR);
		try {
			return jdbcTemplate.queryForObject(SQL_SELECT_MESSAGGIO_BY_ID_CONVERSAZIONE_ID_FOR_LETTO, params, new CodTMessaggioRowMapper());
		} catch (EmptyResultDataAccessException e) {
			// TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		} catch (Exception e) {
			var methodName = "selectMessaggioFromId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}


	@Transactional
	public Long insertSMessaggio(CodTMessaggio messaggio) throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();

		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);

		params.addValue("messaggio_id",  messaggio.getMessaggioId(), Types.INTEGER);
		params.addValue("messaggioTestoCifrato",  messaggio.getMessaggioTestoCifrato().getBytes(),  Types.BINARY);
		params.addValue("soggetto_id_da", messaggio.getSoggettoIdDa(),  Types.INTEGER);
		params.addValue("soggetto_id_a", messaggio.getSoggettoIdA(),  Types.INTEGER);
		params.addValue("messaggio_data_invio", messaggio.getMessaggioDataInvio(), Types.TIMESTAMP);
		params.addValue("messaggio_lettura_data", messaggio.getMessaggioLetturaData(), Types.TIMESTAMP);
		params.addValue("messaggio_lettura_da_cf", messaggio.getMessaggioLetturaDaCf(), Types.VARCHAR);
		params.addValue("conversazione_id", messaggio.getConversazioneId(), Types.INTEGER);
		params.addValue("data_creazione", messaggio.getDataCreazione(), Types.TIMESTAMP);
		params.addValue("data_modifica", messaggio.getDataModifica(), Types.TIMESTAMP);
		params.addValue("utente_creazione", messaggio.getUtenteCreazione(), Types.VARCHAR);
		params.addValue("utente_modifica", messaggio.getUtenteModifica(), Types.VARCHAR);
		params.addValue("validita_inizio", messaggio.getDataModifica(), Types.TIMESTAMP);

		jdbcTemplate.update(SQL_INSERT_S_MESSAGGIO, params, keyHolder, new String[] { "s_messaggio_id" });
		return keyHolder.getKey().longValue();

	}


	@Transactional
	public int updatePatchTMessaggio(Long idMessaggio, String shibIdentitaCodiceFiscale) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource();


		params.addValue("messaggio_lettura_da_cf",  shibIdentitaCodiceFiscale, Types.VARCHAR);
		params.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
		params.addValue("messaggio_id", idMessaggio, Types.INTEGER);

		return jdbcTemplate.update(SQL_UPDATE_PATCH_T_MESSAGGIO, params);
	}

	@Transactional
	public int updatePutTMessaggio(CodTMessaggio messaggio, String shibIdentitaCodiceFiscale) throws DatabaseException {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("encryptionkey", encryptionkey);

		params.addValue("messaggioTestoCifrato",  messaggio.getMessaggioTestoCifrato());
		params.addValue("messaggio_lettura_da_cf",  shibIdentitaCodiceFiscale, Types.VARCHAR);
		params.addValue("utente_modifica", shibIdentitaCodiceFiscale, Types.VARCHAR);
		params.addValue("messaggio_id", messaggio.getMessaggioId(), Types.INTEGER);
//



		return jdbcTemplate.update(SQL_UPDATE_PUT_T_MESSAGGIO, params);
	}

	@Transactional
	public long deleteTAllegato(Long messaggioId) {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("messaggio_id", messaggioId, Types.INTEGER);
		return jdbcTemplate.update(SQL_DELETE_T_ALLEGATO, params);
	}


	public int countMessaggiSuccessivi(CodTMessaggio messaggio) {
		MapSqlParameterSource params = new MapSqlParameterSource().addValue("messaggio_id", messaggio.getMessaggioId())
				.addValue("conversazione_id", messaggio.getConversazioneId());

		return jdbcTemplate.queryForObject(SQL_COUNT_NEXT_MSG, params, Integer.class).intValue();
	}

}
