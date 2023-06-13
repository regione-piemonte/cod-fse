/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.dto.custom;

import java.util.Date;

import it.csi.dma.apicodopsan.dto.ModelAssistito;
import it.csi.dma.apicodopsan.dto.ModelMotivoBlocco;

public class ModelAssistitoCustom extends ModelAssistito {


	public ModelAssistitoCustom() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ModelAssistitoCustom(String nome, String cognome, String codiceFiscale, Date dataNascita, String sesso,
			String statoAbilitazione, ModelMotivoBlocco motivoBlocco, String motivazioneMedico) {
		super();
		setNome(nome);
		setCognome(cognome);
		setCodiceFiscale(codiceFiscale);
		setDataNascita(dataNascita);
		setSesso(sesso);
		setStatoAbilitazione(statoAbilitazione);
		setMotivoBlocco(motivoBlocco);
		setMotivazioneMedico(motivazioneMedico);
	}


}
