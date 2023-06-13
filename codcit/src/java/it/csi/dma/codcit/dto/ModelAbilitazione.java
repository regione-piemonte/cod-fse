/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.dto;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAbilitazione   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String abilitazione = null;
  private ModelMotivoBlocco motivoBlocco = null;
  private String motivazioneMedico = null;

  /**
   * Indica se un assistito Ã¨ abilitato. Possibili valori * A &#x3D; assistito Ã¨ abilitato * B &#x3D; assistito Ã¨ bloccato * N &#x3D; assistito senza stato 
   **/
  

  @JsonProperty("abilitazione") 
 
  public String getAbilitazione() {
    return abilitazione;
  }
  public void setAbilitazione(String abilitazione) {
    this.abilitazione = abilitazione;
  }

  /**
   **/
  

  @JsonProperty("motivo_blocco") 
 
  public ModelMotivoBlocco getMotivoBlocco() {
    return motivoBlocco;
  }
  public void setMotivoBlocco(ModelMotivoBlocco motivoBlocco) {
    this.motivoBlocco = motivoBlocco;
  }

  /**
   * La motivazione del blocco scritta dal medico
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
    ModelAbilitazione modelAbilitazione = (ModelAbilitazione) o;
    return Objects.equals(abilitazione, modelAbilitazione.abilitazione) &&
        Objects.equals(motivoBlocco, modelAbilitazione.motivoBlocco) &&
        Objects.equals(motivazioneMedico, modelAbilitazione.motivazioneMedico);
  }

  @Override
  public int hashCode() {
    return Objects.hash(abilitazione, motivoBlocco, motivazioneMedico);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelAbilitazione {\n");
    
    sb.append("    abilitazione: ").append(toIndentedString(abilitazione)).append("\n");
    sb.append("    motivoBlocco: ").append(toIndentedString(motivoBlocco)).append("\n");
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

