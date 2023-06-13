/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.auditlog;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.custom.DmaccTLogAuditDao;
import it.csi.dma.codcit.util.LoggerUtil;

@Service
public class AuditLogService extends LoggerUtil{
	@Autowired
	DmaccTLogAuditDao dmaccTLogAuditDao;

	
	public void insertAuditLogCittadino(String xRequestId, 
			String xForwardedFor, 
			String shibIdentitaCodiceFiscale,
			String citId,
			String xCodiceServizio, 
			String xCodiceVerticale,
			String codFiscAss, 
			ApiAuditEnum apiAuditEnum,
			String descrizioneCatalogoLogAudit,Long idCatalogoLogAudit) throws DatabaseException {
		try {
			DmaccTLogAuditSupportForInsert audit = new DmaccTLogAuditSupportForInsert();
			audit.setCodiceAudit(apiAuditEnum.getCodiceServizio());
			audit.setInformazioniTracciate(descrizioneCatalogoLogAudit);
			audit.setIdCatalogoLogAudit(idCatalogoLogAudit);
			audit.setCodiceFiscalePaziente(codFiscAss);
			audit.setxRequestId(xRequestId);
			audit.setxCodiceServizio(xCodiceServizio);
			audit.setxForwardedFor(xForwardedFor);
			audit.setShibIdentitaCodiceFiscale(shibIdentitaCodiceFiscale);
			audit.setxCodiceVerticale(xCodiceVerticale);
			audit.setNomeOperation(apiAuditEnum.getNomeOperation());
			audit.setRuolo(RuoloCittEnum.CITTADINO.getCode());
			if(StringUtils.isNotBlank(citId) && !StringUtils.equals(shibIdentitaCodiceFiscale, citId)) {
				// Sul ruolo vale la condizione che se presente e diverso dallo shib cf il ruolo diventa delegato
				audit.setRuolo( RuoloCittEnum.DELLEGATO.getCode());
			}
			logInfo("insertAuditLogCittadino", audit.toString());
			Long esito = dmaccTLogAuditDao.insertDmaccTLogAudit(audit);
			logInfo("insertAuditLogCittadino", "Audit inserito con successo " + esito.toString());
		} catch (Exception e) {
			logError("insertAuditLogCittadino", "Exception : " + e.getMessage());
		}
	}
}
