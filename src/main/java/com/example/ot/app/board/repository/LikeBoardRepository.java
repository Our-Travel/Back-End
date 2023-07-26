package com.example.ot.app.board.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LikeBoardRepository extends JpaRepository<LikeBoardRepository, Long> {

    @Query("select l from LikeBoard l join fetch l.member m join fetch l.travelBoard t " +
            "where m.id = :memberId and t.id = :boardId")
    boolean existsByMember(@Param("boardId") Long boardId, @Param("memberId") Long memberId);
}
