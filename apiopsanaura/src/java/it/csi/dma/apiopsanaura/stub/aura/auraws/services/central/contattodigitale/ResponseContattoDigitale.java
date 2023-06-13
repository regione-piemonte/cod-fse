/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.contattodigitale;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Response.ContattoDigitale complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Response.ContattoDigitale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.sanitxint.sanitx.csi.it/}Ens_Response"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="elencoAssistiti" type="{http://ws.sanitxint.sanitx.csi.it/}utRecAssistitoVO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *         &lt;element name="errorCode" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="errorMessage" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="totalPage" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
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
@XmlType(name = "Response.ContattoDigitale", propOrder = {
    "elencoAssistiti",
    "errorCode",
    "errorMessage",
    "totalPage",
    "retVal"
})
public class ResponseContattoDigitale
    extends EnsResponse
{

    @XmlElement(nillable = true)
    protected List<UtRecAssistitoVO> elencoAssistiti;
    protected String errorCode;
    protected String errorMessage;
    protected BigDecimal totalPage;
    protected BigDecimal retVal;

    /**
     * Gets the value of the elencoAssistiti property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the elencoAssistiti property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getElencoAssistiti().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UtRecAssistitoVO }
     * 
     * 
     */
    public List<UtRecAssistitoVO> getElencoAssistiti() {
        if (elencoAssistiti == null) {
            elencoAssistiti = new ArrayList<UtRecAssistitoVO>();
        }
        return this.elencoAssistiti;
    }

    /**
     * Recupera il valore della proprietÃ  errorCode.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public String getErrorCode() {
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
    public void setErrorCode(String value) {
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
     * Recupera il valore della proprietÃ  totalPage.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getTotalPage() {
        return totalPage;
    }

    /**
     * Imposta il valore della proprietÃ  totalPage.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setTotalPage(BigDecimal value) {
        this.totalPage = value;
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
