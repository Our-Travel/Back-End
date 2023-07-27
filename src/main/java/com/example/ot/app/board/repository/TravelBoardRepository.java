package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.TravelBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TravelBoardRepository extends JpaRepository<TravelBoard, Long> {

    @Query("select t from TravelBoard t join fetch t.member m where t.id = :boardId")
    Optional<TravelBoard> findByBoardIdWithWriter(@Param("boardId") Long boardId);

    @Query("select t from TravelBoard t join fetch t.member m where m.id = :memberId")
    List<TravelBoard> getTravelBoardListByMember(@Param("memberId") Long memberId);
}
