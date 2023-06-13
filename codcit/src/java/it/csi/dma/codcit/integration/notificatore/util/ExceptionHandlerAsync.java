/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration.notificatore.util;

import java.lang.reflect.Method;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;

import it.csi.dma.codcit.util.LoggerUtil;

public class ExceptionHandlerAsync extends LoggerUtil implements AsyncUncaughtExceptionHandler {

    @Override
    public void handleUncaughtException(
      Throwable throwable, Method method, Object... obj) {
    	String methodName="handleUncaughtException";
       logInfo(methodName," Exception message - " + throwable.getMessage());
       logInfo(methodName," Method name - " + method.getName());
        for (Object param : obj) {
        	 logInfo(methodName," Parameter value - " + param);
        }
    }
}