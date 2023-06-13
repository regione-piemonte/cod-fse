/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.business;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.dma.apicodopsan.business.be.impl.AdesioneApiServiceImpl;
import it.csi.dma.apicodopsan.business.be.impl.AssistitiApiServiceImpl;
import it.csi.dma.apicodopsan.business.be.impl.ConversazioniApiServiceImpl;
import it.csi.dma.apicodopsan.business.be.impl.DocumentiApiServiceImpl;
import it.csi.dma.apicodopsan.business.be.impl.InfoApiServiceImpl;
import it.csi.dma.apicodopsan.business.be.impl.NotificaLetturaApiServiceImpl;

@ApplicationPath("api/v1")
public class ApicodopsanRestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public ApicodopsanRestApplication() {
		singletons.add(new AdesioneApiServiceImpl());
		singletons.add(new AssistitiApiServiceImpl());
		singletons.add(new ConversazioniApiServiceImpl());
		singletons.add(new InfoApiServiceImpl());
		singletons.add(new NotificaLetturaApiServiceImpl());
		singletons.add(new DocumentiApiServiceImpl());
	}

	@Override
	public Set<Class<?>> getClasses() {
		return empty;
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

}
