/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelCodifica   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String codDiagnosi = null;
  private String codEsenzione = null;
  private Date dataInizioValidita = null;
  private Date dataFineValidita = null;
  private String descDiagnosi = null;
  private String descEsenzione = null;
  private String tipoEsenzione = null;

  /**
   **/
  

  @JsonProperty("cod_diagnosi") 
 
  public String getCodDiagnosi() {
    return codDiagnosi;
  }
  public void setCodDiagnosi(String codDiagnosi) {
    this.codDiagnosi = codDiagnosi;
  }

  /**
   **/
  

  @JsonProperty("cod_esenzione") 
 
  public String getCodEsenzione() {
    return codEsenzione;
  }
  public void setCodEsenzione(String codEsenzione) {
    this.codEsenzione = codEsenzione;
  }

  /**
   * La data dell&#39;ultima adesione
   **/
  

  @JsonProperty("data_inizio_validita") 
 
  public Date getDataInizioValidita() {
    return dataInizioValidita;
  }
  public void setDataInizioValidita(Date dataInizioValidita) {
    this.dataInizioValidita = dataInizioValidita;
  }

  /**
   * La data dell&#39;ultima adesione
   **/
  

  @JsonProperty("data_fine_validita") 
 
  public Date getDataFineValidita() {
    return dataFineValidita;
  }
  public void setDataFineValidita(Date dataFineValidita) {
    this.dataFineValidita = dataFineValidita;
  }

  /**
   **/
  

  @JsonProperty("desc_diagnosi") 
 
  public String getDescDiagnosi() {
    return descDiagnosi;
  }
  public void setDescDiagnosi(String descDiagnosi) {
    this.descDiagnosi = descDiagnosi;
  }

  /**
   **/
  

  @JsonProperty("desc_esenzione") 
 
  public String getDescEsenzione() {
    return descEsenzione;
  }
  public void setDescEsenzione(String descEsenzione) {
    this.descEsenzione = descEsenzione;
  }

  /**
   **/
  

  @JsonProperty("tipo_esenzione") 
 
  public String getTipoEsenzione() {
    return tipoEsenzione;
  }
  public void setTipoEsenzione(String tipoEsenzione) {
    this.tipoEsenzione = tipoEsenzione;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelCodifica modelCodifica = (ModelCodifica) o;
    return Objects.equals(codDiagnosi, modelCodifica.codDiagnosi) &&
        Objects.equals(codEsenzione, modelCodifica.codEsenzione) &&
        Objects.equals(dataInizioValidita, modelCodifica.dataInizioValidita) &&
        Objects.equals(dataFineValidita, modelCodifica.dataFineValidita) &&
        Objects.equals(descDiagnosi, modelCodifica.descDiagnosi) &&
        Objects.equals(descEsenzione, modelCodifica.descEsenzione) &&
        Objects.equals(tipoEsenzione, modelCodifica.tipoEsenzione);
  }

  @Override
  public int hashCode() {
    return Objects.hash(codDiagnosi, codEsenzione, dataInizioValidita, dataFineValidita, descDiagnosi, descEsenzione, tipoEsenzione);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelCodifica {\n");
    
    sb.append("    codDiagnosi: ").append(toIndentedString(codDiagnosi)).append("\n");
    sb.append("    codEsenzione: ").append(toIndentedString(codEsenzione)).append("\n");
    sb.append("    dataInizioValidita: ").append(toIndentedString(dataInizioValidita)).append("\n");
    sb.append("    dataFineValidita: ").append(toIndentedString(dataFineValidita)).append("\n");
    sb.append("    descDiagnosi: ").append(toIndentedString(descDiagnosi)).append("\n");
    sb.append("    descEsenzione: ").append(toIndentedString(descEsenzione)).append("\n");
    sb.append("    tipoEsenzione: ").append(toIndentedString(tipoEsenzione)).append("\n");
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

