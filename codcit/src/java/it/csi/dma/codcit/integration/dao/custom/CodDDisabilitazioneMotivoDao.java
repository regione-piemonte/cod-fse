/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.dto.custom.DisabilitazioneMotivo;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.DisabilitazioneMotivoCodAndDescMapper;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodDDisabilitazioneMotivoDao extends LoggerUtil {

	private static final String ID_DISABILITAZIONE = "idDisabilitazione";

	private static final String MOTIVO_FROM_ID = "select DISABILITAZIONE_MOTIVO_COD, DISABILITAZIONE_MOTIVO_DESC from cod_d_disabilitazione_motivo cddm where"
			+ " cddm.disabilitazione_motivo_id = :idDisabilitazione;";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public DisabilitazioneMotivo selectMotivoCodAndMotivoDescFromMotivoId(Integer motivoId)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue(ID_DISABILITAZIONE, motivoId);
		try {
			return jdbcTemplate.queryForObject(MOTIVO_FROM_ID, namedParameters, new DisabilitazioneMotivoCodAndDescMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectMotivoCodAndMotivoDescFromMotivoId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

}
