/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelConversazioneNuova   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String id = null;
  private ModelMedico medico = null;
  private String argomento = null;
  private Date dataCreazione = null;
  private ModelAutore autore = null;

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
   **/
  

  @JsonProperty("medico") 
 
  public ModelMedico getMedico() {
    return medico;
  }
  public void setMedico(ModelMedico medico) {
    this.medico = medico;
  }

  /**
   * L&#39;argomento della conversazione
   **/
  

  @JsonProperty("argomento") 
 
  public String getArgomento() {
    return argomento;
  }
  public void setArgomento(String argomento) {
    this.argomento = argomento;
  }

  /**
   * La data in cui Ã¨ stata creata la conversazione
   **/
  

  @JsonProperty("data_creazione") 
 
  public Date getDataCreazione() {
    return dataCreazione;
  }
  public void setDataCreazione(Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  /**
   **/
  

  @JsonProperty("autore") 
 
  public ModelAutore getAutore() {
    return autore;
  }
  public void setAutore(ModelAutore autore) {
    this.autore = autore;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelConversazioneNuova modelConversazioneNuova = (ModelConversazioneNuova) o;
    return Objects.equals(id, modelConversazioneNuova.id) &&
        Objects.equals(medico, modelConversazioneNuova.medico) &&
        Objects.equals(argomento, modelConversazioneNuova.argomento) &&
        Objects.equals(dataCreazione, modelConversazioneNuova.dataCreazione) &&
        Objects.equals(autore, modelConversazioneNuova.autore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, medico, argomento, dataCreazione, autore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelConversazioneNuova {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    medico: ").append(toIndentedString(medico)).append("\n");
    sb.append("    argomento: ").append(toIndentedString(argomento)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
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

