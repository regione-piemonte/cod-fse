/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration.auditlog;

public enum ApiAuditEnum {
	
	GET_ASSISTITI_AURA("COD_MED_CER_ASS","apiopsan","MedCercaAssistiti"),
	GET_ESENZIONI_AURA("COD_MED_LISTA_ESE","apiopsan","MedListaEsenzioni");
	
	
	private final String codiceServizio;
	private final String nomeServizio;
	private final String nomeOperation;
	private ApiAuditEnum(String codiceServizio, String nomeServizio, String nomeOperation) {
		this.codiceServizio = codiceServizio;
		this.nomeServizio = nomeServizio;
		this.nomeOperation = nomeOperation;
	}
	public String getCodiceServizio() {
		return codiceServizio;
	}
	public String getNomeServizio() {
		return nomeServizio;
	}
	public String getNomeOperation() {
		return nomeOperation;
	}
	
	
}
