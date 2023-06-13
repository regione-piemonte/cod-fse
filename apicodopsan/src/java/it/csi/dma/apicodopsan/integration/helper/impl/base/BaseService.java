/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.helper.impl.base;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;

import it.csi.dma.apicodopsan.dto.ErroreDettaglio;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.exception.ResponseErrorException;
import it.csi.dma.apicodopsan.integration.dao.custom.CodDErroreDao;
import it.csi.dma.apicodopsan.integration.service.CodRMessaggioErroreService;
import it.csi.dma.apicodopsan.util.ErrorBuilder;
import it.csi.dma.apicodopsan.util.LoggerUtil;
import it.csi.dma.apicodopsan.util.enumerator.CodeErrorEnum;
import it.csi.dma.apicodopsan.util.enumerator.StatusEnum;

public abstract class BaseService extends LoggerUtil {
	
	
	@Autowired
	protected CodDErroreDao codDErroreDao;
	
//	@Autowired
//	protected CodRMessaggioErroreService codRMessaggioErroreService;	
	
	protected void commonError(HttpServletRequest httpRequest, CodeErrorEnum codeErrorEnum, StatusEnum statusEnum) throws DatabaseException, ResponseErrorException {

		ErroreDettaglio erroreDettaglio = new ErroreDettaglio();
		erroreDettaglio.setChiave(codeErrorEnum.getCode());
		erroreDettaglio.setValore(codDErroreDao.selectErroreDescFromErroreCod(codeErrorEnum.getCode()));
		List<ErroreDettaglio> result = new ArrayList<>();

		result.add(erroreDettaglio);
	
//		codRMessaggioErroreService.saveError(result, httpRequest);
		throw new ResponseErrorException(
				ErrorBuilder.generateErrorBuilderError(statusEnum, result),
				"errore in commonError: "+erroreDettaglio.getValore());
//		return ErrorBuilder.generateResponseError(statusEnum, result);

	}
}
