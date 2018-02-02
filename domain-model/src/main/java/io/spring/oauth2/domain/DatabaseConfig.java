package io.spring.oauth2.domain;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.ApplicationContextException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.slf4j.LoggerFactory.getLogger;

@Configuration
@EnableTransactionManagement
@EntityScan(basePackages = {"io.spring.oauth2.domain"})
@EnableJpaAuditing(auditorAwareRef = "springSecurityAuditorAware")
@EnableJpaRepositories(basePackages = "io.spring.oauth2.domain")
public class DatabaseConfig {

    private final Logger LOG = getLogger(DatabaseConfig.class);

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean(destroyMethod = "close")
    // for dataSourceProperties from spring-boot autoconfig
    @SuppressWarnings("SpringJavaAutowiringInspection")
    public HikariDataSource dataSource(DataSourceProperties dataSourceProperties) {
        LOG.debug("Configuring Datasource");
        if (dataSourceProperties.getUrl() == null) {
            LOG.error("Failed to Instantiate Database Connection, check your application.yml file");
            throw new ApplicationContextException("Database connection pool is not configured correctly");
        }
        HikariConfig config = new HikariConfig();
        config.setDataSourceClassName(dataSourceProperties.getDriverClassName());
        config.addDataSourceProperty("url", dataSourceProperties.getUrl());
        if (dataSourceProperties.getUsername() != null) {
            config.addDataSourceProperty("user", dataSourceProperties.getUsername());
        } else {
            config.addDataSourceProperty("user", ""); // HikariCP doesn't allow null user
        }
        if (dataSourceProperties.getPassword() != null) {
            config.addDataSourceProperty("password", dataSourceProperties.getPassword());
        } else {
            config.addDataSourceProperty("password", ""); // HikariCP doesn't allow null password
        }
        return new HikariDataSource(config);
    }
}