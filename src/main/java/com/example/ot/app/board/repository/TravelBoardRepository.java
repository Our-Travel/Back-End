package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.exception.TravelBoardException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

import static com.example.ot.app.board.exception.ErrorCode.BOARD_NOT_EXISTS;

public interface TravelBoardRepository extends JpaRepository<TravelBoard, Long>, TravelBoardRepositoryCustom {

    @Query("select t from TravelBoard t join fetch t.member m where t.id = :boardId")
    Optional<TravelBoard> findByBoardIdWithWriter(@Param("boardId") Long boardId);

    default TravelBoard findByBoardId(Long boardId){
        return findById(boardId).orElseThrow(() -> new TravelBoardException(BOARD_NOT_EXISTS));
    }
}
