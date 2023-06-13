/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.statoconsensi.dma;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per componenteLocaleOperazione complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="componenteLocaleOperazione"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://dma.csi.it/}codifica"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="TimeoutMaxElaborazione" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "componenteLocaleOperazione", propOrder = {
    "timeoutMaxElaborazione"
})
public class ComponenteLocaleOperazione
    extends Codifica
{

    @XmlElement(name = "TimeoutMaxElaborazione")
    protected Long timeoutMaxElaborazione;

    /**
     * Recupera il valore della proprietÃ  timeoutMaxElaborazione.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTimeoutMaxElaborazione() {
        return timeoutMaxElaborazione;
    }

    /**
     * Imposta il valore della proprietÃ  timeoutMaxElaborazione.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTimeoutMaxElaborazione(Long value) {
        this.timeoutMaxElaborazione = value;
    }

}
