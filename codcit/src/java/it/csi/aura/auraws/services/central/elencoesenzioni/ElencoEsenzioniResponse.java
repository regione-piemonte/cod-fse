/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.elencoesenzioni;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per anonymous complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="ElencoEsenzioniResult" type="{http://ElencoEsenzioni.central.services.auraws.aura.csi.it}Response.ElencoEsenzioni"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "elencoEsenzioniResult"
})
@XmlRootElement(name = "ElencoEsenzioniResponse")
public class ElencoEsenzioniResponse {

    @XmlElement(name = "ElencoEsenzioniResult", required = true)
    protected ResponseElencoEsenzioni elencoEsenzioniResult;

    /**
     * Recupera il valore della proprietÃ  elencoEsenzioniResult.
     * 
     * @return
     *     possible object is
     *     {@link ResponseElencoEsenzioni }
     *     
     */
    public ResponseElencoEsenzioni getElencoEsenzioniResult() {
        return elencoEsenzioniResult;
    }

    /**
     * Imposta il valore della proprietÃ  elencoEsenzioniResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseElencoEsenzioni }
     *     
     */
    public void setElencoEsenzioniResult(ResponseElencoEsenzioni value) {
        this.elencoEsenzioniResult = value;
    }

}
