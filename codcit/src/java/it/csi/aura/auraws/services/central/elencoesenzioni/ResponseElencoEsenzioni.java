/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.elencoesenzioni;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import it.csi.sanitx.sanitxint.ws.UtRecEsenzioniVO;


/**
 * <p>Classe Java per Response.ElencoEsenzioni complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Response.ElencoEsenzioni"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ElencoEsenzioni.central.services.auraws.aura.csi.it}Ens_Response"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elencoEsenzioni" type="{http://ws.sanitxint.sanitx.csi.it/}utRecEsenzioniVO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="retVal" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Response.ElencoEsenzioni", propOrder = {
    "elencoEsenzioni",
    "errorCode",
    "errorMessage",
    "retVal"
})
public class ResponseElencoEsenzioni
    extends EnsResponse
{

    @XmlElement(nillable = true)
    protected List<UtRecEsenzioniVO> elencoEsenzioni;
    protected BigDecimal errorCode;
    protected String errorMessage;
    protected BigDecimal retVal;

    /**
     * Gets the value of the elencoEsenzioni property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoEsenzioni property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoEsenzioni().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UtRecEsenzioniVO }
     * 
     * 
     */
    public List<UtRecEsenzioniVO> getElencoEsenzioni() {
        if (elencoEsenzioni == null) {
            elencoEsenzioni = new ArrayList<UtRecEsenzioniVO>();
        }
        return this.elencoEsenzioni;
    }

    /**
     * Recupera il valore della proprietÃ  errorCode.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getErrorCode() {
        return errorCode;
    }

    /**
     * Imposta il valore della proprietÃ  errorCode.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setErrorCode(BigDecimal value) {
        this.errorCode = value;
    }

    /**
     * Recupera il valore della proprietÃ  errorMessage.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Imposta il valore della proprietÃ  errorMessage.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setErrorMessage(String value) {
        this.errorMessage = value;
    }

    /**
     * Recupera il valore della proprietÃ  retVal.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getRetVal() {
        return retVal;
    }

    /**
     * Imposta il valore della proprietÃ  retVal.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setRetVal(BigDecimal value) {
        this.retVal = value;
    }

}
