/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apicodopsan.verificaservices.dmacc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.csi.dma.apicodopsan.verificaservices.dma.DatiDocumento;
import it.csi.dma.apicodopsan.verificaservices.dma.RichiedenteInfo;


/**
 * <p>Classe Java per VerificaPinRequest complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="VerificaPinRequest"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="richiedente" type="{http://dma.csi.it/}richiedenteInfo" minOccurs="0"/&gt;
 *         &lt;element name="datiDocumento" type="{http://dma.csi.it/}datiDocumento" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "VerificaPinRequest", propOrder = {
    "richiedente",
    "datiDocumento"
})
public class VerificaPinRequest {

    protected RichiedenteInfo richiedente;
    protected DatiDocumento datiDocumento;

    /**
     * Recupera il valore della proprietï¿½ richiedente.
     * 
     * @return
     *     possible object is
     *     {@link RichiedenteInfo }
     *     
     */
    public RichiedenteInfo getRichiedente() {
        return richiedente;
    }

    /**
     * Imposta il valore della proprietï¿½ richiedente.
     * 
     * @param value
     *     allowed object is
     *     {@link RichiedenteInfo }
     *     
     */
    public void setRichiedente(RichiedenteInfo value) {
        this.richiedente = value;
    }

    /**
     * Recupera il valore della proprietï¿½ datiDocumento.
     * 
     * @return
     *     possible object is
     *     {@link DatiDocumento }
     *     
     */
    public DatiDocumento getDatiDocumento() {
        return datiDocumento;
    }

    /**
     * Imposta il valore della proprietï¿½ datiDocumento.
     * 
     * @param value
     *     allowed object is
     *     {@link DatiDocumento }
     *     
     */
    public void setDatiDocumento(DatiDocumento value) {
        this.datiDocumento = value;
    }

}
