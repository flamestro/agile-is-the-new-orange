package de.flamestro.AgileIsTheNewOrange.web.model;

import lombok.*;

@Builder
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MoveCardRequest {
    private String targetCardId;
    private String targetLaneId;
    private String targetBoardId;
    private String sourceCardId;
    private String sourceLaneId;
    private String sourceBoardId;
}
