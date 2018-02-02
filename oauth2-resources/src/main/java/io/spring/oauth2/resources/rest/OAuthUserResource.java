package io.spring.oauth2.resources.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class OAuthUserResource {

    @RequestMapping(value = "/user")
    public Principal user(Principal user) {
        return user;
    }

}