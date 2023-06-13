/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.apiopsanaura.integration;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class DatabaseConfiguration {

	@Primary
	@Bean(name = "apiopsanauraDataSource")
	public JndiObjectFactoryBean dataSource(@Value("${jndi.name}") String jndiName) {
		System.out.println("------------------------------"+jndiName);
		var jndiObjectFactoryBean = new JndiObjectFactoryBean();
		jndiObjectFactoryBean.setJndiName(jndiName);
		jndiObjectFactoryBean.setResourceRef(true);
		jndiObjectFactoryBean.setProxyInterface(DataSource.class);
		return jndiObjectFactoryBean;
	}

	@Bean(name = "namedParameterJdbcTemplate")
	@DependsOn("apiopsanauraDataSource")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
			@Qualifier("apiopsanauraDataSource") DataSource abcDataSource) {
		return new NamedParameterJdbcTemplate(abcDataSource);
	}

	@Bean
	public PlatformTransactionManager dbTransactionManager(@Qualifier("apiopsanauraDataSource") DataSource apicodopsanDataSource) {
//		codcitDataSource.getConnection().setAutoCommit(false);
		return new DataSourceTransactionManager(apicodopsanDataSource);
	}
	
	
}
