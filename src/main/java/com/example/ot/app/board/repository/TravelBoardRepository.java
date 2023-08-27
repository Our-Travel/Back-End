package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.RecruitmentStatus;
import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.exception.TravelBoardException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;

import static com.example.ot.app.board.exception.ErrorCode.BOARD_NOT_EXISTS;

public interface TravelBoardRepository extends JpaRepository<TravelBoard, Long>, TravelBoardRepositoryCustom {

    default TravelBoard findByBoardId(Long boardId){
        return findById(boardId).orElseThrow(() -> new TravelBoardException(BOARD_NOT_EXISTS));
    }

    @Modifying
    @Query("UPDATE TravelBoard t SET t.recruitmentStatus = :newStatus WHERE t.recruitmentStatus = :currentStatus AND t.recruitmentPeriodStart = :currentDate")
    void updateStatusToOpenForToday(@Param("currentStatus") RecruitmentStatus currentStatus, @Param("newStatus") RecruitmentStatus newStatus, @Param("currentDate") LocalDate currentDate);

    @Modifying
    @Query("UPDATE TravelBoard t SET t.recruitmentStatus = :newStatus WHERE t.recruitmentStatus = :currentStatus AND t.recruitmentPeriodEnd = :currentDate")
    void updateStatusOnRecruitmentPeriodEnd(@Param("currentStatus") RecruitmentStatus currentStatus, @Param("newStatus") RecruitmentStatus newStatus, @Param("currentDate") LocalDate currentDate);
}
