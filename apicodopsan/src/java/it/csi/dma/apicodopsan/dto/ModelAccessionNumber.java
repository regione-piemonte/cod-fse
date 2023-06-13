/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAccessionNumber   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String accessionNumber = null;
  private Date dataNotificaPacs = null;

  /**
   **/
  

  @JsonProperty("accession_number") 
 
  public String getAccessionNumber() {
    return accessionNumber;
  }
  public void setAccessionNumber(String accessionNumber) {
    this.accessionNumber = accessionNumber;
  }

  /**
   **/
  

  @JsonProperty("data_notifica_pacs") 
 
  public Date getDataNotificaPacs() {
    return dataNotificaPacs;
  }
  public void setDataNotificaPacs(Date dataNotificaPacs) {
    this.dataNotificaPacs = dataNotificaPacs;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelAccessionNumber modelAccessionNumber = (ModelAccessionNumber) o;
    return Objects.equals(accessionNumber, modelAccessionNumber.accessionNumber) &&
        Objects.equals(dataNotificaPacs, modelAccessionNumber.dataNotificaPacs);
  }

  @Override
  public int hashCode() {
    return Objects.hash(accessionNumber, dataNotificaPacs);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelAccessionNumber {\n");
    
    sb.append("    accessionNumber: ").append(toIndentedString(accessionNumber)).append("\n");
    sb.append("    dataNotificaPacs: ").append(toIndentedString(dataNotificaPacs)).append("\n");
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

