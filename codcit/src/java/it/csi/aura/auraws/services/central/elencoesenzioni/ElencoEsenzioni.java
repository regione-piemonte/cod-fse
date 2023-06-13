/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.elencoesenzioni;

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
 *         &lt;element name="pRequest" type="{http://ElencoEsenzioni.central.services.auraws.aura.csi.it}Request.ElencoEsenzioni" minOccurs="0"/&gt;
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
@XmlRootElement(name = "ElencoEsenzioni")
public class ElencoEsenzioni {

    protected RequestElencoEsenzioni pRequest;

    /**
     * Recupera il valore della proprietÃ  pRequest.
     * 
     * @return
     *     possible object is
     *     {@link RequestElencoEsenzioni }
     *     
     */
    public RequestElencoEsenzioni getPRequest() {
        return pRequest;
    }

    /**
     * Imposta il valore della proprietÃ  pRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link RequestElencoEsenzioni }
     *     
     */
    public void setPRequest(RequestElencoEsenzioni value) {
        this.pRequest = value;
    }

}
