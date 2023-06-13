/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.util.ErrorBuilder;

@Service
public interface CodRMessaggioErroreService {

//	void saveError(List<ErroreDettaglio> messaggioErroreList, HttpServletRequest httpRequest);
	
	ErrorBuilder saveError(ErrorBuilder error, HttpServletRequest httpRequest);
}