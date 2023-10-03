package com.example.ot.base.schedule;

import com.example.ot.app.board.entity.RecruitmentStatus;
import com.example.ot.app.board.repository.TravelBoardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
public class ScheduledTasks {

    @Autowired
    private TravelBoardRepository travelBoardRepository;

    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void updateRecruitmentStatusToOpen() {
        LocalDate currentDate = LocalDate.now();

        travelBoardRepository.updateStatusToOpenForToday(RecruitmentStatus.UPCOMING, RecruitmentStatus.OPEN, currentDate);
    }

    @Scheduled(cron = "0 0 0 * * ?") // 매일 00:00:00에 실행
    @Transactional
    public void updateRecruitmentStatusToClose() {
        LocalDate currentDate = LocalDate.now();
        travelBoardRepository.updateStatusOnRecruitmentPeriodEnd(RecruitmentStatus.OPEN, RecruitmentStatus.CLOSED, currentDate);
    }
}
