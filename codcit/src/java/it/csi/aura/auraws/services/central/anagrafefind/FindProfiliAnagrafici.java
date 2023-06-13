/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.anagrafefind;

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
 *         &lt;element name="profiliRequest" type="{http://AnagrafeFind.central.services.auraws.aura.csi.it}findProfiliAnagraficiRequest" minOccurs="0"/&gt;
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
    "profiliRequest"
})
@XmlRootElement(name = "FindProfiliAnagrafici")
public class FindProfiliAnagrafici {

    protected FindProfiliAnagraficiRequest profiliRequest;

    /**
     * Recupera il valore della proprietÃ  profiliRequest.
     * 
     * @return
     *     possible object is
     *     {@link FindProfiliAnagraficiRequest }
     *     
     */
    public FindProfiliAnagraficiRequest getProfiliRequest() {
        return profiliRequest;
    }

    /**
     * Imposta il valore della proprietÃ  profiliRequest.
     * 
     * @param value
     *     allowed object is
     *     {@link FindProfiliAnagraficiRequest }
     *     
     */
    public void setProfiliRequest(FindProfiliAnagraficiRequest value) {
        this.profiliRequest = value;
    }

}
