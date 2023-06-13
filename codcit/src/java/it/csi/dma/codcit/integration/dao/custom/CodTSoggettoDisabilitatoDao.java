/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.custom;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import it.csi.dma.codcit.dto.custom.SoggettoDisabilitato;
import it.csi.dma.codcit.exception.DatabaseException;
import it.csi.dma.codcit.integration.dao.utils.SoggettoDisabilitatoMotivoAndMotivoIdMapper;
import it.csi.dma.codcit.util.LoggerUtil;

@Repository
public class CodTSoggettoDisabilitatoDao extends LoggerUtil {

	private static final String VERIFICARE_DISABILITAZIONE_MEDICO_CITTADINO = " select DISABILITAZIONE_MOTIVO_ID, DISABILITAZIONE_MOTIVAZIONE from "
			+ "COD_T_SOGGETTO_DISABILITATO ctsd join cod_t_soggetto ctsabilitato on "
			+ "ctsabilitato.soggetto_id = ctsd.soggetto_id_abilitato and ctsabilitato.soggetto_cf = :cfCittadino "
			+ " join cod_t_soggetto ctsabilitante on "
			+ "ctsabilitante.soggetto_id = ctsd.soggetto_id_abilitante and ctsabilitante.soggetto_cf = :cfMedico "
			+ " where ctsd.abilitazione_fine <= NOW() "
			+ "order by coalesce(ctsd.data_modifica,ctsd.data_creazione) desc limit 1;";

	public static final String SELECT_MOTIVAZIONE_BLOCCO = 	"SELECT DISABILITAZIONE_MOTIVAZIONE	" +
			"FROM 								" +
			"	COD_T_SOGGETTO_DISABILITATO		" +
			"WHERE								" +
			"	SOGGETTO_ID_ABILITATO = (SELECT soggetto_id FROM cod_t_soggetto where soggetto_cf=:soggetto_cf limit 1)	and soggetto_id_abilitante=:medicoId order by abilitazione_id desc limit 1;";

	@Autowired
	private NamedParameterJdbcTemplate jdbcTemplate;

	public SoggettoDisabilitato selectSoggettoDisabilitatoWhereCittadinoAbilitatoDaMedico(String cfMedico,
			String cfCittadino) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("cfMedico", cfMedico)
				.addValue("cfCittadino", cfCittadino);
		try {
			return jdbcTemplate.queryForObject(VERIFICARE_DISABILITAZIONE_MEDICO_CITTADINO, namedParameters,
					new SoggettoDisabilitatoMotivoAndMotivoIdMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		} catch (Exception e) {
			var methodName = "selectSoggettoDisabilitatoWhereCittadinoAbilitatoDaMedico";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);

		}
	}

	public String selectMotivazioneBlocco(String soggetto_cf ,int medicoId) throws DatabaseException {

		SqlParameterSource namedParameters = new MapSqlParameterSource()
						.addValue("medicoId",medicoId).addValue("soggetto_cf", soggetto_cf);
		try {
			return jdbcTemplate.queryForMap(SELECT_MOTIVAZIONE_BLOCCO, namedParameters).get("disabilitazione_motivazione")+"";
		}
		catch (IncorrectResultSizeDataAccessException  e) {
			return null;
		}
		catch (Exception e) {
			var methodName = "selectMotivazioneBlocco";
			logError(methodName, e.getMessage());
			throw new DatabaseException(e);
		}
	}

}
