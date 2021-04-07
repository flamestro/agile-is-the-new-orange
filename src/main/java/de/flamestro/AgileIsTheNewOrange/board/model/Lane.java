package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Lane implements Serializable {
    public String id;

    @NotBlank
    private String name;

    @NotNull
    private List<Card> cards;
}
