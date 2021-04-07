package de.flamestro.AgileIsTheNewOrange.board.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class Board implements Serializable {
    @Id
    public String id;

    @JsonIgnore
    private String[] allowedUsers;

    @NotBlank
    private String name;

    @NotNull
    private List<Lane> lanes;
}
