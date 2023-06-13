/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadAbilitazioneTotale   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Boolean abilitazione = null;
  private String motivazioneMedico = null;

  /**
   * true per abilitare, false per bloccare
   **/
  

  @JsonProperty("abilitazione") 
 
  @NotNull
  public Boolean isAbilitazione() {
    return abilitazione;
  }
  public void setAbilitazione(Boolean abilitazione) {
    this.abilitazione = abilitazione;
  }

  /**
   * La motivazione scritta dal medico quando blocca un assistito
   **/
  

  @JsonProperty("motivazione_medico") 
 
  public String getMotivazioneMedico() {
    return motivazioneMedico;
  }
  public void setMotivazioneMedico(String motivazioneMedico) {
    this.motivazioneMedico = motivazioneMedico;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    PayloadAbilitazioneTotale payloadAbilitazioneTotale = (PayloadAbilitazioneTotale) o;
    return Objects.equals(abilitazione, payloadAbilitazioneTotale.abilitazione) &&
        Objects.equals(motivazioneMedico, payloadAbilitazioneTotale.motivazioneMedico);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitazione, motivazioneMedico);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PayloadAbilitazioneTotale {\n");
    
    sb.append("    abilitazione: ").append(toIndentedString(abilitazione)).append("\n");
    sb.append("    motivazioneMedico: ").append(toIndentedString(motivazioneMedico)).append("\n");
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

