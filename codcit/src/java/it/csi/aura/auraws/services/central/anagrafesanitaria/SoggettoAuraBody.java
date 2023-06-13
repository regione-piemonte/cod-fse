/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.anagrafesanitaria;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per SoggettoAuraBody complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="SoggettoAuraBody"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="idAura" type="{http://www.w3.org/2001/XMLSchema}decimal" minOccurs="0"/&gt;
 *         &lt;element name="infoAnag" type="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}InfoAnagrafiche" minOccurs="0"/&gt;
 *         &lt;element name="infoSan" type="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}InfoSanitarie" minOccurs="0"/&gt;
 *         &lt;element name="infoEsenzioni" type="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}ArrayOfinfoesenzioneInfoEsenzione" minOccurs="0"/&gt;
 *         &lt;element name="altreInfo" type="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}ArrayOfinformazioniAltreInfo" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SoggettoAuraBody", propOrder = {
    "idAura",
    "infoAnag",
    "infoSan",
    "infoEsenzioni",
    "altreInfo"
})
public class SoggettoAuraBody {

    protected BigDecimal idAura;
    protected InfoAnagrafiche infoAnag;
    protected InfoSanitarie infoSan;
    protected ArrayOfinfoesenzioneInfoEsenzione infoEsenzioni;
    protected ArrayOfinformazioniAltreInfo altreInfo;

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

    /**
     * Recupera il valore della proprietÃ  infoAnag.
     * 
     * @return
     *     possible object is
     *     {@link InfoAnagrafiche }
     *     
     */
    public InfoAnagrafiche getInfoAnag() {
        return infoAnag;
    }

    /**
     * Imposta il valore della proprietÃ  infoAnag.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoAnagrafiche }
     *     
     */
    public void setInfoAnag(InfoAnagrafiche value) {
        this.infoAnag = value;
    }

    /**
     * Recupera il valore della proprietÃ  infoSan.
     * 
     * @return
     *     possible object is
     *     {@link InfoSanitarie }
     *     
     */
    public InfoSanitarie getInfoSan() {
        return infoSan;
    }

    /**
     * Imposta il valore della proprietÃ  infoSan.
     * 
     * @param value
     *     allowed object is
     *     {@link InfoSanitarie }
     *     
     */
    public void setInfoSan(InfoSanitarie value) {
        this.infoSan = value;
    }

    /**
     * Recupera il valore della proprietÃ  infoEsenzioni.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfinfoesenzioneInfoEsenzione }
     *     
     */
    public ArrayOfinfoesenzioneInfoEsenzione getInfoEsenzioni() {
        return infoEsenzioni;
    }

    /**
     * Imposta il valore della proprietÃ  infoEsenzioni.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfinfoesenzioneInfoEsenzione }
     *     
     */
    public void setInfoEsenzioni(ArrayOfinfoesenzioneInfoEsenzione value) {
        this.infoEsenzioni = value;
    }

    /**
     * Recupera il valore della proprietÃ  altreInfo.
     * 
     * @return
     *     possible object is
     *     {@link ArrayOfinformazioniAltreInfo }
     *     
     */
    public ArrayOfinformazioniAltreInfo getAltreInfo() {
        return altreInfo;
    }

    /**
     * Imposta il valore della proprietÃ  altreInfo.
     * 
     * @param value
     *     allowed object is
     *     {@link ArrayOfinformazioniAltreInfo }
     *     
     */
    public void setAltreInfo(ArrayOfinformazioniAltreInfo value) {
        this.altreInfo = value;
    }

}
