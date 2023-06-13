/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per utRecEsenzioniVO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="utRecEsenzioniVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ws.sanitxint.sanitx.csi.it/}Ens_Response"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codDiagnosi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codEsenzione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataFineValidita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataInizioValidita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="descDiagnosi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descEsenzione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="tipoEsenzione" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "utRecEsenzioniVO", propOrder = {
    "codDiagnosi",
    "codEsenzione",
    "dataFineValidita",
    "dataInizioValidita",
    "descDiagnosi",
    "descEsenzione",
    "tipoEsenzione"
})
public class UtRecEsenzioniVO
    extends EnsResponse
{

    protected String codDiagnosi;
    protected String codEsenzione;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataFineValidita;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataInizioValidita;
    protected String descDiagnosi;
    protected String descEsenzione;
    protected String tipoEsenzione;

    /**
     * Recupera il valore della proprietÃ  codDiagnosi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodDiagnosi() {
        return codDiagnosi;
    }

    /**
     * Imposta il valore della proprietÃ  codDiagnosi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodDiagnosi(String value) {
        this.codDiagnosi = value;
    }

    /**
     * Recupera il valore della proprietÃ  codEsenzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodEsenzione() {
        return codEsenzione;
    }

    /**
     * Imposta il valore della proprietÃ  codEsenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodEsenzione(String value) {
        this.codEsenzione = value;
    }

    /**
     * Recupera il valore della proprietÃ  dataFineValidita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataFineValidita() {
        return dataFineValidita;
    }

    /**
     * Imposta il valore della proprietÃ  dataFineValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataFineValidita(XMLGregorianCalendar value) {
        this.dataFineValidita = value;
    }

    /**
     * Recupera il valore della proprietÃ  dataInizioValidita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataInizioValidita() {
        return dataInizioValidita;
    }

    /**
     * Imposta il valore della proprietÃ  dataInizioValidita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataInizioValidita(XMLGregorianCalendar value) {
        this.dataInizioValidita = value;
    }

    /**
     * Recupera il valore della proprietÃ  descDiagnosi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescDiagnosi() {
        return descDiagnosi;
    }

    /**
     * Imposta il valore della proprietÃ  descDiagnosi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescDiagnosi(String value) {
        this.descDiagnosi = value;
    }

    /**
     * Recupera il valore della proprietÃ  descEsenzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescEsenzione() {
        return descEsenzione;
    }

    /**
     * Imposta il valore della proprietÃ  descEsenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescEsenzione(String value) {
        this.descEsenzione = value;
    }

    /**
     * Recupera il valore della proprietÃ  tipoEsenzione.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEsenzione() {
        return tipoEsenzione;
    }

    /**
     * Imposta il valore della proprietÃ  tipoEsenzione.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEsenzione(String value) {
        this.tipoEsenzione = value;
    }

}
