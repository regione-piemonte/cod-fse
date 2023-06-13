/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.anagrafefind;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;


/**
 * <p>Classe Java per DatiAnagrafici complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="DatiAnagrafici"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idProfiloAnagrafico" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="codiceFiscale" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="[a-zA-Z]{6}\d{2}[a-zA-Z]\d{2}[a-zA-Z]\d{3}[a-zA-Z]"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="cognome" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;maxLength value="50"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sesso" minOccurs="0"&gt;
 *           &lt;simpleType&gt;
 *             &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *               &lt;pattern value="M|F|I"/&gt;
 *             &lt;/restriction&gt;
 *           &lt;/simpleType&gt;
 *         &lt;/element&gt;
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *         &lt;element name="codiceStatoNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="statoNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="comuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceComuneNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="provinciaNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataDecesso" type="{http://www.w3.org/2001/XMLSchema}dateTime" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DatiAnagrafici", propOrder = {
    "idProfiloAnagrafico",
    "codiceFiscale",
    "cognome",
    "nome",
    "sesso",
    "dataNascita",
    "codiceStatoNascita",
    "statoNascita",
    "comuneNascita",
    "codiceComuneNascita",
    "provinciaNascita",
    "dataDecesso"
})
public class DatiAnagrafici {

    protected BigDecimal idProfiloAnagrafico;
    protected String codiceFiscale;
    protected String cognome;
    protected String nome;
    protected String sesso;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataNascita;
    protected String codiceStatoNascita;
    protected String statoNascita;
    protected String comuneNascita;
    protected String codiceComuneNascita;
    protected String provinciaNascita;
    @XmlSchemaType(name = "dateTime")
    protected XMLGregorianCalendar dataDecesso;

    /**
     * Recupera il valore della proprietÃ  idProfiloAnagrafico.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIdProfiloAnagrafico() {
        return idProfiloAnagrafico;
    }

    /**
     * Imposta il valore della proprietÃ  idProfiloAnagrafico.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIdProfiloAnagrafico(BigDecimal value) {
        this.idProfiloAnagrafico = value;
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
     * Recupera il valore della proprietÃ  codiceStatoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceStatoNascita() {
        return codiceStatoNascita;
    }

    /**
     * Imposta il valore della proprietÃ  codiceStatoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceStatoNascita(String value) {
        this.codiceStatoNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  statoNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStatoNascita() {
        return statoNascita;
    }

    /**
     * Imposta il valore della proprietÃ  statoNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStatoNascita(String value) {
        this.statoNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  comuneNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getComuneNascita() {
        return comuneNascita;
    }

    /**
     * Imposta il valore della proprietÃ  comuneNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setComuneNascita(String value) {
        this.comuneNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  codiceComuneNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceComuneNascita() {
        return codiceComuneNascita;
    }

    /**
     * Imposta il valore della proprietÃ  codiceComuneNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceComuneNascita(String value) {
        this.codiceComuneNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  provinciaNascita.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getProvinciaNascita() {
        return provinciaNascita;
    }

    /**
     * Imposta il valore della proprietÃ  provinciaNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setProvinciaNascita(String value) {
        this.provinciaNascita = value;
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

}
