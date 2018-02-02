package io.spring.oauth2.authorization.service;

import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import javax.sql.DataSource;

/**
 * JDBC implementation of the client details service
 */
@Service
public class PersistentClientDetailsService extends JdbcClientDetailsService {

    @Inject
    public PersistentClientDetailsService(DataSource dataSource) {
        super(dataSource);
    }
}