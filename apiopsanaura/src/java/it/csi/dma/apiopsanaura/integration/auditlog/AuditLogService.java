/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.auditlog;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.exception.DatabaseException;
import it.csi.dma.apiopsanaura.integration.dao.custom.DmaccTLogAuditDao;
import it.csi.dma.apiopsanaura.util.LoggerUtil;

@Service
public class AuditLogService extends LoggerUtil{
	@Autowired
	DmaccTLogAuditDao dmaccTLogAuditDao;

	public void insertAuditLog(String xRequestId, String xForwardedFor, String shibIdentitaCodiceFiscale,
			String xCodiceServizio, String xCodiceVerticale, String ruolo, String collocazioneDescrizione,
			String codFiscAss, ApiAuditEnum apiAuditEnum,String descrizioneCatalogoLogAudit,Long idCatalogoLogAudit) throws DatabaseException {
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
			audit.setCollocazioneDescrizione(collocazioneDescrizione);
			audit.setNomeOperation(apiAuditEnum.getNomeOperation());
			audit.setRuolo(ruolo);
			logInfo("insertAuditLog", audit.toString());
			Long esito = dmaccTLogAuditDao.insertDmaccTLogAudit(audit);
			logInfo("insertAuditLog", "Audit inserito con successo " + esito.toString());
		} catch (Exception e) {
			logError("insertAuditLog", "Exception : " + e.getMessage());
		}
	}
}
