/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.aura.auraws.services.central.elencoesenzioni;

import javax.xml.bind.annotation.XmlRegistry;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.aura.auraws.services.central.elencoesenzioni package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {


    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.aura.auraws.services.central.elencoesenzioni
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ElencoEsenzioni }
     * 
     */
    public ElencoEsenzioni createElencoEsenzioni() {
        return new ElencoEsenzioni();
    }

    /**
     * Create an instance of {@link RequestElencoEsenzioni }
     * 
     */
    public RequestElencoEsenzioni createRequestElencoEsenzioni() {
        return new RequestElencoEsenzioni();
    }

    /**
     * Create an instance of {@link ElencoEsenzioniResponse }
     * 
     */
    public ElencoEsenzioniResponse createElencoEsenzioniResponse() {
        return new ElencoEsenzioniResponse();
    }

    /**
     * Create an instance of {@link ResponseElencoEsenzioni }
     * 
     */
    public ResponseElencoEsenzioni createResponseElencoEsenzioni() {
        return new ResponseElencoEsenzioni();
    }

    /**
     * Create an instance of {@link EnsRequest }
     * 
     */
    public EnsRequest createEnsRequest() {
        return new EnsRequest();
    }

    /**
     * Create an instance of {@link EnsMessagebody }
     * 
     */
    public EnsMessagebody createEnsMessagebody() {
        return new EnsMessagebody();
    }

    /**
     * Create an instance of {@link EnsResponse }
     * 
     */
    public EnsResponse createEnsResponse() {
        return new EnsResponse();
    }

    /**
     * Create an instance of {@link ArrayOfutRecEsenzioniVO }
     * 
     */
    public ArrayOfutRecEsenzioniVO createArrayOfutRecEsenzioniVO() {
        return new ArrayOfutRecEsenzioniVO();
    }

}
