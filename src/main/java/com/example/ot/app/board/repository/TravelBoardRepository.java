package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.exception.TravelBoardException;
import org.springframework.data.jpa.repository.JpaRepository;

import static com.example.ot.app.board.exception.ErrorCode.BOARD_NOT_EXISTS;

public interface TravelBoardRepository extends JpaRepository<TravelBoard, Long>, TravelBoardRepositoryCustom {

    default TravelBoard findByBoardId(Long boardId){
        return findById(boardId).orElseThrow(() -> new TravelBoardException(BOARD_NOT_EXISTS));
    }
}
