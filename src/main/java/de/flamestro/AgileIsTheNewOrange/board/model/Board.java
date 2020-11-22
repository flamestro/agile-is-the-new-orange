package de.flamestro.AgileIsTheNewOrange.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Builder
public class Board {
    @Id
    public String id;

    @JsonIgnore
    private String[] allowedUsers;

    @NotBlank
    private final String name;

    @NotNull
    private final List<Lane> lanes;
}
