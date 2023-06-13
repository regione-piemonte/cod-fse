/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadAdesione   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Boolean adesione = null;
  private String nomeMedico = null;
  private String cognomeMedico = null;

  /**
   * true se desidera aderire, false per revocare
   **/
  

  @JsonProperty("adesione") 
 
  @NotNull
  public Boolean isAdesione() {
    return adesione;
  }
  public void setAdesione(Boolean adesione) {
    this.adesione = adesione;
  }

  /**
   * Il nome del medico
   **/
  

  @JsonProperty("nome_medico") 
 
  public String getNomeMedico() {
    return nomeMedico;
  }
  public void setNomeMedico(String nomeMedico) {
    this.nomeMedico = nomeMedico;
  }

  /**
   * Il cognome del medico
   **/
  

  @JsonProperty("cognome_medico") 
 
  public String getCognomeMedico() {
    return cognomeMedico;
  }
  public void setCognomeMedico(String cognomeMedico) {
    this.cognomeMedico = cognomeMedico;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PayloadAdesione payloadAdesione = (PayloadAdesione) o;
    return Objects.equals(adesione, payloadAdesione.adesione) &&
        Objects.equals(nomeMedico, payloadAdesione.nomeMedico) &&
        Objects.equals(cognomeMedico, payloadAdesione.cognomeMedico);
  }

  @Override
  public int hashCode() {
    return Objects.hash(adesione, nomeMedico, cognomeMedico);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PayloadAdesione {\n");
    
    sb.append("    adesione: ").append(toIndentedString(adesione)).append("\n");
    sb.append("    nomeMedico: ").append(toIndentedString(nomeMedico)).append("\n");
    sb.append("    cognomeMedico: ").append(toIndentedString(cognomeMedico)).append("\n");
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

