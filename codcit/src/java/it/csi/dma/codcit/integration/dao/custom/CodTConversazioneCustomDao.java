/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.dto.custom.ConversazioneCustom;
import it.csi.dma.codcit.dto.custom.ConversazioneSoggettoCustom;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.ConversazioneCustomMapper;
import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.enumerator.SolaLetturaValueEnum;

@Repository
public class CodTConversazioneCustomDao extends LoggerUtil {

	private static final String BASE_SELECT_CONVERSAZIONE = "with ctcs as ( " + "select "
			+ "    ctc.conversazione_id, ctc.disabilitazione_motivo_id, ctc.conversazione_cod , ctsMedico.soggetto_id as medico_soggetto_id, ctsMedico.soggetto_nome , ctsMedico.soggetto_cognome , ctsMedico.soggetto_cf,  "
			+ "    convert_from(pgp_sym_decrypt_bytea(ctc.conversazione_oggetto, :encryptionkey), 'UTF8') as conversazione_oggetto, ctc.conversazione_data_blocco, ctc.data_creazione, ctc.utente_creazione, "
			+ "    (select count(*) from cod_t_messaggio z where (z.messaggio_lettura_data is null  or (z.messaggio_lettura_data is not null and z.messaggio_lettura_data < z.data_modifica) )  and z.soggetto_id_a = cts.soggetto_id "
			+ "    and z.conversazione_id = ctc.conversazione_id) as messaggi_non_letti, cts.soggetto_cf " + "from "
			+ "    cod_t_conversazione ctc " + "join cod_t_soggetto cts on "
			+ "    ctc.soggetto_id_autore = cts.soggetto_id " + "    and cts.soggetto_cf = :citId "
			+ "join cod_t_soggetto ctsMedico on  "
			+ "    ctc.soggetto_id_partecipante = ctsMedico.soggetto_id   ";
	private static final String BASE_SELECT_CONVERSAZIONE_MEDICO = "    and ctsMedico.soggetto_cf = :cfMedico  "
			+ "    and ctsMedico.soggetto_is_medico is true  ";
	private static final String BASE_WHERE_CONVERSAZIONE = "where true  ";
	private static final String BASE_WHERE_CONVERSAZIONE_A = "and ctc.conversazione_data_blocco  is null ";
	private static final String BASE_WHERE_CONVERSAZIONE_B = "and ctc.conversazione_data_blocco is not null ";
	private static final String BASE_WHERE_CONVERSAZIONE_ID = "and ctc.conversazione_cod = :idConversazione ";
	private static final String BASE_WHERE_CONVERSAZIONE_ARGOMENTO = " and convert_from(pgp_sym_decrypt_bytea(ctc.conversazione_oggetto, :encryptionkey), 'UTF8') ilike :argomento ";
	private static final String BASE_SELECT_CONVERSAZIONE_FINALE = ") " + "select " + " ctcs.*, "
			+ " cddm.disabilitazione_motivo_desc, " + " cddm.disabilitazione_motivo_cod, "
			+ " ctms.data_creazione as messaggio_data_creazione, " + " ctms.messaggio_id, "
			+ " ctms.messaggio_lettura_data, " + " convert_from(pgp_sym_decrypt_bytea(ctms.messaggio_testo_cifrato, "
			+ " :encryptionkey ), 'UTF8') as messaggio_testo, " + " ctms.utente_creazione as messaggio_utente_creazione, "
			+ " ctms.utente_modifica as messaggio_utente_modifica, "
			+ " ctms.data_modifica as messaggio_data_modifica, " + " ctms.soggetto_id_da " + "from " + " ctcs "
			+ "left join cod_d_disabilitazione_motivo cddm on "
			+ " cddm.disabilitazione_motivo_id = ctcs.disabilitazione_motivo_id " + "left join ( " + " select "
			+ " distinct on " + " (ctm.conversazione_id) ctm.conversazione_id, " + " ctm.data_creazione, "
			+ " ctm.messaggio_id, " + " ctm.messaggio_lettura_data, " + " ctm.messaggio_testo_cifrato, "
			+ " ctm.utente_creazione, " + " ctm.utente_modifica, " + " ctm.data_modifica, " + " ctm.soggetto_id_da "
			+ " from " + " cod_t_messaggio ctm " + " where " + " ctm.conversazione_id in ( " + " select "
			+ " conversazione_id " + " from " + " ctcs) " + " order by " + " ctm.conversazione_id, "
			+ " ctm.data_creazione desc ) as ctms on " + " ctms.conversazione_id = ctcs.conversazione_id  order by 1 desc ";
	private static final String BASE_COUNT_CONVERSAZIONE_FINALE = ")  select  count(ctcs.*) from  ctcs  ";

	private static final String BASE_OFFSET = "OFFSET   :offset ";
	private static final String BASE_LIMIT = "LIMIT :limit ";
	private static final String BASE_SELECT_CONVERSAZIONE_FINALE_CHIUSURA = "; ";


	//query utilizzata per trovare le conversazioni per lista messaggi
	private static final String SQL_SELECT_T_CONVERSAZIONE_AND_CF_SOGGETTO="SELECT c.conversazione_id,c.conversazione_cod, convert_from(pgp_sym_decrypt_bytea(conversazione_oggetto, :encryptionkey),'UTF8') as conversazione_oggetto, soggetto_id_autore, soggetto_id_partecipante, conversazione_data_blocco, disabilitazione_motivo_id, c.data_creazione, c.data_modifica, c.utente_creazione, c.utente_modifica "
			+ ",s.soggetto_cf FROM cod_t_conversazione as c, cod_t_soggetto as s where c.soggetto_id_autore=s.soggetto_id and c.conversazione_cod=:conversazione_cod ;";


	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	@Value("${encryptionkey}")
	private String encryptionkey;

