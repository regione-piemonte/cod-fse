/*******************************************************************************
* Copyright Regione Piemonte - 2023
* SPDX-License-Identifier: EUPL-1.2
******************************************************************************/

package it.csi.dma.codcit.integration;

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
	@Bean(name = "codcitDataSource")
	public JndiObjectFactoryBean dataSource(@Value("${jndi.name}") String jndiName) {
		var jndiObjectFactoryBean = new JndiObjectFactoryBean();
		jndiObjectFactoryBean.setJndiName(jndiName);
		jndiObjectFactoryBean.setResourceRef(true);
		jndiObjectFactoryBean.setProxyInterface(DataSource.class);
		return jndiObjectFactoryBean;
	}

	@Bean(name = "namedParameterJdbcTemplate")
	@DependsOn("codcitDataSource")
	public NamedParameterJdbcTemplate namedParameterJdbcTemplate(
			@Qualifier("codcitDataSource") DataSource codcitDataSource) {
		return new NamedParameterJdbcTemplate(codcitDataSource);
	}

	@Bean
	public PlatformTransactionManager dbTransactionManager(@Qualifier("codcitDataSource") DataSource codcitDataSource) {
		return new DataSourceTransactionManager(codcitDataSource);
	}
}
