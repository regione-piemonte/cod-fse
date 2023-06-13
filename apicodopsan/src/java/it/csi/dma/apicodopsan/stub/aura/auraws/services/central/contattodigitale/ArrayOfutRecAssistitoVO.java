/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apicodopsan.stub.aura.auraws.services.central.contattodigitale;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per ArrayOfutRecAssistitoVO complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="ArrayOfutRecAssistitoVO"&gt;
 *   &lt;complexContent&gt;
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="utRecAssistitoVO" type="{http://ws.sanitxint.sanitx.csi.it/}utRecAssistitoVO" maxOccurs="unbounded" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/restriction&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArrayOfutRecAssistitoVO", propOrder = {
    "utRecAssistitoVO"
})
public class ArrayOfutRecAssistitoVO {

    @XmlElement(nillable = true)
    protected List<UtRecAssistitoVO> utRecAssistitoVO;

    /**
     * Gets the value of the utRecAssistitoVO property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the utRecAssistitoVO property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getUtRecAssistitoVO().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link UtRecAssistitoVO }
     * 
     * 
     */
    public List<UtRecAssistitoVO> getUtRecAssistitoVO() {
        if (utRecAssistitoVO == null) {
            utRecAssistitoVO = new ArrayList<UtRecAssistitoVO>();
        }
        return this.utRecAssistitoVO;
    }

}
