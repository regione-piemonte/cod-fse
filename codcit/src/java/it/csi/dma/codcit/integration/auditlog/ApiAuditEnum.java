/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.auditlog;

public enum ApiAuditEnum {
	
	GET_CITTADINI_CONVERSAZIONI("COD_CIT_LISTA_CONV","codcit","CitListaConversazioni"),
	POST_CITTADINI_CONVERSAZIONI("COD_CIT_NUOVA_CONV","codcit","CitNuovaConversazione"),
	GET_CITTADINI_CONVERSAZIONI_MESSAGGI("COD_CIT_LISTA_MSG","codcit","CitListaMessaggi"),
	POST_CITTADINI_CONVERSAZIONI_MESSAGGI("COD_CIT_NUOVO_MSG","codcit","CitNuovoMessaggio"),
	
	PUT_CITTADINI_CONVERSAZIONI_MESSAGGI("COD_CIT_MOD_MSG","codcit","CitModificaMessaggio"),
	PATCH_CITTADINI_CONVERSAZIONI_MESSAGGI("COD_CIT_MSG_LETTO",	"codcit","CitMessaggioLetto");


	
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
