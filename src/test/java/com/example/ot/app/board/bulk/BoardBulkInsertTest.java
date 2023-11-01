package com.example.ot.app.board.bulk;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.repository.TravelBoardRepository;
import com.example.ot.util.BoardFixtureFactory;
import lombok.extern.slf4j.Slf4j;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StopWatch;

import java.util.List;
import java.util.stream.IntStream;

@Slf4j
@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class BoardBulkInsertTest {

    @Autowired
    private TravelBoardRepository travelBoardRepository;

    @Test
    void bulkInsert() {
        EasyRandom board = BoardFixtureFactory.getBoard();

        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        List<TravelBoard> travelBoardList = IntStream.range(0, 10000)
                .parallel()
                .mapToObj(i -> board.nextObject(TravelBoard.class))
                .toList();

        stopWatch.stop();
        log.info("객체 생성 시간 : {}", stopWatch.getTotalTimeSeconds());

        StopWatch queryStopWatch = new StopWatch();
        queryStopWatch.start();

        travelBoardRepository.bulkInsert(travelBoardList);

        queryStopWatch.stop();
        log.info("DB Insert 시간 : {}", queryStopWatch.getTotalTimeSeconds());
    }
}
