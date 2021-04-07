package de.flamestro.AgileIsTheNewOrange.web.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MoveCardRequest {
    private String targetCardId;
    private String targetLaneId;
    private String targetBoardId;
    private String sourceCardId;
    private String sourceLaneId;
    private String sourceBoardId;
}
