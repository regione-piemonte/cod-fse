/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apicodopsan.verificaservices.dmacc;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Classe Java per risultatoCodice.
 * 
 * <p>Il seguente frammento di schema specifica il contenuto previsto contenuto in questa classe.
 * <pre>
 * &lt;simpleType name="risultatoCodice"&gt;
 *   &lt;restriction base="{http://www.w3.org/2001/XMLSchema}string"&gt;
 *     &lt;enumeration value="SUCCESSO"/&gt;
 *     &lt;enumeration value="FALLIMENTO"/&gt;
 *   &lt;/restriction&gt;
 * &lt;/simpleType&gt;
 * </pre>
 * 
 */
@XmlType(name = "risultatoCodice")
@XmlEnum
public enum RisultatoCodice {

    SUCCESSO,
    FALLIMENTO;

    public String value() {
        return name();
    }

    public static RisultatoCodice fromValue(String v) {
        return valueOf(v);
    }

}
