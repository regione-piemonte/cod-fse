/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.ModelAutore;
import it.csi.dma.codcit.dto.ModelAutore.TipoEnum;
import it.csi.dma.codcit.dto.ModelMessaggio;

public class ModelMessaggioRowMapper implements RowMapper<ModelMessaggio> {

	@Override
	public ModelMessaggio mapRow(ResultSet rs, int rowNumber) throws SQLException {
		var result =new ModelMessaggio();
		result.setId("" + rs.getInt("messaggio_id"));
		result.setTesto(rs.getString("messaggio_testo_cifrato"));
		ModelAutore autore = new ModelAutore();
		autore.setCodiceFiscale(rs.getString("soggetto_cf"));
		autore.setTipo(TipoEnum.ASSISTITO);
		result.setAutore(autore);
		result.setDataCreazione(rs.getTimestamp("data_creazione"));
		//TODO verificare
		//result.setAutoreModifica(rs.getString("soggetto_cf"));
		//result.setLetto(rs.get);
		//result.setModificabile(modificabile);
		//result.setModificato(modificato);
		result.setDataLettura(rs.getTimestamp("messaggio_lettura_data"));
		result.setDataModifica(rs.getTimestamp("data_modifica"));
		
		return result;
	}

}
