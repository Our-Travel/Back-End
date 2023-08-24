package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.LikeBoard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LikeBoardRepository extends JpaRepository<LikeBoard, Long> {

    @Query("select l.id from LikeBoard l join l.member m join l.travelBoard t " +
            "where m.id = :memberId and t.id = :boardId")
    Optional<LikeBoard> findByMemberAndBoard(@Param("boardId") Long boardId, @Param("memberId") Long memberId);

    @Query("select count(*) from LikeBoard l join l.travelBoard t where t.id = :boardId")
    Long countByTravelBoard(@Param("boardId") Long boardId);
}
