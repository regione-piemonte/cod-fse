/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import it.csi.dma.apicodopsan.dto.custom.TAdesione;
import it.csi.dma.apicodopsan.exception.DatabaseException;
import it.csi.dma.apicodopsan.util.LoggerUtil;

@Repository
public class CodTSoggettoAbilitatoDao extends LoggerUtil {

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public static final String DELETE_SOGGETTO_ABILITATO = "			DELETE										"
															+ "			FROM										"
															+ "				COD_T_SOGGETTO_ABILITATO				"
															+ "			WHERE										"
															+ "				SOGGETTO_ID_ABILITANTE = :soggettoId 	";

	private static final String VERIFICARE_ABILITAZIONE_MEDICO_CITTADINO_DA_ID = "select count(*) from cod_t_soggetto_abilitato ctsa "
			+ "join cod_t_soggetto ctsabilitato on ctsabilitato.soggetto_id = ctsa.soggetto_id_abilitato "
			+ "and ctsabilitato.soggetto_id = :idCittadino "
			+ "join cod_t_soggetto ctsabilitante on ctsabilitante.soggetto_id = ctsa.soggetto_id_abilitante "
			+ "and ctsabilitante.soggetto_id = :idMedico and ctsabilitante.soggetto_is_medico is TRUE where  "
			+ "abilitazione_inizio <= NOW() ";

	@Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
	public int deleteTSocggettoAbilitato(TAdesione adesione) throws DatabaseException {

		// SETTAGGIO PARAMENTRI QUERY
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("soggettoId",
				adesione.getSoggettoId());

		// ESECUZIONE DELLA QUERY
		try {
			return jdbcTemplate.update(DELETE_SOGGETTO_ABILITATO, namedParameters);
		} catch (Exception e) {
			var methodName = "selectErroreDescFromErroreCod";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Integer selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedicoFromId(Integer idMedico, Integer idCittadino)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("idMedico", idMedico)
				.addValue("idCittadino", idCittadino);
		try {

			return jdbcTemplate.queryForObject(VERIFICARE_ABILITAZIONE_MEDICO_CITTADINO_DA_ID, namedParameters,
					Integer.class);

		} catch (Exception e) {
			var methodName = "selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedicoFromId";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}
