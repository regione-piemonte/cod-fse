/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.elencoesenzioni;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

import it.csi.sanitx.sanitxint.ws.UtRecEsenzioniVO;


/**
 * <p>Classe Java per Ens_Response complex type.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * 
 * <pre>
 * &lt;complexType name="Ens_Response"&gt;
 *   &lt;complexContent&gt;
 *     &lt;extension base="{http://ElencoEsenzioni.central.services.auraws.aura.csi.it}Ens_Messagebody"&gt;
 *     &lt;/extension&gt;
 *   &lt;/complexContent&gt;
 * &lt;/complexType&gt;
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Ens_Response")
@XmlSeeAlso({
    ResponseElencoEsenzioni.class,
    UtRecEsenzioniVO.class
})
public class EnsResponse
    extends EnsMessagebody
{


}
