package io.spring.oauth2.authorization.rest;

import io.spring.oauth2.authorization.Bootstrap;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the AccountResource REST controller.
 *
 * @see UserService
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Bootstrap.class)
@WebAppConfiguration
@IntegrationTest
@Ignore
public class AccountResourceIntTest {
}
