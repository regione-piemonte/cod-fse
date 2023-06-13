/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto.custom;

import java.sql.Timestamp;

public class LMessaggi {

	private String xRequestId;
	private String cfChiamante;
	private String xCodiceServizio;
	private String ipChiamante;
	private String cfRichiedente;
	private String componente;
	private Integer esitoChiamata;
	private byte[] requestPayload;
	private byte[] responsePayload;
	private Timestamp requestDate;
	private Timestamp responseDate;
	private String metodo;
	private String requestUri;
	private long msResponse;
	private Timestamp dataCreazione;
	private Timestamp dataModifica;
	private String soggettoCf;

	public String getxRequestId() {
		return xRequestId;
	}

	public void setxRequestId(String xRequestId) {
		this.xRequestId = xRequestId;
	}

	public String getCfChiamante() {
		return cfChiamante;
	}

	public void setCfChiamante(String cfChiamante) {
		this.cfChiamante = cfChiamante;
	}

	public String getxCodiceServizio() {
		return xCodiceServizio;
	}

	public void setxCodiceServizio(String xCodiceServizio) {
		this.xCodiceServizio = xCodiceServizio;
	}

	public String getIpChiamante() {
		return ipChiamante;
	}

	public void setIpChiamante(String ipChiamante) {
		this.ipChiamante = ipChiamante;
	}

	public String getCfRichiedente() {
		return cfRichiedente;
	}

	public void setCfRichiedente(String cfRichiedente) {
		this.cfRichiedente = cfRichiedente;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public Integer getEsitoChiamata() {
		return esitoChiamata;
	}

	public void setEsitoChiamata(Integer esitoChiamata) {
		this.esitoChiamata = esitoChiamata;
	}

	public byte[] getRequestPayload() {
		return requestPayload;
	}

	public void setRequestPayload(byte[] requestPayload) {
		this.requestPayload = requestPayload;
	}

	public byte[] getResponsePayload() {
		return responsePayload;
	}

	public void setResponsePayload(byte[] responsePayload) {
		this.responsePayload = responsePayload;
	}

	public Timestamp getRequestDate() {
		return requestDate;
	}

	public void setRequestDate(Timestamp requestDate) {
		this.requestDate = requestDate;
	}

	public Timestamp getResponseDate() {
		return responseDate;
	}

	public void setResponseDate(Timestamp responseDate) {
		this.responseDate = responseDate;
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}

	public String getRequestUri() {
		return requestUri;
	}

	public void setRequestUri(String requestUri) {
		this.requestUri = requestUri;
	}

	public long getMsResponse() {
		return msResponse;
	}

	public void setMsResponse(long msResponse) {
		this.msResponse = msResponse;
	}

	public Timestamp getDataCreazione() {
		return dataCreazione;
	}

	public void setDataCreazione(Timestamp dataCreazione) {
		this.dataCreazione = dataCreazione;
	}

	public Timestamp getDataModifica() {
		return dataModifica;
	}

	public void setDataModifica(Timestamp dataModifica) {
		this.dataModifica = dataModifica;
	}

	public String getSoggettoCf() {
		return soggettoCf;
	}

	public void setSoggettoCf(String soggettoCf) {
		this.soggettoCf = soggettoCf;
	}
}
