package io.spring.oauth2.authorization.rest;

import io.spring.oauth2.authorization.Bootstrap;
import org.junit.Ignore;
import org.junit.runner.RunWith;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

/**
 * Test class for the UserResource REST controller.
 *
 * @see UserResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Bootstrap.class)
@WebAppConfiguration
@IntegrationTest
@Ignore
public class UserResourceIntTest {
//
//    @Inject
//    private UserRepository userRepository;
//
//    @Inject
//    private UserService userService;
//
//    private MockMvc restUserMockMvc;
//
//    @Before
//    public void setup() {
//        UserResource userResource = new UserResource();
//        ReflectionTestUtils.setField(userResource, "userRepository", userRepository);
//        ReflectionTestUtils.setField(userResource, "userService", userService);
//        this.restUserMockMvc = MockMvcBuilders.standaloneSetup(userResource).build();
//    }
//
//    @Test
//    public void testGetExistingUser() throws Exception {
//        restUserMockMvc.perform(get("/api/users/admin")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json"))
//                .andExpect(jsonPath("$.lastName").value("Administrator"));
//    }
//
//    @Test
//    public void testGetUnknownUser() throws Exception {
//        restUserMockMvc.perform(get("/api/users/unknown")
//                .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isNotFound());
//    }
}
