/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.sanitx.sanitxint.ws;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.csi.aura.auraws.services.central.contattodigitale.EnsResponse;


/**
 * <p>Classe Java per utRecAssistitoVO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="utRecAssistitoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ContattoDigitale.central.services.auraws.aura.csi.it}Ens_Response"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="codiceFiscale" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="cognome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="nome" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="sesso" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="dataNascita" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="codiceComuneNasc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="descrizioneComuneNasc" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="idAura" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "utRecAssistitoVO", propOrder = {
    "codiceFiscale",
    "cognome",
    "nome",
    "sesso",
    "dataNascita",
    "codiceComuneNasc",
    "descrizioneComuneNasc",
    "idAura"
})
public class UtRecAssistitoVO
    extends EnsResponse
{

    protected String codiceFiscale;
    protected String cognome;
    protected String nome;
    protected String sesso;
    protected String dataNascita;
    protected String codiceComuneNasc;
    protected String descrizioneComuneNasc;
    protected BigDecimal idAura;

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
     *     {@link String }
     *     
     */
    public String getDataNascita() {
        return dataNascita;
    }

    /**
     * Imposta il valore della proprietÃ  dataNascita.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDataNascita(String value) {
        this.dataNascita = value;
    }

    /**
     * Recupera il valore della proprietÃ  codiceComuneNasc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getCodiceComuneNasc() {
        return codiceComuneNasc;
    }

    /**
     * Imposta il valore della proprietÃ  codiceComuneNasc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setCodiceComuneNasc(String value) {
        this.codiceComuneNasc = value;
    }

    /**
     * Recupera il valore della proprietÃ  descrizioneComuneNasc.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getDescrizioneComuneNasc() {
        return descrizioneComuneNasc;
    }

    /**
     * Imposta il valore della proprietÃ  descrizioneComuneNasc.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setDescrizioneComuneNasc(String value) {
        this.descrizioneComuneNasc = value;
    }

    /**
     * Recupera il valore della proprietÃ  idAura.
     * 
     * @return
     *     possible object is
     *     {@link BigDecimal }
     *     
     */
    public BigDecimal getIdAura() {
        return idAura;
    }

    /**
     * Imposta il valore della proprietÃ  idAura.
     * 
     * @param value
     *     allowed object is
     *     {@link BigDecimal }
     *     
     */
    public void setIdAura(BigDecimal value) {
        this.idAura = value;
    }

}
