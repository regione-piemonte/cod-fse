/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.dao.custom;

import java.sql.Types;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.integration.auditlog.DmaccTLogAuditSupportForInsert;
import it.csi.dma.apiopsanaura.util.LoggerUtil;

@Repository
public class DmaccTLogAuditDao extends LoggerUtil {
	//TODO
//	cambiare query ruolo per cittadino
	private static final String INSERT_INTO_AUDIT_LOG = "INSERT INTO dmacc_t_log_audit  "
			+ "(codice_token_operazione, "
			+ "informazioni_tracciate, "
			+ "id_paziente, "
			+ "id_catalogo_log_audit, "
			+ "data_inserimento, "
			+ "id_transazione, "
			+ "id_regime, "
			+ "id_applicazione_richiedente, "
			+ "id_utente, "
			+ "id_ruolo, "
			+ "collocazione, "
			+ "visibilealcittadino, "
			+ "ip_richiedente, "
			+ "codice_servizio, "
			+ "nome_servizio, "
			+ "ip_chiamante, "
			+ "codice_fiscale_utente, "
			+ "appl_verticale, "
			+ "codice_fiscale_assistito) "
			+ " VALUES  "
			+ "( "
			+ ":codiceTokenOperazione, "
			+ ":informazioniTracciate, "
			+ "(case when :codiceFiscalePaziente is null then null else (select paz.id_paziente from dmacc_t_paziente paz where paz.codice_fiscale=:codiceFiscalePaziente ) end ), "
			+ ":idCatalogoLogAudit, "
			+ "now(), "
			+ ":xRequestId, "
			+ "(case when :codiceRegime is null then null else (select dmacc_d_regime_operativita.id from dmacc_d_regime_operativita where  dmacc_d_regime_operativita.codice_regime_operativita=:codiceRegime ) end ), "
			+ "(select appric.id from dmacc_d_applicazione_richiedente appric where  appric.codice_applicazione=:xCodiceServizio ), "
			+ "(case when :id_utente is null then null else (select uten.id from dmacc_t_utente uten where uten.codice_fiscale=:id_utente ) end ), "
			+ "(select druolo.id from dmacc_d_ruolo druolo where druolo.codice_ruolo=( SELECT x.codice_ruolo_fse FROM dmacc_t_decodifica_ruoli_pua x WHERE codice_ruolo_pua =:ruoloIn and sol=:xcodiceVerticale )), "
			+ ":collocazione, "
			+ ":visibilealcittadino, "
			+ ":ip_richiedente, "
			+ "(select operation.codice_servizio from dmacc_d_catalogo_servizi_operation operation where operation.nome_operation=:nomeOperation ), "
			+ "(select operation.nome_servizio from dmacc_d_catalogo_servizi_operation operation where operation.nome_operation=:nomeOperation ), "
			+ ":ip_chiamante, "
			+ "pgp_sym_encrypt(:codice_fiscale_utente, :encryptionkey), "
			+ ":xcodiceVerticale, "
			+ ":codice_fiscale_assistito "
			+ ");";
	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;
	@Value("${dmaencryptionkey}")
	private String dmaencryptionkey;
	
//	Types.VARCHAR,Types.INTEGER
	public Long insertDmaccTLogAudit(DmaccTLogAuditSupportForInsert logAudit) throws DatabaseException {
		KeyHolder keyHolder = new GeneratedKeyHolder();
		MapSqlParameterSource params = new MapSqlParameterSource()
				.addValue("codiceTokenOperazione",logAudit.getCodice_token_operazione())
				.addValue("codiceAudit",logAudit.getCodiceAudit(),Types.VARCHAR)
				.addValue("codiceFiscalePaziente",logAudit.getCodiceFiscalePaziente(),Types.VARCHAR)
				.addValue("xRequestId",logAudit.getxRequestId(),Types.VARCHAR)
				.addValue("codiceRegime",logAudit.getCodiceRegime(),Types.VARCHAR)
				.addValue("xCodiceServizio",logAudit.getxCodiceServizio())
				// CATALOGO AUDIT
				.addValue("idCatalogoLogAudit", logAudit.getIdCatalogoLogAudit(),Types.BIGINT)
				.addValue("informazioniTracciate", logAudit.getInformazioniTracciate(),Types.VARCHAR)
				// DA RIMUOVERE PER CITTADINO
				.addValue("id_utente",logAudit.getShibIdentitaCodiceFiscale(),Types.VARCHAR)
				.addValue("ruoloIn",logAudit.getRuolo())
				// RA RIMUOVERE PER CITTADINO
				.addValue("collocazione",logAudit.getCollocazioneDescrizione(),Types.VARCHAR)
				.addValue("visibilealcittadino",logAudit.getVisibilealcittadino(),Types.VARCHAR)
				.addValue("ip_richiedente",logAudit.getxForwardedFor(),Types.VARCHAR)
				.addValue("nomeOperation",logAudit.getNomeOperation(),Types.VARCHAR)
				.addValue("ip_chiamante",logAudit.getxForwardedFor(),Types.VARCHAR)
				// DA CIFRARE
				.addValue("codice_fiscale_utente",logAudit.getShibIdentitaCodiceFiscale(),Types.VARCHAR)
				.addValue("xcodiceVerticale",logAudit.getxCodiceVerticale()) 
				.addValue("codice_fiscale_assistito",logAudit.getCodiceFiscalePaziente(),Types.VARCHAR)
				.addValue("encryptionkey",dmaencryptionkey);
		try {
			jdbcTemplate.update(INSERT_INTO_AUDIT_LOG, params, keyHolder, new String[] { "id" });
			return keyHolder.getKey().longValue();
		} catch (Exception e) {
			var methodName = "insertDmaccTLogAudit";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}
}
