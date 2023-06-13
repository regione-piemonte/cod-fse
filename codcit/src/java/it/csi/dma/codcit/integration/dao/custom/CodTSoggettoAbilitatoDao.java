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

import it.csi.dma.codcit.dto.custom.PresenzaSoggettoMedico;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.PresenzaSoggettoMedicoMapper;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodTSoggettoAbilitatoDao extends LoggerUtil {

	private static final String VERIFICARE_ABILITAZIONE_MEDICO_CITTADINO = "select count(*) from cod_t_soggetto_abilitato ctsa "
			+ "join cod_t_soggetto ctsabilitato on ctsabilitato.soggetto_id = ctsa.soggetto_id_abilitato "
			+ "and ctsabilitato.soggetto_cf = :cfCittadino "
			+ "join cod_t_soggetto ctsabilitante on ctsabilitante.soggetto_id = ctsa.soggetto_id_abilitante "
			+ "and ctsabilitante.soggetto_cf = :cfMedico and ctsabilitante.soggetto_is_medico is TRUE where "
			+ "abilitazione_inizio <= NOW();";


	private static final String VERIFICARE_ABILITAZIONE_MEDICO_CITTADINO_DA_ID = "select count(*) from cod_t_soggetto_abilitato ctsa "
			+ "join cod_t_soggetto ctsabilitato on ctsabilitato.soggetto_id = ctsa.soggetto_id_abilitato "
			+ "and ctsabilitato.soggetto_id = :idCittadino "
			+ "join cod_t_soggetto ctsabilitante on ctsabilitante.soggetto_id = ctsa.soggetto_id_abilitante "
			+ "and ctsabilitante.soggetto_id = :idMedico and ctsabilitante.soggetto_is_medico is TRUE where  "
			+ "abilitazione_inizio <= NOW() ";

	private static final String SOGGETTO_CITTADINO = "select soggetto_id from cod_t_soggetto cts where "
			+"cts.soggetto_cf = :cfCittadino ";

	private static final String SOGGETTO_MEDICO = "select soggetto_id from cod_t_soggetto cts where "
			+"cts.soggetto_cf = :cfMedico and cts.soggetto_is_medico is true ";

	private static final String VERIFICARE_PRESENZA_CITTADINO = "select count(*) as countCittadino from cod_t_soggetto cts where "
							+"cts.soggetto_cf = :cfCittadino ";

	private static final String VERIFICARE_PRESENZA_MEDICO = "select count(*) countMedico from cod_t_soggetto cts where "
			+"cts.soggetto_cf = :cfMedico and cts.soggetto_is_medico is true ";


	private static final String VERIFICARE_PRESENZA_MEDICO_CITTADINO = "select cittadino.countCittadino, medico.countMedico "
				+ " from ( " + VERIFICARE_PRESENZA_CITTADINO + ") as cittadino, ("
						+ VERIFICARE_PRESENZA_MEDICO + ") as medico";


	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public Integer selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedico(String cfMedico, String cfCittadino)
			throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfMedico", cfMedico)
				.addValue("cfCittadino", cfCittadino);
		try {

			return jdbcTemplate.queryForObject(VERIFICARE_ABILITAZIONE_MEDICO_CITTADINO, namedParameters,
					Integer.class);

		} catch (Exception e) {
			var methodName = "selectSoggettoAbilitatoWhereCittadinoAbilitatoDaMedico";
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

	public Integer selectIdSoggettoCittadino(String cfCittadino) throws DatabaseException{
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfCittadino", cfCittadino);
		try {
			return jdbcTemplate.queryForObject(SOGGETTO_CITTADINO, namedParameters,
					Integer.class);

		} catch(EmptyResultDataAccessException e) {
			//TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		}
		catch (Exception e) {
			var methodName = "idSoggettoCittadino";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public Integer selectIdSoggettoMedico(String cfMedico) throws DatabaseException{
		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfMedico", cfMedico);
		try {
			return jdbcTemplate.queryForObject(SOGGETTO_MEDICO, namedParameters,
					Integer.class);

		} catch(EmptyResultDataAccessException e) {
			//TODO verificare di farlo cosi per tutti i EmptyResultDataAccessException
			return null;
		}
		catch (Exception e) {
			var methodName = "idSoggettoCittadino";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

	public PresenzaSoggettoMedico selectSoggettoMedicoPresenti(String cfCittadino, String cfMedico) throws DatabaseException{

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfCittadino", cfCittadino).addValue("cfMedico", cfMedico);
		try {
			return jdbcTemplate.queryForObject(VERIFICARE_PRESENZA_MEDICO_CITTADINO, namedParameters,
					new PresenzaSoggettoMedicoMapper());

		} catch (Exception e) {
			var methodName = "selectSoggettoMedicoPresenti";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}
}
