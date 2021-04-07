package de.flamestro.AgileIsTheNewOrange.web.model;

import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LaneResponse {
    private final Status status;
    private final Lane lane;
}
