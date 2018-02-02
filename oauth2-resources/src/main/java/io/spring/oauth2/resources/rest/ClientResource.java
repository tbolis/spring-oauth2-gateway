package io.spring.oauth2.resources.rest;

import com.codahale.metrics.annotation.Timed;
import io.spring.oauth2.resources.rest.dto.ManagedUserDTO;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * CRUD operations for Client details management
 */
@RestController
@RequestMapping("/api")
public class ClientResource {

    private Logger LOG = getLogger(ClientResource.class);

    /**
     * GET  /clients/:id -> get the "login" user.
     */
    @Timed
    @RequestMapping(value = "/clients/{clientId}", method = GET, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ManagedUserDTO> getClient(@PathVariable String clientId) {

        LOG.debug("REST request to get Client with id : {}", clientId);

        return null;
//        return userService.getUserWithAuthoritiesByLogin(login).map(ManagedUserDTO::new)
//                .map(managedUserDTO -> new ResponseEntity<>(managedUserDTO, OK))
//                .orElse(new ResponseEntity<>(NOT_FOUND));

    }
}
