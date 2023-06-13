/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apicodopsan.integration;

import java.util.concurrent.Executor;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import it.csi.dma.apicodopsan.integration.notificatore.util.ExceptionHandlerAsync;

@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer{
	   @Override
	   public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
	       return new ExceptionHandlerAsync();
	   }

	@Override
	public Executor getAsyncExecutor() {
		 ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
         executor.setCorePoolSize(3);
         executor.setMaxPoolSize(3);
         executor.setQueueCapacity(100);
         executor.setThreadNamePrefix("AsynchThread-");
         executor.initialize();
         return executor; 
	}
}
