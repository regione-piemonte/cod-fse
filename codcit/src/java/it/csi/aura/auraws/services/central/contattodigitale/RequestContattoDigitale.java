/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.contattodigitale;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Request.ContattoDigitale complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Request.ContattoDigitale"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ContattoDigitale.central.services.auraws.aura.csi.it}Ens_Request"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codFiscMed" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codFiscAss" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="patologia" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="etaMin" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="etaMax" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="pageNumber" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Request.ContattoDigitale", propOrder = {
    "codFiscMed",
    "codFiscAss",
    "cognome",
    "nome",
    "sesso",
    "patologia",
    "etaMin",
    "etaMax",
    "pageNumber"
})
public class RequestContattoDigitale
    extends EnsRequest
{

    protected String codFiscMed;
    protected String codFiscAss;
    protected String cognome;
    protected String nome;
    protected String sesso;
    protected String patologia;
    protected BigDecimal etaMin;
    protected BigDecimal etaMax;
    protected BigDecimal pageNumber;

    /**
     * Recupera il valore della proprietÃ  codFiscMed.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFiscMed() {
        return codFiscMed;
    }

    /**
     * Imposta il valore della proprietÃ  codFiscMed.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFiscMed(String value) {
        this.codFiscMed = value;
    }

    /**
     * Recupera il valore della proprietÃ  codFiscAss.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodFiscAss() {
        return codFiscAss;
    }

    /**
     * Imposta il valore della proprietÃ  codFiscAss.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodFiscAss(String value) {
        this.codFiscAss = value;
    }

    /**
     * Recupera il valore della proprietÃ  cognome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCognome() {
        return cognome;
    }

    /**
     * Imposta il valore della proprietÃ  cognome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCognome(String value) {
        this.cognome = value;
    }

    /**
     * Recupera il valore della proprietÃ  nome.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getNome() {
        return nome;
    }

    /**
     * Imposta il valore della proprietÃ  nome.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setNome(String value) {
        this.nome = value;
    }

    /**
     * Recupera il valore della proprietÃ  sesso.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSesso() {
        return sesso;
    }

    /**
     * Imposta il valore della proprietÃ  sesso.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSesso(String value) {
        this.sesso = value;
    }

    /**
     * Recupera il valore della proprietÃ  patologia.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPatologia() {
        return patologia;
    }

    /**
     * Imposta il valore della proprietÃ  patologia.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPatologia(String value) {
        this.patologia = value;
    }

    /**
     * Recupera il valore della proprietÃ  etaMin.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEtaMin() {
        return etaMin;
    }

    /**
     * Imposta il valore della proprietÃ  etaMin.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEtaMin(BigDecimal value) {
        this.etaMin = value;
    }

    /**
     * Recupera il valore della proprietÃ  etaMax.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getEtaMax() {
        return etaMax;
    }

    /**
     * Imposta il valore della proprietÃ  etaMax.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setEtaMax(BigDecimal value) {
        this.etaMax = value;
    }

    /**
     * Recupera il valore della proprietÃ  pageNumber.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getPageNumber() {
        return pageNumber;
    }

    /**
     * Imposta il valore della proprietÃ  pageNumber.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setPageNumber(BigDecimal value) {
        this.pageNumber = value;
    }

}
