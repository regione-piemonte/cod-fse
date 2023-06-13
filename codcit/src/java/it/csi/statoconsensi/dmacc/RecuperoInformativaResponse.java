/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.statoconsensi.dmacc;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import it.csi.statoconsensi.dma.InformativaOUT;


/**
 * <p>Classe Java per recuperoInformativaResponse complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="recuperoInformativaResponse"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://dmacc.csi.it/}serviceResponse"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="informativaOUT" type="{http://dma.csi.it/}informativaOUT" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "recuperoInformativaResponse", propOrder = {
    "informativaOUT"
})
public class RecuperoInformativaResponse
    extends ServiceResponse
{

    protected InformativaOUT informativaOUT;

    /**
     * Recupera il valore della proprietÃ  informativaOUT.
     * 
     * @return
     *     possible object is
     *     {@link InformativaOUT }
     *     
     */
    public InformativaOUT getInformativaOUT() {
        return informativaOUT;
    }

    /**
     * Imposta il valore della proprietÃ  informativaOUT.
     * 
     * @param value
     *     allowed object is
     *     {@link InformativaOUT }
     *     
     */
    public void setInformativaOUT(InformativaOUT value) {
        this.informativaOUT = value;
    }

}
