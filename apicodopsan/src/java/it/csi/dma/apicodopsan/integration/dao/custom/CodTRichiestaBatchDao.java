/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.ModelAssistito;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazione;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazioneTotale;
import it.csi.dma.apicodopsan.dto.custom.ModelAssistitoCustom;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.Constants;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodTRichiestaBatchDao extends LoggerUtil {
//	DUBBI
	private static final String SELECT_COUNT_RICHIESTE_PER_MEDICO="select count(ctrb.*) "
			+ "from cod_t_richiesta_batch ctrb "
			+ "where ctrb.batchric_richiedente =:cfMedico ";
	private static final String SELECT_COUNT_RICHIESTE_ATTIVE_PER_MEDICO="select count(ctrb.*) "
			+ "from cod_t_richiesta_batch ctrb "
			+ "where ctrb.batchric_richiedente =:cfMedico and ctrb.validita_fine is null";

	private static final String SELECT_COUNT_RICHIESTE_CON_ESECUZIONI_PER_MEDICO="	select count(ctrb.*) "
			+ "	from cod_t_richiesta_batch ctrb "
			+ "	join cod_l_batch_esecuzione clbe on ctrb.batchric_id =clbe.batchric_id  "
			+ "	where ctrb.batchric_richiedente =:cfMedico ";
	/**
	 * Se la insert riesce vuol dire che possiamo continuare con l'operazione richiesta
	 */
	private static final String INSERT_SEMAFORO_RICHIESTA_BATCH ="INSERT INTO "
			+ "  cod_t_richiesta_batch "
			+ "( "
			+ "  batchric_richiedente, "
			+ "  batch_id, "
			+ "  batchparam_id, "
			+ "  batchparam_value, "
			+ "  batchric_motivazione, "
			+ "  utente_creazione "
			+ ") "
			+ "select :in_cf_medico , a.batch_id, b.batchparam_id,  :in_batchparam_value, :in_motivazione, :in_cf_medico  "
			+ "from cod_d_batch a, cod_d_batch_parametro b "
			+ "where "
			+ "a.batch_id=b.batch_id and "
			+ "a.batch_cod=:in_codice_batch "
			+ "and b.batchparam_cod=:in_batchparam_cod "
			+ "and "
			+ "not exists (select 1 from cod_t_richiesta_batch where batchric_richiedente = :in_cf_medico and validita_fine is null) "
			+ "returning batchric_id;";

	private static final String REMOVE_LOCK_FORCED="UPDATE cod_t_richiesta_batch SET validita_fine=now() WHERE batchric_id=:batchric_id";
	/**
	 * La function restituisce una struttura dati di tipo TABLE(cf text, stato text)
	 */
	private static final String CALL_FUNCTION_ABILITA="SELECT * FROM fnc_cod_abilita(:in_codice_batch, :in_batchparam, :in_elenco_cf, :in_cf_medico);";
	//private static final String CALL_FUNCTION_ABILITA="SELECT * FROM cod.fnc_cod_abilita('ABI_SING_ASS', 'assistiti',  'CRCLRD56M27L219N,AAAA', 'AAAAAA00A11C000K');";

	private static final String CALL_FUNCTION_DISABILITA="SELECT * FROM fnc_cod_disabilita(:in_codice_batch, :in_batchparam, :in_elenco_cf, :in_cf_medico, :in_motivazione);";

	private static final String SELECT_ASSISTITI_DISABILITABILI="SELECT t.soggetto_cf "
			+ " FROM cod_t_soggetto_abilitato a,cod_t_soggetto s, cod_t_soggetto t "
			+ " where a.soggetto_id_abilitante = s.soggetto_id and "
			+ "   s.soggetto_cf = :cfMedico and t.soggetto_id = a.soggetto_id_abilitato";
	//for info()
	//private static final String CURRENT_USER="SELECT CURRENT_USER";
	private static final String CURRENT_SEARCH_PATH="show search_path";
	//private static final String TEST_CRYPT="select pgp_sym_encrypt_bytea(a.conversazione_oggetto, 'mypass')::TEXT from cod_t_conversazione a limit 1";

//	private static final String SELECT_COUNT_RICHIESTE_CON_ESECUZIONI_IN_ERRORE_PER_MEDICO="select count(ctrb.*) "
//			+ "from cod_t_richiesta_batch ctrb "
//			+ "join cod_l_batch_esecuzione clbe on ctrb.batchric_id =clbe.batchric_id  "
//			+ "join cod_d_batch_stato cdbs on clbe.batchesecstato_id =cdbs.batchesecstato_id  "
//			+ "where ctrb.batchric_richiedente =:cfMedico "
//			+ "and cdbs.batchesecstato_cod =:statoErrore ;";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Autowired
	CodLBatchEsecuzioneDao codLBatchEsecuzioneDao;

	public String info() throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource();
						//.addValue("cfMedico",cfMedico);
		try {
			String res = jdbcTemplate.queryForObject(CURRENT_SEARCH_PATH, namedParameters,String.class);
			return res;
		}
		catch (Exception e) {
			var methodName = "countRichiesteMedico";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public List<String> selectSoggettiDisabilitabili(String cfMedico) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("cfMedico",cfMedico);
		try {
			List<String> res = jdbcTemplate.query(SELECT_ASSISTITI_DISABILITABILI, namedParameters,(rs, rowNum) ->rs.getString(1));
			return res;
		}
		catch (Exception e) {
			var methodName = "countRichiesteMedico";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Integer countRichiesteMedico(String cfMedico) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("cfMedico",cfMedico);
		try {
			Integer count = jdbcTemplate.queryForObject(SELECT_COUNT_RICHIESTE_PER_MEDICO, namedParameters,Integer.class);
			return count;
		}
		catch (Exception e) {
			var methodName = "countRichiesteMedico";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	public Integer countRichiesteAttiveMedico(String cfMedico) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("cfMedico",cfMedico);
		try {
			Integer count = jdbcTemplate.queryForObject(SELECT_COUNT_RICHIESTE_ATTIVE_PER_MEDICO, namedParameters,Integer.class);
			return count;
		}
		catch (Exception e) {
			var methodName = "countRichiesteMedico";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}


	public Integer countRichiesteConEsecuzioniMedico(String cfMedico) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("cfMedico",cfMedico);
		try {
			Integer count = jdbcTemplate.queryForObject(SELECT_COUNT_RICHIESTE_CON_ESECUZIONI_PER_MEDICO, namedParameters,Integer.class);
			return count;
		}
		catch (Exception e) {
			var methodName = "countRichiesteMedico";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Long insertLockRichiestaBatch(String in_cf_medico,PayloadAbilitazione payloadAbilitazione) throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Non modificare i dati vengono passati all inner insert
		MapSqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("in_cf_medico",in_cf_medico).addValue("in_batchparam_value", buildAssistitiString(payloadAbilitazione.getAssistiti()))
						.addValue("in_motivazione", payloadAbilitazione.getMotivazioneMedico())
						.addValue("in_codice_batch", payloadAbilitazione.isAbilitazione()?"ABI_SING_ASS":"DIS_SING_ASS").addValue("in_batchparam_cod", "assistiti");
		try {
			jdbcTemplate.update(INSERT_SEMAFORO_RICHIESTA_BATCH, namedParameters, keyHolder, new String[] { "batchric_id" });
			if(keyHolder.getKey()!=null) {
				codLBatchEsecuzioneDao.insertAfterLock(namedParameters, keyHolder.getKey().longValue());
			}
			return (keyHolder.getKey()!=null?keyHolder.getKey().longValue():-1);
		}
		catch (Exception e) {
			var methodName = "insertLockRichiestaBatch";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public Long insertLockRichiestaBatchTutti(String in_cf_medico,PayloadAbilitazioneTotale payloadAbilitazioneTotale) throws DatabaseException {

		KeyHolder keyHolder = new GeneratedKeyHolder();
		//Non modificare i dati vengono passati all inner insert
		MapSqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("in_cf_medico",in_cf_medico).addValue("in_batchparam_value", "all")
						.addValue("in_motivazione", payloadAbilitazioneTotale.getMotivazioneMedico())
						.addValue("in_codice_batch", payloadAbilitazioneTotale.isAbilitazione()?"ABI_TUTTI_ASS":"DIS_TUTTI_ASS").addValue("in_batchparam_cod", "all");
		try {
			jdbcTemplate.update(INSERT_SEMAFORO_RICHIESTA_BATCH, namedParameters, keyHolder, new String[] { "batchric_id" });
			if(keyHolder.getKey()!=null) {
				codLBatchEsecuzioneDao.insertAfterLock(namedParameters, keyHolder.getKey().longValue());
			}
			return (keyHolder.getKey()!=null?keyHolder.getKey().longValue():-1);
		}
		catch (Exception e) {
			var methodName = "insertLockRichiestaBatch";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}

	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void removeLockRichiestaBatchForced(Long batchric_id, boolean esito) {
		log.debug("Sono in removeLockRichiestaBatchForError:"+batchric_id);
		if(batchric_id!=null) {
			SqlParameterSource namedParameters = new MapSqlParameterSource()
					.addValue("batchric_id",batchric_id);
			jdbcTemplate.update(REMOVE_LOCK_FORCED,namedParameters);
			codLBatchEsecuzioneDao.updateAfterLock(batchric_id, esito);
		}
	}

	//@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	/**
	 *
	 * @param in_cf_medico
	 * @param payloadAbilitazione
	 * @return una lista di modelAssistito dove lo stato vale:
	 * A Abilitato
	 * B Bloccato
	 * GIA_ABILITATO gia' abilitato
	 *
	 */
	public List<ModelAssistito> callAbilitazione(String in_cf_medico,PayloadAbilitazione payloadAbilitazione){
		log.info("Sono in callAbilitazione");

		List<ModelAssistito> resFromCall = new ArrayList<ModelAssistito>();
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("in_cf_medico",in_cf_medico).addValue("in_elenco_cf", buildAssistitiString(payloadAbilitazione.getAssistiti()))
				.addValue("in_codice_batch", Constants.BATCH_ABI_SING_ASS).addValue("in_batchparam", Constants.BATCH_PARAM_ASSISTITI);
		resFromCall=jdbcTemplate.query(CALL_FUNCTION_ABILITA, namedParameters,(rs, rowNum) -> (ModelAssistito)new ModelAssistitoCustom( rs.getString("nome"), rs.getString("cognome"), rs.getString("cf"), null, null,
				rs.getString("stato"), null, payloadAbilitazione.getMotivazioneMedico()));
		return resFromCall;

	}
	/**
	 *
	 * @param in_cf_medico
	 * @param payloadAbilitazioneTotale
	 * @param in_elenco_cf
	 * @return  una lista di modelAssistito dove lo stato vale:
	 * A Abilitato
	 * B Bloccato
	 * GIA_ABILITATO gia' abilitato
	 */

//	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public List<ModelAssistito> callAbilitazioneTutti(String in_cf_medico,PayloadAbilitazioneTotale payloadAbilitazioneTotale, List<String> in_elenco_cf){
		log.info("Sono in callAbilitazioneTutti");

		List<ModelAssistito> resFromCall = new ArrayList<ModelAssistito>();
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("in_cf_medico",in_cf_medico).addValue("in_elenco_cf", buildAssistitiString(in_elenco_cf))
				.addValue("in_motivazione", payloadAbilitazioneTotale.getMotivazioneMedico())
				.addValue("in_codice_batch", Constants.BATCH_ABI_TUTTI_ASS).addValue("in_batchparam", Constants.BATCH_PARAM_ALL);
		resFromCall=jdbcTemplate.query(CALL_FUNCTION_ABILITA, namedParameters,(rs, rowNum) -> (ModelAssistito)new ModelAssistitoCustom( rs.getString("nome"), rs.getString("cognome"), rs.getString("cf"), null, null,
				rs.getString("stato"), null, payloadAbilitazioneTotale.getMotivazioneMedico()));
		return resFromCall;

	}

	//@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public List<ModelAssistito> callDisabilita(String in_cf_medico,PayloadAbilitazione payloadAbilitazione){
		log.info("Sono in callDisabilita");

		List<ModelAssistito> resFromCall = new ArrayList<ModelAssistito>();
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("in_cf_medico",in_cf_medico).addValue("in_elenco_cf", buildAssistitiString(payloadAbilitazione.getAssistiti()))
				.addValue("in_motivazione", payloadAbilitazione.getMotivazioneMedico())
				.addValue("in_codice_batch", Constants.BATCH_DIS_SING_ASS).addValue("in_batchparam", Constants.BATCH_PARAM_ASSISTITI);
		resFromCall=jdbcTemplate.query(CALL_FUNCTION_DISABILITA, namedParameters,(rs, rowNum) -> new ModelAssistitoCustom( rs.getString("nome"), rs.getString("cognome"), rs.getString("cf"), null, null,
				rs.getString("stato"), null, payloadAbilitazione.getMotivazioneMedico()));
		return resFromCall;

	}

//	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public List<ModelAssistito> callDisabilitaTutti(String in_cf_medico,PayloadAbilitazioneTotale payloadAbilitazioneTotale, List<String> in_elenco_cf){
		log.info("Sono in callDisabilitaTutti");
		List<ModelAssistito> resFromCall = new ArrayList<ModelAssistito>();
		SqlParameterSource namedParameters = new MapSqlParameterSource()
				.addValue("in_cf_medico",in_cf_medico).addValue("in_elenco_cf", buildAssistitiString(in_elenco_cf))
				.addValue("in_motivazione", payloadAbilitazioneTotale.getMotivazioneMedico())
				.addValue("in_codice_batch", Constants.BATCH_DIS_TUTTI_ASS).addValue("in_batchparam", Constants.BATCH_PARAM_ALL);
		resFromCall=jdbcTemplate.query(CALL_FUNCTION_DISABILITA, namedParameters,(rs, rowNum) -> new ModelAssistitoCustom( rs.getString("nome"), rs.getString("cognome"), rs.getString("cf"), null, null,
				rs.getString("stato"), null, payloadAbilitazioneTotale.getMotivazioneMedico()));
		return resFromCall;

	}

	private String buildAssistitiString(List<String> assistiti) {
		return assistiti.stream().collect(Collectors.joining(","));
	}

//	public Integer countRichiesteConEsecuzioniAndStatoErrorMedico(String cfMedico,String statoErrore ) throws DatabaseException {
//
//		SqlParameterSource namedParameters = new MapSqlParameterSource()
//						.addValue("cfMedico",cfMedico).addValue("statoErrore", statoErrore);
//		try {
//			Integer count = jdbcTemplate.queryForObject(SELECT_COUNT_RICHIESTE_CON_ESECUZIONI_IN_ERRORE_PER_MEDICO, namedParameters,Integer.class);
//			return count;
//		}
//		catch (Exception e) {
//			var methodName = "countRichiesteMedico";
//			logError(methodName, e.getMessage());
//			throw new DatabaseException(e);
//		}
//
//	}
//
}
