/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAutorePayload   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String codiceFiscale = null;

  /**
   * Il codice fiscale dell&#39;autore
   **/
  

  @JsonProperty("codice_fiscale") 
 
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelAutorePayload modelAutorePayload = (ModelAutorePayload) o;
    return Objects.equals(codiceFiscale, modelAutorePayload.codiceFiscale);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codiceFiscale);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelAutorePayload {\n");
    
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
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

