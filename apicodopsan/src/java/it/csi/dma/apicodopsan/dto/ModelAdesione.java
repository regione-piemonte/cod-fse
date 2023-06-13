/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAdesione   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Date dataInizioAdesione = null;
  private Date dataFineAdesione = null;

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


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelAdesione modelAdesione = (ModelAdesione) o;
    return Objects.equals(dataInizioAdesione, modelAdesione.dataInizioAdesione) &&
        Objects.equals(dataFineAdesione, modelAdesione.dataFineAdesione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataInizioAdesione, dataFineAdesione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelAdesione {\n");
    
    sb.append("    dataInizioAdesione: ").append(toIndentedString(dataInizioAdesione)).append("\n");
    sb.append("    dataFineAdesione: ").append(toIndentedString(dataFineAdesione)).append("\n");
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

