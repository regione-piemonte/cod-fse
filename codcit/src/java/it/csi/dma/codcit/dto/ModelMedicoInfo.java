/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelMedicoInfo   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Date dataInizioAdesione = null;
  private Date dataFineAdesione = null;
  private Boolean notificaLetturaMessaggi = null;
  private String codiceFiscale = null;
  private String cognome = null;
  private String nome = null;

  /**
   * La data dell&#39;ultima adesione
   **/
  

  @JsonProperty("data_inizio_adesione") 
 
  public Date getDataInizioAdesione() {
    return dataInizioAdesione;
  }
  public void setDataInizioAdesione(Date dataInizioAdesione) {
    this.dataInizioAdesione = dataInizioAdesione;
  }

  /**
   * La data della revoca al servizio.
   **/
  

  @JsonProperty("data_fine_adesione") 
 
  public Date getDataFineAdesione() {
    return dataFineAdesione;
  }
  public void setDataFineAdesione(Date dataFineAdesione) {
    this.dataFineAdesione = dataFineAdesione;
  }

  /**
   * Indica se i pazienti possono visualizzare lo stato della lettura dei messaggi inviati al medico
   **/
  

  @JsonProperty("notifica_lettura_messaggi") 
 
  public Boolean isNotificaLetturaMessaggi() {
    return notificaLetturaMessaggi;
  }
  public void setNotificaLetturaMessaggi(Boolean notificaLetturaMessaggi) {
    this.notificaLetturaMessaggi = notificaLetturaMessaggi;
  }

  /**
   * Il codice fiscale del medico
   **/
  

  @JsonProperty("codice_fiscale") 
 
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * Il cognome del medico
   **/
  

  @JsonProperty("cognome") 
 
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   * Il nome del medico
   **/
  

  @JsonProperty("nome") 
 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelMedicoInfo modelMedicoInfo = (ModelMedicoInfo) o;
    return Objects.equals(dataInizioAdesione, modelMedicoInfo.dataInizioAdesione) &&
        Objects.equals(dataFineAdesione, modelMedicoInfo.dataFineAdesione) &&
        Objects.equals(notificaLetturaMessaggi, modelMedicoInfo.notificaLetturaMessaggi) &&
        Objects.equals(codiceFiscale, modelMedicoInfo.codiceFiscale) &&
        Objects.equals(cognome, modelMedicoInfo.cognome) &&
        Objects.equals(nome, modelMedicoInfo.nome);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataInizioAdesione, dataFineAdesione, notificaLetturaMessaggi, codiceFiscale, cognome, nome);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelMedicoInfo {\n");
    
    sb.append("    dataInizioAdesione: ").append(toIndentedString(dataInizioAdesione)).append("\n");
    sb.append("    dataFineAdesione: ").append(toIndentedString(dataFineAdesione)).append("\n");
    sb.append("    notificaLetturaMessaggi: ").append(toIndentedString(notificaLetturaMessaggi)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}

