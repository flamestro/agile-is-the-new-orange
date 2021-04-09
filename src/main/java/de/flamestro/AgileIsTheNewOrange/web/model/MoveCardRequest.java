package de.flamestro.AgileIsTheNewOrange.web.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MoveCardRequest {
    private final String targetCardId;
    private final String targetLaneId;
    private final String targetBoardId;
    private final String sourceCardId;
    private final String sourceLaneId;
    private final String sourceBoardId;
}
