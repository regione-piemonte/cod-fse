/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.statoconsensi.dmacc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.csi.statoconsensi.dma.ComunicazioneConsensiIN;
import it.csi.statoconsensi.dma.Paziente;
import it.csi.statoconsensi.dma.RichiedenteExt;


/**
 * <p>Classe Java per comunicazioneConsensiExtRequeste complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="comunicazioneConsensiExtRequeste"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="comunicazioneConsensiIN" type="{http://dma.csi.it/}comunicazioneConsensiIN" minOccurs="0"/&gt;
 *         &lt;element name="paziente" type="{http://dma.csi.it/}paziente" minOccurs="0"/&gt;
 *         &lt;element name="richiedente" type="{http://dma.csi.it/}richiedenteExt" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "comunicazioneConsensiExtRequeste", propOrder = {
    "comunicazioneConsensiIN",
    "paziente",
    "richiedente"
})
public class ComunicazioneConsensiExtRequeste {

    protected ComunicazioneConsensiIN comunicazioneConsensiIN;
    protected Paziente paziente;
    protected RichiedenteExt richiedente;

    /**
     * Recupera il valore della proprietÃ  comunicazioneConsensiIN.
     * 
     * @return
     *     possible object is
     *     {@link ComunicazioneConsensiIN }
     *     
     */
    public ComunicazioneConsensiIN getComunicazioneConsensiIN() {
        return comunicazioneConsensiIN;
    }

    /**
     * Imposta il valore della proprietÃ  comunicazioneConsensiIN.
     * 
     * @param value
     *     allowed object is
     *     {@link ComunicazioneConsensiIN }
     *     
     */
    public void setComunicazioneConsensiIN(ComunicazioneConsensiIN value) {
        this.comunicazioneConsensiIN = value;
    }

    /**
     * Recupera il valore della proprietÃ  paziente.
     * 
     * @return
     *     possible object is
     *     {@link Paziente }
     *     
     */
    public Paziente getPaziente() {
        return paziente;
    }

    /**
     * Imposta il valore della proprietÃ  paziente.
     * 
     * @param value
     *     allowed object is
     *     {@link Paziente }
     *     
     */
    public void setPaziente(Paziente value) {
        this.paziente = value;
    }

    /**
     * Recupera il valore della proprietÃ  richiedente.
     * 
     * @return
     *     possible object is
     *     {@link RichiedenteExt }
     *     
     */
    public RichiedenteExt getRichiedente() {
        return richiedente;
    }

    /**
     * Imposta il valore della proprietÃ  richiedente.
     * 
     * @param value
     *     allowed object is
     *     {@link RichiedenteExt }
     *     
     */
    public void setRichiedente(RichiedenteExt value) {
        this.richiedente = value;
    }

}
