package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Card implements Serializable {
    public String id;

    @NotBlank
    private String name;

    private String description;

    public Card copyWithNewId() {
        return Card.builder().description(this.getDescription()).id(UUID.randomUUID().toString()).name(this.getName()).build();
    }
}
