/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonProperty;

public class PayloadAbilitazione   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Boolean abilitazione = null;
  private List<String> assistiti = new ArrayList<String>();
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
   **/
  

  @JsonProperty("assistiti") 
 
  @NotNull
  public List<String> getAssistiti() {
    return assistiti;
  }
  public void setAssistiti(List<String> assistiti) {
    this.assistiti = assistiti;
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
    PayloadAbilitazione payloadAbilitazione = (PayloadAbilitazione) o;
    return Objects.equals(abilitazione, payloadAbilitazione.abilitazione) &&
        Objects.equals(assistiti, payloadAbilitazione.assistiti) &&
        Objects.equals(motivazioneMedico, payloadAbilitazione.motivazioneMedico);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitazione, assistiti, motivazioneMedico);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class PayloadAbilitazione {\n");
    
    sb.append("    abilitazione: ").append(toIndentedString(abilitazione)).append("\n");
    sb.append("    assistiti: ").append(toIndentedString(assistiti)).append("\n");
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

