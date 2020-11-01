package de.flamestro.AgileIsTheNewOrange.board.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@Builder
@ToString
public class Lane {
    @Id
    public String id;

    @NotBlank
    private String name;

    @NotNull
    private List<Card> cardList;
}
