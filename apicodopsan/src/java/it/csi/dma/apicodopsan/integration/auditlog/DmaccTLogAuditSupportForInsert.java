/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.auditlog;

public class DmaccTLogAuditSupportForInsert {
	private String codice_token_operazione=null;
	private String visibilealcittadino="S";
	private String informazioniTracciate=null;
	private Long idCatalogoLogAudit = null;
	// CAMPI ACCESSORI
	private String codiceAudit;
	private String codiceFiscalePaziente;
	// PER CITTADINO NULL
	private String codiceRegime="AMB";
	private String xRequestId=null;
	private String xCodiceServizio=null;
	private String xForwardedFor=null;
	private String shibIdentitaCodiceFiscale = null;
	private String xCodiceVerticale=null;
	private String collocazioneDescrizione=null;
	private String nomeOperation= null;
	private String ruolo;
	
	public String getCodice_token_operazione() {
		return codice_token_operazione;
	}
	public void setCodice_token_operazione(String codice_token_operazione) {
		this.codice_token_operazione = codice_token_operazione;
	}
	public String getVisibilealcittadino() {
		return visibilealcittadino;
	}
	public void setVisibilealcittadino(String visibilealcittadino) {
		this.visibilealcittadino = visibilealcittadino;
	}
	
	public String getInformazioniTracciate() {
		return informazioniTracciate;
	}
	public void setInformazioniTracciate(String informazioniTracciate) {
		this.informazioniTracciate = informazioniTracciate;
	}
	
	public Long getIdCatalogoLogAudit() {
		return idCatalogoLogAudit;
	}
	public void setIdCatalogoLogAudit(Long idCatalogoLogAudit) {
		this.idCatalogoLogAudit = idCatalogoLogAudit;
	}
	// CAMPI ACCESSORI
	public String getCodiceAudit() {
		return codiceAudit;
	}
	public void setCodiceAudit(String codiceAudit) {
		this.codiceAudit = codiceAudit;
	}
	public String getCodiceFiscalePaziente() {
		return codiceFiscalePaziente;
	}
	public void setCodiceFiscalePaziente(String codiceFiscalePaziente) {
		this.codiceFiscalePaziente = codiceFiscalePaziente;
	}
	public String getCodiceRegime() {
		return codiceRegime;
	}
	public void setCodiceRegime(String codiceRegime) {
		this.codiceRegime = codiceRegime;
	}
	public String getxRequestId() {
		return xRequestId;
	}
	public void setxRequestId(String xRequestId) {
		this.xRequestId = xRequestId;
	}
	public String getxCodiceServizio() {
		return xCodiceServizio;
	}
	public void setxCodiceServizio(String xCodiceServizio) {
		this.xCodiceServizio = xCodiceServizio;
	}
	public String getxForwardedFor() {
		return xForwardedFor;
	}
	public void setxForwardedFor(String xForwardedFor) {
		this.xForwardedFor = xForwardedFor;
	}
	public String getShibIdentitaCodiceFiscale() {
		return shibIdentitaCodiceFiscale;
	}
	public void setShibIdentitaCodiceFiscale(String shibIdentitaCodiceFiscale) {
		this.shibIdentitaCodiceFiscale = shibIdentitaCodiceFiscale;
	}
	public String getxCodiceVerticale() {
		return xCodiceVerticale;
	}
	public void setxCodiceVerticale(String xCodiceVerticale) {
		this.xCodiceVerticale = xCodiceVerticale;
	}
	public String getCollocazioneDescrizione() {
		return collocazioneDescrizione;
	}
	public void setCollocazioneDescrizione(String collocazioneDescrizione) {
		this.collocazioneDescrizione = collocazioneDescrizione;
	}
	public String getNomeOperation() {
		return nomeOperation;
	}
	public void setNomeOperation(String nomeOperation) {
		this.nomeOperation = nomeOperation;
	}
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	@Override
	public String toString() {
		return "DmaccTLogAuditSupportForInsert [codice_token_operazione=" + codice_token_operazione
				+ ", visibilealcittadino=" + visibilealcittadino + ", informazioniTracciate=" + informazioniTracciate
				+ ", idCatalogoLogAudit=" + idCatalogoLogAudit + ", codiceAudit=" + codiceAudit
				+ ", codiceFiscalePaziente=" + codiceFiscalePaziente + ", codiceRegime=" + codiceRegime
				+ ", xRequestId=" + xRequestId + ", xCodiceServizio=" + xCodiceServizio + ", xForwardedFor="
				+ xForwardedFor + ", shibIdentitaCodiceFiscale=" + shibIdentitaCodiceFiscale + ", xCodiceVerticale="
				+ xCodiceVerticale + ", collocazioneDescrizione=" + collocazioneDescrizione + ", nomeOperation="
				+ nomeOperation + ", ruolo=" + ruolo + "]";
	}
	
	
}
