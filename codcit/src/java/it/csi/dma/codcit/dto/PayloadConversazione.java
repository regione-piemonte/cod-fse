/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadConversazione   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private ModelMedico medico = null;
  private String argomento = null;
  private ModelAutorePayload autore = null;

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
   **/
  

  @JsonProperty("autore") 
 
  public ModelAutorePayload getAutore() {
    return autore;
  }
  public void setAutore(ModelAutorePayload autore) {
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
    PayloadConversazione payloadConversazione = (PayloadConversazione) o;
    return Objects.equals(medico, payloadConversazione.medico) &&
        Objects.equals(argomento, payloadConversazione.argomento) &&
        Objects.equals(autore, payloadConversazione.autore);
  }

  @Override
  public int hashCode() {
    return Objects.hash(medico, argomento, autore);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PayloadConversazione {\n");
    
    sb.append("    medico: ").append(toIndentedString(medico)).append("\n");
    sb.append("    argomento: ").append(toIndentedString(argomento)).append("\n");
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

