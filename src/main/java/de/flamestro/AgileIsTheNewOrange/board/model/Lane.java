package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class Lane implements Serializable {
    public String id;

    @NotBlank
    private String name;

    private Board board;

    @NotNull
    private List<Card> cards;

}
