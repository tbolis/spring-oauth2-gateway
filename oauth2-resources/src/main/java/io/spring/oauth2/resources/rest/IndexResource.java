package io.spring.oauth2.resources.rest;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by tbolis on 12/2/2016.
 */
@RestController
@RequestMapping("/api")
public class IndexResource {

    @RequestMapping(value = "/dummy", method = GET, produces = TEXT_PLAIN_VALUE)
    public String dummy() {
        return "foo";
    }
}
