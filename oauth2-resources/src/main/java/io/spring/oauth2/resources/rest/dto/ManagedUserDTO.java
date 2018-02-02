package io.spring.oauth2.resources.rest.dto;

import io.spring.oauth2.domain.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.ZonedDateTime;

/**
 * A DTO extending the UserDTO, which is meant to be used in the user management UI.
 */
@Getter
@Setter
@ToString
@NoArgsConstructor
public class ManagedUserDTO extends UserDTO {

    private Long id;
    private ZonedDateTime createdDate;
    private String lastModifiedBy;
    private ZonedDateTime lastModifiedDate;

    public ManagedUserDTO(User user) {
        super(user);
        this.id = user.getId();
        this.createdDate = user.getCreatedDate();
        this.lastModifiedBy = user.getLastModifiedBy();
        this.lastModifiedDate = user.getLastModifiedDate();
    }
}