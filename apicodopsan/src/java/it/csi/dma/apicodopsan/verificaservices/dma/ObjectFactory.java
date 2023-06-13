/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/


package it.csi.dma.apicodopsan.verificaservices.dma;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;

import it.csi.dma.apicodopsan.verificaservices.dmacc.Codifica;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the it.csi.dma package. 
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

    private final static QName _DatiDocumentoResponse_QNAME = new QName("http://dma.csi.it/", "datiDocumentoResponse");
    private final static QName _ListaProfili_QNAME = new QName("http://dma.csi.it/", "listaProfili");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: it.csi.dma
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link RichiedenteInfo }
     * 
     */
    public RichiedenteInfo createRichiedenteInfo() {
        return new RichiedenteInfo();
    }

    /**
     * Create an instance of {@link DatiDocumentoResponse }
     * 
     */
    public DatiDocumentoResponse createDatiDocumentoResponse() {
        return new DatiDocumentoResponse();
    }

    /**
     * Create an instance of {@link Ruolo }
     * 
     */
    public Ruolo createRuolo() {
        return new Ruolo();
    }

    /**
     * Create an instance of {@link DatiDocumento }
     * 
     */
    public DatiDocumento createDatiDocumento() {
        return new DatiDocumento();
    }

    /**
     * Create an instance of {@link ServiceResponse }
     * 
     */
    public ServiceResponse createServiceResponse() {
        return new ServiceResponse();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DatiDocumentoResponse }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link DatiDocumentoResponse }{@code >}
     */
    @XmlElementDecl(namespace = "http://dma.csi.it/", name = "datiDocumentoResponse")
    public JAXBElement<DatiDocumentoResponse> createDatiDocumentoResponse(DatiDocumentoResponse value) {
        return new JAXBElement<DatiDocumentoResponse>(_DatiDocumentoResponse_QNAME, DatiDocumentoResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link Codifica }{@code >}
     * 
     * @param value
     *     Java instance representing xml element's value.
     * @return
     *     the new instance of {@link JAXBElement }{@code <}{@link Codifica }{@code >}
     */
    @XmlElementDecl(namespace = "http://dma.csi.it/", name = "listaProfili")
    public JAXBElement<Codifica> createListaProfili(Codifica value) {
        return new JAXBElement<Codifica>(_ListaProfili_QNAME, Codifica.class, null, value);
    }

}
