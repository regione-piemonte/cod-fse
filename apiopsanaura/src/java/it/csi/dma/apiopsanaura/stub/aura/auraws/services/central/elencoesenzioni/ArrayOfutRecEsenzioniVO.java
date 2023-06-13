/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apiopsanaura.stub.aura.auraws.services.central.elencoesenzioni;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ArrayOfutRecEsenzioniVO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfutRecEsenzioniVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="utRecEsenzioniVO" type="{http://ws.sanitxint.sanitx.csi.it/}utRecEsenzioniVO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfutRecEsenzioniVO", propOrder = {
    "utRecEsenzioniVO"
})
public class ArrayOfutRecEsenzioniVO {

    @XmlElement(nillable = true)
    protected List<UtRecEsenzioniVO> utRecEsenzioniVO;

    /**
     * Gets the value of the utRecEsenzioniVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the utRecEsenzioniVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUtRecEsenzioniVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UtRecEsenzioniVO }
     * 
     * 
     */
    public List<UtRecEsenzioniVO> getUtRecEsenzioniVO() {
        if (utRecEsenzioniVO == null) {
            utRecEsenzioniVO = new ArrayList<UtRecEsenzioniVO>();
        }
        return this.utRecEsenzioniVO;
    }

}
