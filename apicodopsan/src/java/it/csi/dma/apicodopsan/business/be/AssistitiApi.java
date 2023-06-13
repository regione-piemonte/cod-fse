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
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.dma.apicodopsan.dto.PayloadAbilitazione;
import it.csi.dma.apicodopsan.dto.PayloadAbilitazioneTotale;

@Path("/assistiti")




public interface AssistitiApi  {
   
    @GET
    
    
    @Produces({ "application/json" })

    public Response assistitiGet(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, @NotNull @QueryParam("limit") Integer limit, @NotNull @QueryParam("offset") Integer offset, @NotNull @QueryParam("stato") String stato, @QueryParam("cognome") String cognome, @QueryParam("nome") String nome, @QueryParam("codice_fiscale") String codiceFiscale, @QueryParam("eta_min") Integer etaMin, @QueryParam("eta_max") Integer etaMax, @QueryParam("sesso") String sesso,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    @POST
    
    
    @Produces({ "application/json" })

    public Response assistitiPost(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, PayloadAbilitazione payloadAbilitazione,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    @POST
    @Path("/tutti")
    
    @Produces({ "application/json" })

    public Response assistitiTuttiPost(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, PayloadAbilitazioneTotale payloadAbilitazioneTotale,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
}
