/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadNotificaLettura   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Boolean notificaLettura = null;

  /**
   * true se desidera aderire, false per revocare
   **/
  

  @JsonProperty("notifica_lettura") 
 
  @NotNull
  public Boolean isNotificaLettura() {
    return notificaLettura;
  }
  public void setNotificaLettura(Boolean notificaLettura) {
    this.notificaLettura = notificaLettura;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PayloadNotificaLettura payloadNotificaLettura = (PayloadNotificaLettura) o;
    return Objects.equals(notificaLettura, payloadNotificaLettura.notificaLettura);
  }

  @Override
  public int hashCode() {
    return Objects.hash(notificaLettura);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PayloadNotificaLettura {\n");
    
    sb.append("    notificaLettura: ").append(toIndentedString(notificaLettura)).append("\n");
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

