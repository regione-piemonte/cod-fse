/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.util;

import java.util.List;

public class Pagination<T> {
	private Integer count;
	private List<T> listaRis;
	
	
	public Integer getCount() {
		return count;
	}
	public void setCount(Integer count) {
		this.count = count;
	}
	public List<T> getListaRis() {
		return listaRis;
	}
	public void setListaRis(List<T> listaRis) {
		this.listaRis = listaRis;
	}
}
