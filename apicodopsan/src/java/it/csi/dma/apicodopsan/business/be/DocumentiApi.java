/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

/**********************************************
 * CSI PIEMONTE 
 **********************************************/
package it.csi.dma.apicodopsan.business.be;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

@Path("/documenti")




public interface DocumentiApi  {
   
    @GET
    @Path("/{id_documento}/verifica-allegato")
    
    @Produces({ "application/json" })

    public Response documentiIdDocumentoVerificaAllegatoGet(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @PathParam("id_documento") String idDocumento, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, @QueryParam("codice_fiscale") String codiceFiscale, @QueryParam("cod_cl") String codCl,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
}
