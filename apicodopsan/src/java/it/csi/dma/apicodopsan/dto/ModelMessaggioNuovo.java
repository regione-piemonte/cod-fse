/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelMessaggioNuovo   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String id = null;
  private Date dataCreazione = null;
  private String testo = null;
  private Boolean modificabile = null;
  private Boolean modificato = null;
  private Date dataModifica = null;

  /**
   * Identificativo univoco
   **/
  

  @JsonProperty("id") 
 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * La data in cui Ã¨ stato mandato il messaggio
   **/
  

  @JsonProperty("data_creazione") 
 
  public Date getDataCreazione() {
    return dataCreazione;
  }
  public void setDataCreazione(Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  /**
   * Il testo del messaggio
   **/
  

  @JsonProperty("testo") 
 
  public String getTesto() {
    return testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }

  /**
   * Indica se il messaggio Ã¨ modificabile
   **/
  

  @JsonProperty("modificabile") 
 
  public Boolean isModificabile() {
    return modificabile;
  }
  public void setModificabile(Boolean modificabile) {
    this.modificabile = modificabile;
  }

  /**
   * Indica se il messaggio Ã¨ stato modificato
   **/
  

  @JsonProperty("modificato") 
 
  public Boolean isModificato() {
    return modificato;
  }
  public void setModificato(Boolean modificato) {
    this.modificato = modificato;
  }

  /**
   * La data in cui il messaggio Ã¨ stato modificato
   **/
  

  @JsonProperty("data_modifica") 
 
  public Date getDataModifica() {
    return dataModifica;
  }
  public void setDataModifica(Date dataModifica) {
    this.dataModifica = dataModifica;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelMessaggioNuovo modelMessaggioNuovo = (ModelMessaggioNuovo) o;
    return Objects.equals(id, modelMessaggioNuovo.id) &&
        Objects.equals(dataCreazione, modelMessaggioNuovo.dataCreazione) &&
        Objects.equals(testo, modelMessaggioNuovo.testo) &&
        Objects.equals(modificabile, modelMessaggioNuovo.modificabile) &&
        Objects.equals(modificato, modelMessaggioNuovo.modificato) &&
        Objects.equals(dataModifica, modelMessaggioNuovo.dataModifica);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dataCreazione, testo, modificabile, modificato, dataModifica);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelMessaggioNuovo {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
    sb.append("    modificabile: ").append(toIndentedString(modificabile)).append("\n");
    sb.append("    modificato: ").append(toIndentedString(modificato)).append("\n");
    sb.append("    dataModifica: ").append(toIndentedString(dataModifica)).append("\n");
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

