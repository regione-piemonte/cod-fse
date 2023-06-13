/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.anagrafesanitaria;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per soggettoAuraMsg complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="soggettoAuraMsg"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}Ens_Response"&gt;
 *       &lt;sequence&gt;
 *         &lt;element name="header" type="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}Header" minOccurs="0"/&gt;
 *         &lt;element name="body" type="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}SoggettoAuraBody" minOccurs="0"/&gt;
 *         &lt;element name="footer" type="{http://AnagrafeSanitaria.central.services.auraws.aura.csi.it}Footer" minOccurs="0"/&gt;
 *       &lt;/sequence&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "soggettoAuraMsg", propOrder = {
    "header",
    "body",
    "footer"
})
public class SoggettoAuraMsg
    extends EnsResponse
{

    protected Header header;
    protected SoggettoAuraBody body;
    protected Footer footer;

    /**
     * Recupera il valore della proprietÃ  header.
     * 
     * @return
     *     possible object is
     *     {@link Header }
     *     
     */
    public Header getHeader() {
        return header;
    }

    /**
     * Imposta il valore della proprietÃ  header.
     * 
     * @param value
     *     allowed object is
     *     {@link Header }
     *     
     */
    public void setHeader(Header value) {
        this.header = value;
    }

    /**
     * Recupera il valore della proprietÃ  body.
     * 
     * @return
     *     possible object is
     *     {@link SoggettoAuraBody }
     *     
     */
    public SoggettoAuraBody getBody() {
        return body;
    }

    /**
     * Imposta il valore della proprietÃ  body.
     * 
     * @param value
     *     allowed object is
     *     {@link SoggettoAuraBody }
     *     
     */
    public void setBody(SoggettoAuraBody value) {
        this.body = value;
    }

    /**
     * Recupera il valore della proprietÃ  footer.
     * 
     * @return
     *     possible object is
     *     {@link Footer }
     *     
     */
    public Footer getFooter() {
        return footer;
    }

    /**
     * Imposta il valore della proprietÃ  footer.
     * 
     * @param value
     *     allowed object is
     *     {@link Footer }
     *     
     */
    public void setFooter(Footer value) {
        this.footer = value;
    }

}
