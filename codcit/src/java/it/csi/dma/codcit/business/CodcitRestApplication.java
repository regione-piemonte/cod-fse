/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.business;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import it.csi.dma.codcit.business.be.impl.CittadiniApiServiceImpl;
import it.csi.dma.codcit.business.be.impl.ServizioAttivoImpl;
import it.csi.dma.codcit.exception.BadRequestExceptionMapper;
import it.csi.dma.codcit.exception.NotFoundExceptionMapper;

@ApplicationPath("api/v1")
public class CodcitRestApplication extends Application {
	private Set<Object> singletons = new HashSet<Object>();
	private Set<Class<?>> empty = new HashSet<Class<?>>();
	
	public CodcitRestApplication() {
		singletons.add(new CittadiniApiServiceImpl());
		singletons.add(new ServizioAttivoImpl());
		
		singletons.add(new BadRequestExceptionMapper());
		singletons.add(new NotFoundExceptionMapper());
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
