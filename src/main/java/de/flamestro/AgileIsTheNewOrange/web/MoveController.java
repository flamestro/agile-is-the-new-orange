package de.flamestro.AgileIsTheNewOrange.web;

import de.flamestro.AgileIsTheNewOrange.board.service.LaneService;
import de.flamestro.AgileIsTheNewOrange.web.model.LaneResponse;
import de.flamestro.AgileIsTheNewOrange.web.model.MoveCardRequest;
import de.flamestro.AgileIsTheNewOrange.web.model.Status;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/")
@AllArgsConstructor
@Slf4j
public class MoveController {

    private final LaneService laneService;

    @PutMapping("/moveCard")
    public ResponseEntity<LaneResponse> moveCard(@RequestBody MoveCardRequest moveCardRequest) {
        laneService.moveCard(moveCardRequest);
        return ResponseEntity.ok(LaneResponse.builder().status(Status.SUCCESS).build());
    }
}
