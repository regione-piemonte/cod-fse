/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.anagrafesanitaria;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per DatiPrimari complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiPrimari"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codCittadinanza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataDecesso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="descCittadinanza" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="siglaProvNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="statoCodiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="statoProfiloAnagrafico" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiPrimari", propOrder = {
    "codCittadinanza",
    "codComuneNascita",
    "codStatoNascita",
    "codiceFiscale",
    "cognome",
    "dataDecesso",
    "dataNascita",
    "descCittadinanza",
    "descComuneNascita",
    "descStatoNascita",
    "nome",
    "sesso",
    "siglaProvNascita",
    "statoCodiceFiscale",
    "statoProfiloAnagrafico"
})
public class DatiPrimari {

    protected String codCittadinanza;
    protected String codComuneNascita;
    protected String codStatoNascita;
    protected String codiceFiscale;
    protected String cognome;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataDecesso;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataNascita;
    protected String descCittadinanza;
    protected String descComuneNascita;
    protected String descStatoNascita;
    protected String nome;
    protected String sesso;
    protected String siglaProvNascita;
    protected String statoCodiceFiscale;
    protected String statoProfiloAnagrafico;

    /**
     * Recupera il valore della proprietÃ  codCittadinanza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodCittadinanza() {
        return codCittadinanza;
    }

    /**
     * Imposta il valore della proprietÃ  codCittadinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodCittadinanza(String value) {
        this.codCittadinanza = value;
    }

    /**
     * Recupera il valore della proprietÃ  codComuneNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodComuneNascita() {
        return codComuneNascita;
    }

    /**
     * Imposta il valore della proprietÃ  codComuneNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodComuneNascita(String value) {
        this.codComuneNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  codStatoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodStatoNascita() {
        return codStatoNascita;
    }

    /**
     * Imposta il valore della proprietÃ  codStatoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodStatoNascita(String value) {
        this.codStatoNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  codiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceFiscale() {
        return codiceFiscale;
    }

    /**
     * Imposta il valore della proprietÃ  codiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceFiscale(String value) {
        this.codiceFiscale = value;
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
     * Recupera il valore della proprietÃ  dataDecesso.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataDecesso() {
        return dataDecesso;
    }

    /**
     * Imposta il valore della proprietÃ  dataDecesso.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataDecesso(XMLGregorianCalendar value) {
        this.dataDecesso = value;
    }

    /**
     * Recupera il valore della proprietÃ  dataNascita.
     * 
     * @return
     *     possible object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public XMLGregorianCalendar getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta il valore della proprietÃ  dataNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link XMLGregorianCalendar }
     *     
     */
    public void setDataNascita(XMLGregorianCalendar value) {
        this.dataNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  descCittadinanza.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescCittadinanza() {
        return descCittadinanza;
    }

    /**
     * Imposta il valore della proprietÃ  descCittadinanza.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescCittadinanza(String value) {
        this.descCittadinanza = value;
    }

    /**
     * Recupera il valore della proprietÃ  descComuneNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescComuneNascita() {
        return descComuneNascita;
    }

    /**
     * Imposta il valore della proprietÃ  descComuneNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescComuneNascita(String value) {
        this.descComuneNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  descStatoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescStatoNascita() {
        return descStatoNascita;
    }

    /**
     * Imposta il valore della proprietÃ  descStatoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescStatoNascita(String value) {
        this.descStatoNascita = value;
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
     * Recupera il valore della proprietÃ  siglaProvNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getSiglaProvNascita() {
        return siglaProvNascita;
    }

    /**
     * Imposta il valore della proprietÃ  siglaProvNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setSiglaProvNascita(String value) {
        this.siglaProvNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  statoCodiceFiscale.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatoCodiceFiscale() {
        return statoCodiceFiscale;
    }

    /**
     * Imposta il valore della proprietÃ  statoCodiceFiscale.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatoCodiceFiscale(String value) {
        this.statoCodiceFiscale = value;
    }

    /**
     * Recupera il valore della proprietÃ  statoProfiloAnagrafico.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatoProfiloAnagrafico() {
        return statoProfiloAnagrafico;
    }

    /**
     * Imposta il valore della proprietÃ  statoProfiloAnagrafico.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatoProfiloAnagrafico(String value) {
        this.statoProfiloAnagrafico = value;
    }

}
