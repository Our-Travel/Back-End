package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {

    @Query("select l from LikeBoard l join fetch l.member m join fetch l.travelBoard t " +
            "where m.id = :memberId and t.id = :boardId")
    Optional<LikeBoard> findByMemberAndBoard(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}
