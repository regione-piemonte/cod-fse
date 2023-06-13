/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.dto;

import java.io.Serializable;

import it.csi.dmacodbatch.notifica.core.enumeration.NotificatoreEventCode;

public class NotificaSupport implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -2774937432981801162L;
	private String shibIdentitaCodiceFiscale;
	private Long soggettoRicevente;
	private String requestId;
	private String codiceServizio;
	private String codeNotificaPrincipale;
	private String codeNotificaSecondario;
	
	public String getRequestId() {
		return requestId;
	}
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}
	public String getCodiceServizio() {
		return codiceServizio;
	}
	public void setCodiceServizio(String codiceServizio) {
		this.codiceServizio = codiceServizio;
	}
	public String getShibIdentitaCodiceFiscale() {
		return shibIdentitaCodiceFiscale;
	}
	public void setShibIdentitaCodiceFiscale(String shibIdentitaCodiceFiscale) {
		this.shibIdentitaCodiceFiscale = shibIdentitaCodiceFiscale;
	}
	
	
	public String getCodeNotificaPrincipale() {
		return codeNotificaPrincipale;
	}
	public void setCodeNotificaPrincipale(String codeNotificaPrincipale) {
		this.codeNotificaPrincipale = codeNotificaPrincipale;
	}
	public String getCodeNotificaSecondario() {
		return codeNotificaSecondario;
	}
	public void setCodeNotificaSecondario(String codeNotificaSecondario) {
		this.codeNotificaSecondario = codeNotificaSecondario;
	}
	public Long getSoggettoRicevente() {
		return soggettoRicevente;
	}
	public void setSoggettoRicevente(Long soggettoRicevente) {
		this.soggettoRicevente = soggettoRicevente;
	}
	@Override
	public String toString() {
		return "NotificaSupport [shibIdentitaCodiceFiscale=" + shibIdentitaCodiceFiscale + ", soggettoRicevente=" + soggettoRicevente + ", requestId=" + requestId
				+ ", codiceServizio=" + codiceServizio + ", codeNotificaPrincipale=" + codeNotificaPrincipale + ", codeNotificaSecondario="
				+ codeNotificaSecondario + "]";
	}
	
	
}
