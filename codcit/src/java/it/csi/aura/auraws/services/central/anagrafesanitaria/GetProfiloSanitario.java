/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.anagrafesanitaria;

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
 *         &lt;element name="AURAid" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
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
    "aurAid"
})
@XmlRootElement(name = "GetProfiloSanitario")
public class GetProfiloSanitario {

    @XmlElement(name = "AURAid")
    protected String aurAid;

    /**
     * Recupera il valore della proprietÃ  aurAid.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getAURAid() {
        return aurAid;
    }

    /**
     * Imposta il valore della proprietÃ  aurAid.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setAURAid(String value) {
        this.aurAid = value;
    }

}
