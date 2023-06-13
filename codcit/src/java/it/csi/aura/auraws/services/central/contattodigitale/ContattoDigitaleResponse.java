/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.contattodigitale;

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
 *         &lt;element name="ContattoDigitaleResult" type="{http://ContattoDigitale.central.services.auraws.aura.csi.it}Response.ContattoDigitale"/&gt;
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
    "contattoDigitaleResult"
})
@XmlRootElement(name = "ContattoDigitaleResponse")
public class ContattoDigitaleResponse {

    @XmlElement(name = "ContattoDigitaleResult", required = true)
    protected ResponseContattoDigitale contattoDigitaleResult;

    /**
     * Recupera il valore della proprietÃ  contattoDigitaleResult.
     * 
     * @return
     *     possible object is
     *     {@link ResponseContattoDigitale }
     *     
     */
    public ResponseContattoDigitale getContattoDigitaleResult() {
        return contattoDigitaleResult;
    }

    /**
     * Imposta il valore della proprietÃ  contattoDigitaleResult.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseContattoDigitale }
     *     
     */
    public void setContattoDigitaleResult(ResponseContattoDigitale value) {
        this.contattoDigitaleResult = value;
    }

}
