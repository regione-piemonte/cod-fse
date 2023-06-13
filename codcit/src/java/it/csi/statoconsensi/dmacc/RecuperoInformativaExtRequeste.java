/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.statoconsensi.dmacc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.csi.statoconsensi.dma.InformativaIN;
import it.csi.statoconsensi.dma.RichiedenteExt;


/**
 * <p>Classe Java per recuperoInformativaExtRequeste complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="recuperoInformativaExtRequeste"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="informativaIN" type="{http://dma.csi.it/}informativaIN" minOccurs="0"/&gt;
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
@XmlType(name = "recuperoInformativaExtRequeste", propOrder = {
    "informativaIN",
    "richiedente"
})
public class RecuperoInformativaExtRequeste {

    protected InformativaIN informativaIN;
    protected RichiedenteExt richiedente;

    /**
     * Recupera il valore della proprietÃ  informativaIN.
     * 
     * @return
     *     possible object is
     *     {@link InformativaIN }
     *     
     */
    public InformativaIN getInformativaIN() {
        return informativaIN;
    }

    /**
     * Imposta il valore della proprietÃ  informativaIN.
     * 
     * @param value
     *     allowed object is
     *     {@link InformativaIN }
     *     
     */
    public void setInformativaIN(InformativaIN value) {
        this.informativaIN = value;
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
