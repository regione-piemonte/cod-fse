/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.business;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.dma.apiopsanaura.business.be.impl.AssistitiApiServiceImpl;
import it.csi.dma.apiopsanaura.business.be.impl.EsenzioniPatologiaApiServiceImpl;

@ApplicationPath("api/v1")
public class ApiopsanauraRestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();

	public ApiopsanauraRestApplication() {
		singletons.add(new AssistitiApiServiceImpl());
		singletons.add(new EsenzioniPatologiaApiServiceImpl());
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
