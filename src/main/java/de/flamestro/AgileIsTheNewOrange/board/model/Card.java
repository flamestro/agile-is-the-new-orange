package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@Builder
@ToString
public class Card {
    @Id
    public String id;

    @NotBlank
    private String name;

    private String description;
}
