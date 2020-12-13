package de.flamestro.AgileIsTheNewOrange.user;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class User {
    @NotBlank
    private final String userId;
    private final String name;
    private final String pictureUrl;
    private final Role role;
    public enum Role{
        USER, // can add boards, move cards, rename boards ...
        ARCHIVE_OWNER, // can archive boards and all rights of a USER
        SUPERVISOR // can delete boards and all rights of a ARCHIVE_MASTER
    }
}
