/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.dao.utils;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import it.csi.dma.codcit.dto.custom.ConversazioneCustom;

public class ConversazioneCustomMapper implements RowMapper<ConversazioneCustom> {

	@Override
	public ConversazioneCustom mapRow(ResultSet r, int rowNumber) throws SQLException {
		var result = new ConversazioneCustom();
		result.setConversazioneId(r.getLong("conversazione_id"));
		result.setDisabilitazioneMotivoId(
				r.getObject("disabilitazione_motivo_id") != null ? r.getInt("disabilitazione_motivo_id") : null);
		result.setConversazioneCod(r.getString("conversazione_cod"));
		result.setMedicoSoggettoNome(r.getString("soggetto_nome"));
		result.setMedicoSoggettoCognome(r.getString("soggetto_cognome"));
		result.setMedicoSoggettoCf(r.getString("soggetto_cf"));
		result.setConversazioneOggetto(r.getBytes("conversazione_oggetto"));
		result.setConversazioneDataBlocco(r.getTimestamp("conversazione_data_blocco"));
		result.setDataCreazione(r.getTimestamp("data_creazione"));
		result.setUtenteCreazione(r.getString("utente_creazione"));
		result.setMessaggiNonLetti(r.getInt("messaggi_non_letti"));
		result.setMessaggioDataCreazione(r.getTimestamp("messaggio_data_creazione"));
		result.setMessaggioId(r.getObject("messaggio_id") != null ? r.getLong("messaggio_id") : null);
		result.setMessaggioLetturaData(r.getTimestamp("messaggio_lettura_data"));
		result.setMessaggioTesto(r.getBytes("messaggio_testo"));
		result.setMessaggioUtenteCreazione(r.getString("messaggio_utente_creazione"));
		result.setDisabilitazioneMotivoCod(r.getString("disabilitazione_motivo_cod"));
		result.setDisabilitazioneMotivoDesc(r.getString("disabilitazione_motivo_desc"));
		result.setMessaggioUtenteModifica(r.getString("messaggio_utente_modifica"));
		result.setMessaggioDataModifica(r.getTimestamp("messaggio_data_modifica"));
		result.setSoggettoCf(r.getString("soggetto_cf"));
		result.setMedicoSoggettoCf(r.getString("soggetto_cf"));
		result.setSoggettoIdDa(r.getInt("soggetto_id_da"));
		result.setMedicoSoggettoId(r.getInt("medico_soggetto_id"));
		return result;
	}

}
