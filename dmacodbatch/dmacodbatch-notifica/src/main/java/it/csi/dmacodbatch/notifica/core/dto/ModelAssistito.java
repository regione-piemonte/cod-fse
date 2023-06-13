/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch.notifica.core.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelAssistito   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String nome = null;
  private String cognome = null;
  private String codiceFiscale = null;
  private Date dataNascita = null;
  private String sesso = null;
  private String statoAbilitazione = null;
  private ModelMotivoBlocco motivoBlocco = null;
  private String motivazioneMedico = null;

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

  /**
   * Indica se un assistito. Possibili valori * A &#x3D; assistito Ã¨ abilitato * B &#x3D; assistito Ã¨ bloccato * N &#x3D; assistito senza stato 
   **/
  

  @JsonProperty("stato_abilitazione") 
 
  public String getStatoAbilitazione() {
    return statoAbilitazione;
  }
  public void setStatoAbilitazione(String statoAbilitazione) {
    this.statoAbilitazione = statoAbilitazione;
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
    ModelAssistito modelAssistito = (ModelAssistito) o;
    return Objects.equals(nome, modelAssistito.nome) &&
        Objects.equals(cognome, modelAssistito.cognome) &&
        Objects.equals(codiceFiscale, modelAssistito.codiceFiscale) &&
        Objects.equals(dataNascita, modelAssistito.dataNascita) &&
        Objects.equals(sesso, modelAssistito.sesso) &&
        Objects.equals(statoAbilitazione, modelAssistito.statoAbilitazione) &&
        Objects.equals(motivoBlocco, modelAssistito.motivoBlocco) &&
        Objects.equals(motivazioneMedico, modelAssistito.motivazioneMedico);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nome, cognome, codiceFiscale, dataNascita, sesso, statoAbilitazione, motivoBlocco, motivazioneMedico);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelAssistito {\n");
    
    sb.append("    nome: ").append(toIndentedString(nome)).append("\n");
    sb.append("    cognome: ").append(toIndentedString(cognome)).append("\n");
    sb.append("    codiceFiscale: ").append(toIndentedString(codiceFiscale)).append("\n");
    sb.append("    dataNascita: ").append(toIndentedString(dataNascita)).append("\n");
    sb.append("    sesso: ").append(toIndentedString(sesso)).append("\n");
    sb.append("    statoAbilitazione: ").append(toIndentedString(statoAbilitazione)).append("\n");
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

