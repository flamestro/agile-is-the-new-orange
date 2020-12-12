package de.flamestro.AgileIsTheNewOrange.board.service;

import de.flamestro.AgileIsTheNewOrange.board.model.Board;
import de.flamestro.AgileIsTheNewOrange.board.model.Card;
import de.flamestro.AgileIsTheNewOrange.board.model.Lane;
import de.flamestro.AgileIsTheNewOrange.board.repository.BoardRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
public class LaneService {

    private final BoardRepository boardRepository;
    private final BoardService boardService;

    public Lane createLane(String name, Board board) {
        Lane lane = Lane.builder()
                .name(name)
                .id(UUID.randomUUID().toString())
                .cards(new ArrayList<>())
                .build();
        boardService.addLaneToBoard(board, lane);
        log.info("added lane(id={}) to board(id={})", lane.getId(), board.getId());
        return lane;
    }

    public Lane removeLane(Lane lane, Board board) {
        board.getLanes().removeIf(laneInBoard -> laneInBoard.getId().equals(lane.getId()));
        boardRepository.save(board);
        log.info("removed lane(id={}) from board(id={})", lane.getId(), board.getId());
        return lane;
    }

    public void addCard(Board board, Lane lane, Card card) {
        List<Lane> laneList = board.getLanes()
                .stream()
                .filter(laneInBoard -> laneInBoard.getId().equals(lane.getId()))
                .collect(Collectors.toList());
        laneList.get(0).getCards().add(card);
        boardRepository.save(board);
    }

    public void moveCard(Card sourceCard, Lane sourceLane, Board sourceBoard, String targetCard, Lane targetLane, Board targetBoard) {
        Card copyOfSourceCard = Card.builder().description(sourceCard.getDescription()).id(UUID.randomUUID().toString()).name(sourceCard.getName()).build();

        List<Lane> laneList = targetBoard.getLanes()
                .stream()
                .filter(laneInBoard -> laneInBoard.getId().equals(targetLane.getId()))
                .collect(Collectors.toList());
        if(!targetCard.isEmpty() && !targetCard.isBlank()) {
            Card targetInList = laneList.get(0).getCards().stream().filter(c -> c.getId().equals(targetCard)).findAny().get();
            int index = laneList.get(0).getCards().indexOf(targetInList);
            laneList.get(0).getCards().add(index, copyOfSourceCard);
        }
        else{
            laneList.get(0).getCards().add(copyOfSourceCard);
        }
        boardRepository.save(targetBoard);

        Board newSource = boardRepository.findBoardById(sourceBoard.getId());
        List<Lane> singleEntryList = newSource.getLanes().stream().filter(laneInBoard -> laneInBoard.getId().equals(sourceLane.getId())).collect(Collectors.toList());
        Lane correctLane = singleEntryList.get(0);
        correctLane.getCards().removeIf(cardInLane -> cardInLane.getId().equals(sourceCard.getId()));
        boardRepository.save(newSource);
    }

    public Lane getLaneById(String id) {
        for (Board board : boardRepository.findAll()) {
            for (Lane lane : board.getLanes()) {
                if (lane.getId().equals(id)) {
                    return lane;
                }
            }
        }
        //TODO: HANDLE THIS
        return null;
    }
}
