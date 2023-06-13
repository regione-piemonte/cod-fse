/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.auditlog;

public enum ApiAuditEnum {
	
	POST_ADESIONE("COD_MED_ADE_REV","apiopsan","MedAdesioneRevoca"),
	GET_CONVERSAZIONI("COD_MED_LISTA_CONV","apiopsan","MedListaConversazioni"),
	GET_ASSISTITI("COD_MED_LISTA_ASS","apiopsan","MedListaAssistiti"),
	//PATCH_MESSAGGI_LETTI("COD_MED_MSG_LETTO","apiopsan","MedMessaggioLetto"),
	GET_CONVERSAZIONE_MESSAGGI("COD_MED_LISTA_MSG","apiopsan","MedListaMessaggi"),
	POST_CONVERSAZIONE_MESSAGGI("COD_MED_NUOVO_MSG","apiopsan","MedNuovoMessaggio"),
	PUT_CONVERSAZIONE_MESSAGGI("COD_MED_MOD_MSG","apiopsan","MedModificaMessaggio"),
	PUT_CONVERSAZIONE_MESSAGG_LETTURA("COD_MED_NOTLET","apiopsan","MedNotificaLettura"),
	POST_ASSISTITI_TUTTI("COD_MED_ABIBLO_TUTTIASS","apiopsan","MedAbilitaBloccaTuttiAssistiti"),
	POST_ASSISTITI("COD_MED_ABIBLO_ASS","apiopsan","MedAbilitaBloccaAssistiti");


	
	
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
