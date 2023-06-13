/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import it.csi.dma.apiopsanaura.dto.ErroreDettaglio;

@Service
public interface CodRMessaggioErroreService {

	void saveError(List<ErroreDettaglio> messaggioErroreList, HttpServletRequest httpRequest);

}