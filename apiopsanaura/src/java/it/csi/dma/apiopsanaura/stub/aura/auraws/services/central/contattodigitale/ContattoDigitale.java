/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
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
 *         &lt;element name="pRequest" type="{http://ws.sanitxint.sanitx.csi.it/}Request.ContattoDigitale" minOccurs="0"/&gt;
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
    "pRequest"
})
@XmlRootElement(name = "ContattoDigitale")
public class ContattoDigitale {

    protected RequestContattoDigitale pRequest;

    /**
     * Recupera il valore della proprietÃ  pRequest.
     * 
     * @return
     *     possible object is
     *     {@link RequestContattoDigitale }
     *     
     */
    public RequestContattoDigitale getPRequest() {
        return pRequest;
    }

    /**
     * Imposta il valore della proprietÃ  pRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestContattoDigitale }
     *     
     */
    public void setPRequest(RequestContattoDigitale value) {
        this.pRequest = value;
    }

}
