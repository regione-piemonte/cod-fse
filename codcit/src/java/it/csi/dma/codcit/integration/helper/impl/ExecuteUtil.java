/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.helper.impl;

import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Response;

import it.csi.dma.codcit.util.LoggerUtil;
import it.csi.dma.codcit.util.Pagination;
import it.csi.dma.codcit.util.enumerator.HeaderEnum;

abstract class ExecuteUtil extends LoggerUtil {

	protected static String getStringFromByteArray(byte[] byteA) {
		if (byteA == null) {
			return null;
		}
		return new String(byteA, StandardCharsets.UTF_8);
	}

	protected static String getStringFromByteArray(byte[] byteA, Integer maxLength) {
		if (byteA == null) {
			return null;
		} else if(byteA.length<maxLength)  {
			return new String(byteA, StandardCharsets.UTF_8);
		}
		
		return new String(byteA, 0, maxLength, StandardCharsets.UTF_8);
	}
	
	
	protected  <T> Response returnResponsePagination(Pagination<T> listaPaginata, Integer limit, Integer offset) {
		Integer totalElements = listaPaginata.getCount();
		Integer pageSize = Integer.valueOf(listaPaginata.getListaRis().size());
		
		Integer totalPages = null;
		Integer page = null;
		
		if(totalElements != null && pageSize != null) {
			totalPages = totalElements / pageSize;
			Integer resto = totalElements % pageSize;
			if(resto != 0) {
				totalPages += 1;
			}
			page = offset /limit;
			Integer restoPage = offset % limit;
			if(restoPage != 0) {
				page += 1;
			}			
		}
		
		return Response.ok(listaPaginata.getListaRis()).header(HeaderEnum.X_TOTAL_ELEMENTS.getCode(), totalElements).
				header(HeaderEnum.X_Page_Size.getCode(), pageSize).header(HeaderEnum.X_Total_Pages.getCode(), totalPages).
				header(HeaderEnum.X_Page.getCode(), page).build();
		
	}
}
