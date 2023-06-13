/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.anagrafefind;

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
 *         &lt;element name="FindProfiliAnagraficiResult" type="{http://AnagrafeFind.central.services.auraws.aura.csi.it}datiAnagraficiMsg"/&gt;
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
    "findProfiliAnagraficiResult"
})
@XmlRootElement(name = "FindProfiliAnagraficiResponse")
public class FindProfiliAnagraficiResponse {

    @XmlElement(name = "FindProfiliAnagraficiResult", required = true)
    protected DatiAnagraficiMsg findProfiliAnagraficiResult;

    /**
     * Recupera il valore della proprietÃ  findProfiliAnagraficiResult.
     * 
     * @return
     *     possible object is
     *     {@link DatiAnagraficiMsg }
     *     
     */
    public DatiAnagraficiMsg getFindProfiliAnagraficiResult() {
        return findProfiliAnagraficiResult;
    }

    /**
     * Imposta il valore della proprietÃ  findProfiliAnagraficiResult.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiAnagraficiMsg }
     *     
     */
    public void setFindProfiliAnagraficiResult(DatiAnagraficiMsg value) {
        this.findProfiliAnagraficiResult = value;
    }

}
