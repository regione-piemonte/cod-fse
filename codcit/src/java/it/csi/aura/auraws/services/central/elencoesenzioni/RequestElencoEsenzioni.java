/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.elencoesenzioni;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per Request.ElencoEsenzioni complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Request.ElencoEsenzioni"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ElencoEsenzioni.central.services.auraws.aura.csi.it}Ens_Request"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="tipoEsen" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *         &lt;element name="listaDiagnosi" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Request.ElencoEsenzioni", propOrder = {
    "tipoEsen",
    "listaDiagnosi"
})
public class RequestElencoEsenzioni
    extends EnsRequest
{

    protected String tipoEsen;
    protected String listaDiagnosi;

    /**
     * Recupera il valore della proprietÃ  tipoEsen.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getTipoEsen() {
        return tipoEsen;
    }

    /**
     * Imposta il valore della proprietÃ  tipoEsen.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setTipoEsen(String value) {
        this.tipoEsen = value;
    }

    /**
     * Recupera il valore della proprietÃ  listaDiagnosi.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getListaDiagnosi() {
        return listaDiagnosi;
    }

    /**
     * Imposta il valore della proprietÃ  listaDiagnosi.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setListaDiagnosi(String value) {
        this.listaDiagnosi = value;
    }

}
