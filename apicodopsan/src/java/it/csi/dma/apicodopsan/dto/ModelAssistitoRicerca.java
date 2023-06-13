/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAssistitoRicerca   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String nome = null;
  private String cognome = null;
  private String codiceFiscale = null;
  private Date dataNascita = null;
  private String sesso = null;

  /**
   * Il nome dell&#39;assistito
   **/
  

  @JsonProperty("nome") 
 
  public String getNome() {
    return nome;
  }
  public void setNome(String nome) {
    this.nome = nome;
  }

  /**
   * Il cognome dell&#39;assistito
   **/
  

  @JsonProperty("cognome") 
 
  public String getCognome() {
    return cognome;
  }
  public void setCognome(String cognome) {
    this.cognome = cognome;
  }

  /**
   * Il nome dell&#39;assistito
   **/
  

  @JsonProperty("codice_fiscale") 
 
  public String getCodiceFiscale() {
    return codiceFiscale;
  }
  public void setCodiceFiscale(String codiceFiscale) {
    this.codiceFiscale = codiceFiscale;
  }

  /**
   * La data dell&#39;ultima adesione
   **/
  

  @JsonProperty("data_nascita") 
 
  public Date getDataNascita() {
    return dataNascita;
  }
  public void setDataNascita(Date dataNascita) {
    this.dataNascita = dataNascita;
  }

  /**
   * Il sesso dell&#39;assistito
   **/
  

  @JsonProperty("sesso") 
 
  public String getSesso() {
    return sesso;
  }
  public void setSesso(String sesso) {
    this.sesso = sesso;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelAssistitoRicerca modelAssistitoRicerca = (ModelAssistitoRicerca) o;
    return Objects.equals(nome, modelAssistitoRicerca.nome) &&
        Objects.equals(cognome, modelAssistitoRicerca.cognome) &&
        Objects.equals(codiceFiscale, modelAssistitoRicerca.codiceFiscale) &&
        Objects.equals(dataNascita, modelAssistitoRicerca.dataNascita) &&
        Objects.equals(sesso, modelAssistitoRicerca.sesso);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, cognome, codiceFiscale, dataNascita, sesso);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelAssistitoRicerca {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    dataNascita: ").append(toIndentedString(dataNascita)).append("\n");
    sb.append("    sesso: ").append(toIndentedString(sesso)).append("\n");
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

