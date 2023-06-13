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

import it.csi.dma.codcit.dto.custom.AdesioneSoggetto;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodTAdesioneSoggetto extends LoggerUtil{

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	private static final String RICERCA_DATI_MEDICO="SELECT adesione_id, ta.soggetto_id, ta.adesione_inizio,  ta.adesione_fine, ta.mostra_lettura_messaggi_a_assistiti, ta.data_creazione, ta.data_modifica, ta.utente_creazione, ta.utente_modifica "
			+ "	FROM cod_t_adesione as ta, cod_t_soggetto as ts where ta.soggetto_id=ts.soggetto_id "
			+ " and soggetto_cf=:soggettoCf and soggetto_is_medico=true order by ta.adesione_inizio desc limit 1;";


	public AdesioneSoggetto selectAdesioneSoggettoFromCF(String soggettoCF)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("soggettoCf", soggettoCF);
		try {
			return jdbcTemplate.queryForObject(RICERCA_DATI_MEDICO, namedParameters, (rs, rowNum) ->
				new AdesioneSoggetto(rs.getLong(1), rs.getLong(2), rs.getTimestamp(3), rs.getTimestamp(4), rs.getBoolean(5), rs.getTimestamp(6), rs.getTimestamp(7), rs.getString(8), rs.getString(9)));
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectAdesioneSoggettoFromCF";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

}