	public List<ConversazioneCustom> selectConversazioniExtByCittadinoAndMedicoAndArgomentoAndIdConversazioneAndSolaLettura(
			String cfMedico, String cfCittadino, String argomento, String idConversazione, String solaLettura,
			Integer offset, Integer limit) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("citId", cfCittadino)
				.addValue("cfMedico", cfMedico).addValue("argomento", this.convertForLike(argomento))
				.addValue("idConversazione", idConversazione).addValue("encryptionkey", encryptionkey)
				.addValue("offset", offset).addValue("limit", limit);
		try {
			StringBuilder sql = new StringBuilder().append(BASE_SELECT_CONVERSAZIONE);
			if (cfMedico != null) {
				sql.append(BASE_SELECT_CONVERSAZIONE_MEDICO);
			}
			sql.append(BASE_WHERE_CONVERSAZIONE);
			if (SolaLetturaValueEnum.SOLA_LETTURA_A.getCode().equals(solaLettura)) {
				sql.append(BASE_WHERE_CONVERSAZIONE_A);

			} else if (SolaLetturaValueEnum.SOLA_LETTURA_B.getCode().equals(solaLettura)) {
				sql.append(BASE_WHERE_CONVERSAZIONE_B);
			}
			if (idConversazione != null && !idConversazione.isEmpty()) {
				sql.append(BASE_WHERE_CONVERSAZIONE_ID);
			}
			if (argomento != null && !argomento.isEmpty()) {
				//aggiungo % in test al fondo e sostiuisco blank con %
				sql.append(BASE_WHERE_CONVERSAZIONE_ARGOMENTO);
			}
			sql.append(BASE_SELECT_CONVERSAZIONE_FINALE);
			if (offset != null) {
				sql.append(BASE_OFFSET);
			}
			if (limit != null) {
				sql.append(BASE_LIMIT);
			}
			sql.append(BASE_SELECT_CONVERSAZIONE_FINALE_CHIUSURA);
			return jdbcTemplate.query(sql.toString(), namedParameters, new ConversazioneCustomMapper());
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			var methodName = "selectConversazioniExtByCittadinoAndMedicoAndArgomentoAndIdConversazioneAndSolaLettura";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public Integer countConversazioniExtByCittadinoAndMedicoAndArgomentoAndIdConversazioneAndSolaLettura(
			String cfMedico, String cfCittadino, String argomento, String idConversazione, String solaLettura) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("citId", cfCittadino)
				.addValue("cfMedico", cfMedico).addValue("argomento", this.convertForLike(argomento))
				.addValue("idConversazione", idConversazione).addValue("encryptionkey", encryptionkey);
		try {
			StringBuilder sql = new StringBuilder().append(BASE_SELECT_CONVERSAZIONE);
			if (cfMedico != null) {
				sql.append(BASE_SELECT_CONVERSAZIONE_MEDICO);
			}
			sql.append(BASE_WHERE_CONVERSAZIONE);
			if (SolaLetturaValueEnum.SOLA_LETTURA_A.getCode().equals(solaLettura)) {
				sql.append(BASE_WHERE_CONVERSAZIONE_A);

			} else if (SolaLetturaValueEnum.SOLA_LETTURA_B.getCode().equals(solaLettura)) {
				sql.append(BASE_WHERE_CONVERSAZIONE_B);
			}
			if (idConversazione != null && !idConversazione.isEmpty()) {
				sql.append(BASE_WHERE_CONVERSAZIONE_ID);
			}
			if (argomento != null && !argomento.isEmpty()) {
				sql.append(BASE_WHERE_CONVERSAZIONE_ARGOMENTO);
			}
			sql.append(BASE_COUNT_CONVERSAZIONE_FINALE);
			sql.append(BASE_SELECT_CONVERSAZIONE_FINALE_CHIUSURA);
			return jdbcTemplate.queryForObject(sql.toString(), namedParameters, Integer.class);
		} catch (EmptyResultDataAccessException e) {
			return 0;
		} catch (Exception e) {
			var methodName = "countConversazioniExtByCittadinoAndMedicoAndArgomentoAndIdConversazioneAndSolaLettura";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public List<ConversazioneSoggettoCustom> selectConversazioniByconversazioneCod(String conversazione_cod) throws DatabaseException{
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("conversazione_cod", conversazione_cod)
				.addValue("encryptionkey", encryptionkey);
		try {
			return jdbcTemplate.query(SQL_SELECT_T_CONVERSAZIONE_AND_CF_SOGGETTO, namedParameters, (rs, rowNum) ->
			new ConversazioneSoggettoCustom(rs.getLong(1), rs.getString(2) , rs.getString(3), rs.getInt(4), rs.getInt(5), rs.getTimestamp(6), rs.getInt(7), rs.getTimestamp(8), rs.getTimestamp(9), rs.getString(10), rs.getString(11), rs.getString(12)));
		} catch (EmptyResultDataAccessException e) {
			return new ArrayList<>();
		} catch (Exception e) {
			var methodName = "selectSoggettoDisabilitatoWhereCittadinoAbilitatoDaMedico";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	protected String convertForLike(String src) {
		String newLike="";
		if(StringUtils.isNotEmpty(src)) {
			newLike="%"+src.replaceAll(" ", "%")+"%";
		}
		return newLike;
	}

}
