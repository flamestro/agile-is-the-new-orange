package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Builder
public class Card implements Serializable {
    public String id;

    @NotBlank
    private String name;

    private String description;
}
