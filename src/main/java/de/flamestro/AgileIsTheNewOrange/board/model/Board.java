package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
@ToString
public class Board {
    @Id
    public String id;

    @NotBlank
    private final String name;

    @NotNull
    private final List<Lane> lanes;
}
