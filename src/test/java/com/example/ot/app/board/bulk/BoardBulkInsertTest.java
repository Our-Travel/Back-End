package com.example.ot.app.board.bulk;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.board.repository.TravelBoardRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.util.BoardFixtureFactory;
import org.jeasy.random.EasyRandom;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.IntStream;

@SpringBootTest
@ActiveProfiles("test")
public class BoardBulkInsertTest {

    @Autowired
    private TravelBoardRepository travelBoardRepository;

    @Test
    void bulkInsert() {
        EasyRandom board = BoardFixtureFactory.getBoard();

        List<TravelBoard> travelBoardList = IntStream.range(0, 5)
                .mapToObj(i -> board.nextObject(TravelBoard.class))
                .toList();

        travelBoardRepository.bulkInsert(travelBoardList);
    }
}
