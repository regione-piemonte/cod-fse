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
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import it.csi.dma.apicodopsan.dto.PayloadMessaggio1;
import it.csi.dma.apicodopsan.dto.PayloadMessaggio_;

@Path("/conversazioni")




public interface ConversazioniApi  {
   
    @GET
    
    
    @Produces({ "application/json" })

    public Response conversazioniGet(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, @NotNull @QueryParam("limit") Integer limit, @NotNull @QueryParam("offset") Integer offset, @NotNull @QueryParam("sola_lettura") String solaLettura, @QueryParam("cognome") String cognome, @QueryParam("codice_fiscale") String codiceFiscale, @QueryParam("id_conversazione") String idConversazione,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    @GET
    @Path("/{id_conversazione}/messaggi")
    
    @Produces({ "application/json" })

    public Response conversazioniIdConversazioneMessaggiGet(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, @PathParam("id_conversazione") String idConversazione, @NotNull @QueryParam("limit") Integer limit, @NotNull @QueryParam("offset") Integer offset,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    @PUT
    @Path("/{id_conversazione}/messaggi/{id_messaggio}/letto")
    
    @Produces({ "application/json" })

    public Response conversazioniIdConversazioneMessaggiIdMessaggioLettoPut(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, @PathParam("id_conversazione") String idConversazione, @PathParam("id_messaggio") String idMessaggio,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    @PUT
    @Path("/{id_conversazione}/messaggi/{id_messaggio}")
    
    @Produces({ "application/json" })

    public Response conversazioniIdConversazioneMessaggiIdMessaggioPut(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, @PathParam("id_conversazione") String idConversazione, @PathParam("id_messaggio") String idMessaggio, PayloadMessaggio1 payloadMessaggio_,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
    @POST
    @Path("/{id_conversazione}/messaggi")
    
    @Produces({ "application/json" })

    public Response conversazioniIdConversazioneMessaggiPost(@HeaderParam("Shib-Identita-CodiceFiscale") String shibIdentitaCodiceFiscale,@HeaderParam("X-Request-Id") String xRequestId,@HeaderParam("X-Forwarded-For") String xForwardedFor,@HeaderParam("X-Codice-Servizio") String xCodiceServizio,@HeaderParam("X-Codice-Verticale") String xCodiceVerticale, @NotNull @QueryParam("ruolo") String ruolo, @NotNull @QueryParam("collocazione_codice") String collocazioneCodice, @NotNull @QueryParam("collocazione_descrizione") String collocazioneDescrizione, @PathParam("id_conversazione") String idConversazione, PayloadMessaggio_ payloadMessaggio_,@Context SecurityContext securityContext, @Context HttpHeaders httpHeaders , @Context HttpServletRequest httpRequest );
}
