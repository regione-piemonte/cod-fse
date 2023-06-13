/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dmacodbatch;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import it.csi.dmacodbatch.exception.BusinessException;
import it.csi.dmacodbatch.service.DisabilitaAssistitiService;
import it.csi.dmacodbatch.service.EliminaMessaggiService;

@SpringBootApplication
@ComponentScan({"it.csi.dmacodbatch"})
public class DmacodbatchApplication implements CommandLineRunner {
	Logger logger = LoggerFactory.getLogger(DmacodbatchApplication.class);
	
	
	
	@Value("${command:}")
    private String command;
	@Autowired
	EliminaMessaggiService eliminaMessaggiService;
	@Autowired
	DisabilitaAssistitiService disabilitaAssistitiService;
	
	public static void main(String[] args) {
		SpringApplication.run(DmacodbatchApplication.class, args);
		
	}

	@Override
	public void run(String... args) throws Exception {
		//System.out.println(this.foo);
		switch (command) {
		case "eliminaMessaggi":
			eliminaMessaggi();
			break;
		case "disabilitaAssistiti":
			disabilitaAssistiti();
			break;

		default:
			logger.error("Command non esistente");
			break;
		}
		System.exit(0);
	}

	private void eliminaMessaggi() {
		try {
			eliminaMessaggiService.execute();
		}catch(BusinessException be) {
			logger.error("Batch non andato a buon fine", be);
		}
	}
	private void disabilitaAssistiti() {
		try {
			disabilitaAssistitiService.execute();
		}catch(BusinessException be) {
			logger.error("Batch non andato a buon fine", be);
		}
	}

	
}
