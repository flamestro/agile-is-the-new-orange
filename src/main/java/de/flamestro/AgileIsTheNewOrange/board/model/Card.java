package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class Card {
    @Id
    public String id;

    @NotBlank
    private final String name;

    private final String description;
}
