/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto;

import java.util.Date;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ModelMedicoInfo   {
  // verra' utilizzata la seguente strategia serializzazione degli attributi: [explicit-as-modeled] 
  
  private Date dataInizioAdesione = null;
  private Date dataFineAdesione = null;
  private Boolean notificaLetturaMessaggi = null;
  private Integer nPazientiAbilitati = null;
  private Integer nPazientiBloccati = null;
  private Boolean abilitazioneAssistiti = null;

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

  /**
   * Indica se i pazienti possono visualizzare lo stato della lettura dei messaggi inviati al medico
   **/
  

  @JsonProperty("notifica_lettura_messaggi") 
 
  public Boolean isNotificaLetturaMessaggi() {
    return notificaLetturaMessaggi;
  }
  public void setNotificaLetturaMessaggi(Boolean notificaLetturaMessaggi) {
    this.notificaLetturaMessaggi = notificaLetturaMessaggi;
  }

  /**
   * Il numero di pazienti abilitati
   **/
  

  @JsonProperty("n_pazienti_abilitati") 
 
  public Integer getNPazientiAbilitati() {
    return nPazientiAbilitati;
  }
  public void setNPazientiAbilitati(Integer nPazientiAbilitati) {
    this.nPazientiAbilitati = nPazientiAbilitati;
  }

  /**
   * Il numero di pazienti bloccati
   **/
  

  @JsonProperty("n_pazienti_bloccati") 
 
  public Integer getNPazientiBloccati() {
    return nPazientiBloccati;
  }
  public void setNPazientiBloccati(Integer nPazientiBloccati) {
    this.nPazientiBloccati = nPazientiBloccati;
  }

  /**
   * Indica se Ã¨ in corso una richiesta di abilitazione tutti gli assistiti
   **/
  

  @JsonProperty("abilitazione_assistiti") 
 
  public Boolean isAbilitazioneAssistiti() {
    return abilitazioneAssistiti;
  }
  public void setAbilitazioneAssistiti(Boolean abilitazioneAssistiti) {
    this.abilitazioneAssistiti = abilitazioneAssistiti;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ModelMedicoInfo modelMedicoInfo = (ModelMedicoInfo) o;
    return Objects.equals(dataInizioAdesione, modelMedicoInfo.dataInizioAdesione) &&
        Objects.equals(dataFineAdesione, modelMedicoInfo.dataFineAdesione) &&
        Objects.equals(notificaLetturaMessaggi, modelMedicoInfo.notificaLetturaMessaggi) &&
        Objects.equals(nPazientiAbilitati, modelMedicoInfo.nPazientiAbilitati) &&
        Objects.equals(nPazientiBloccati, modelMedicoInfo.nPazientiBloccati) &&
        Objects.equals(abilitazioneAssistiti, modelMedicoInfo.abilitazioneAssistiti);
  }

  @Override
  public int hashCode() {
    return Objects.hash(dataInizioAdesione, dataFineAdesione, notificaLetturaMessaggi, nPazientiAbilitati, nPazientiBloccati, abilitazioneAssistiti);
  }

  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class ModelMedicoInfo {\n");
    
    sb.append("    dataInizioAdesione: ").append(toIndentedString(dataInizioAdesione)).append("\n");
    sb.append("    dataFineAdesione: ").append(toIndentedString(dataFineAdesione)).append("\n");
    sb.append("    notificaLetturaMessaggi: ").append(toIndentedString(notificaLetturaMessaggi)).append("\n");
    sb.append("    nPazientiAbilitati: ").append(toIndentedString(nPazientiAbilitati)).append("\n");
    sb.append("    nPazientiBloccati: ").append(toIndentedString(nPazientiBloccati)).append("\n");
    sb.append("    abilitazioneAssistiti: ").append(toIndentedString(abilitazioneAssistiti)).append("\n");
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

