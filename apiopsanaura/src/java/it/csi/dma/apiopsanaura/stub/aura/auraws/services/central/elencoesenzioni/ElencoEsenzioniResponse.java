/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni;

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
 *         &lt;element name="return" type="{http://ws.sanitxint.sanitx.csi.it/}Response.ElencoEsenzioni"/&gt;
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
    "_return"
})
@XmlRootElement(name = "ElencoEsenzioniResponse")
public class ElencoEsenzioniResponse {

    @XmlElement(name = "return", required = true)
    protected ResponseElencoEsenzioni _return;

    /**
     * Recupera il valore della proprietÃ  return.
     * 
     * @return
     *     possible object is
     *     {@link ResponseElencoEsenzioni }
     *     
     */
    public ResponseElencoEsenzioni getReturn() {
        return _return;
    }

    /**
     * Imposta il valore della proprietÃ  return.
     * 
     * @param value
     *     allowed object is
     *     {@link ResponseElencoEsenzioni }
     *     
     */
    public void setReturn(ResponseElencoEsenzioni value) {
        this._return = value;
    }

}
