/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelMessaggio   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private String id = null;
  private Date dataCreazione = null;
  private Boolean letto = null;
  private Date dataLettura = null;
  private String testo = null;
  private Boolean modificabile = null;
  private Boolean modificato = null;
  private ModelAutore autore = null;
  private String autoreModifica = null;
  private Date dataModifica = null;
  private List<ModelDocumento> allegati = new ArrayList<ModelDocumento>();

  /**
   * Identificativo univoco
   **/
  

  @JsonProperty("id") 
 
  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  /**
   * La data in cui Ã¨ stato mandato il messaggio
   **/
  

  @JsonProperty("data_creazione") 
 
  public Date getDataCreazione() {
    return dataCreazione;
  }
  public void setDataCreazione(Date dataCreazione) {
    this.dataCreazione = dataCreazione;
  }

  /**
   * Indica se il messaggio Ã¨ stato letto dal destinatario
   **/
  

  @JsonProperty("letto") 
 
  public Boolean isLetto() {
    return letto;
  }
  public void setLetto(Boolean letto) {
    this.letto = letto;
  }

  /**
   * La data in cui il messaggio Ã¨ stato letto dal destinatario
   **/
  

  @JsonProperty("data_lettura") 
 
  public Date getDataLettura() {
    return dataLettura;
  }
  public void setDataLettura(Date dataLettura) {
    this.dataLettura = dataLettura;
  }

  /**
   * Il testo del messaggio
   **/
  

  @JsonProperty("testo") 
 
  public String getTesto() {
    return testo;
  }
  public void setTesto(String testo) {
    this.testo = testo;
  }

  /**
   * Indica se il messaggio Ã¨ modificabile
   **/
  

  @JsonProperty("modificabile") 
 
  public Boolean isModificabile() {
    return modificabile;
  }
  public void setModificabile(Boolean modificabile) {
    this.modificabile = modificabile;
  }

  /**
   * Indica se il messaggio Ã¨ stato modificato
   **/
  

  @JsonProperty("modificato") 
 
  public Boolean isModificato() {
    return modificato;
  }
  public void setModificato(Boolean modificato) {
    this.modificato = modificato;
  }

  /**
   **/
  

  @JsonProperty("autore") 
 
  public ModelAutore getAutore() {
    return autore;
  }
  public void setAutore(ModelAutore autore) {
    this.autore = autore;
  }

  /**
   * Il cf dell&#39;autore della modifica
   **/
  

  @JsonProperty("autore_modifica") 
 
  public String getAutoreModifica() {
    return autoreModifica;
  }
  public void setAutoreModifica(String autoreModifica) {
    this.autoreModifica = autoreModifica;
  }

  /**
   * La data in cui il messaggio Ã¨ stato modificato
   **/
  

  @JsonProperty("data_modifica") 
 
  public Date getDataModifica() {
    return dataModifica;
  }
  public void setDataModifica(Date dataModifica) {
    this.dataModifica = dataModifica;
  }

  /**
   **/
  

  @JsonProperty("allegati") 
 
  public List<ModelDocumento> getAllegati() {
    return allegati;
  }
  public void setAllegati(List<ModelDocumento> allegati) {
    this.allegati = allegati;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelMessaggio modelMessaggio = (ModelMessaggio) o;
    return Objects.equals(id, modelMessaggio.id) &&
        Objects.equals(dataCreazione, modelMessaggio.dataCreazione) &&
        Objects.equals(letto, modelMessaggio.letto) &&
        Objects.equals(dataLettura, modelMessaggio.dataLettura) &&
        Objects.equals(testo, modelMessaggio.testo) &&
        Objects.equals(modificabile, modelMessaggio.modificabile) &&
        Objects.equals(modificato, modelMessaggio.modificato) &&
        Objects.equals(autore, modelMessaggio.autore) &&
        Objects.equals(autoreModifica, modelMessaggio.autoreModifica) &&
        Objects.equals(dataModifica, modelMessaggio.dataModifica) &&
        Objects.equals(allegati, modelMessaggio.allegati);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, dataCreazione, letto, dataLettura, testo, modificabile, modificato, autore, autoreModifica, dataModifica, allegati);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelMessaggio {\n");
    
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    dataCreazione: ").append(toIndentedString(dataCreazione)).append("\n");
    sb.append("    letto: ").append(toIndentedString(letto)).append("\n");
    sb.append("    dataLettura: ").append(toIndentedString(dataLettura)).append("\n");
    sb.append("    testo: ").append(toIndentedString(testo)).append("\n");
    sb.append("    modificabile: ").append(toIndentedString(modificabile)).append("\n");
    sb.append("    modificato: ").append(toIndentedString(modificato)).append("\n");
    sb.append("    autore: ").append(toIndentedString(autore)).append("\n");
    sb.append("    autoreModifica: ").append(toIndentedString(autoreModifica)).append("\n");
    sb.append("    dataModifica: ").append(toIndentedString(dataModifica)).append("\n");
    sb.append("    allegati: ").append(toIndentedString(allegati)).append("\n");
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

