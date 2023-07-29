package com.example.ot.app.board.repository;

import com.example.ot.app.board.dto.response.BoardListResponse;
import com.example.ot.app.board.entity.TravelBoard;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

import static com.example.ot.app.board.entity.QTravelBoard.travelBoard;

import java.util.List;

@RequiredArgsConstructor
public class TravelBoardRepositoryImpl implements TravelBoardRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public Slice<TravelBoard> findMyBoardsWithKeysetPaging(Long lastBoardId, Long memberId, Pageable pageable) {
        BooleanExpression boardIdLt = lastBoardId != null ? travelBoard.id.lt(lastBoardId) : null;
        BooleanExpression boardByMemberEq = memberId != null ? travelBoard.member.id.eq(memberId) : null;

        List<TravelBoard> results = jpaQueryFactory.selectFrom(travelBoard)
                .where(boardIdLt, boardByMemberEq)
                .orderBy(travelBoard.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    @Override
    public Slice<TravelBoard> findBoardsByRegionWithKeysetPaging(Integer regionCode, Long lastBoardId, Long memberId, Pageable pageable) {
        BooleanExpression boardByRegionCodeEq = regionCode != null ? travelBoard.regionCode.eq(regionCode) : null;
        BooleanExpression boardIdLt = lastBoardId != null ? travelBoard.id.lt(lastBoardId) : null;

        List<TravelBoard> results = jpaQueryFactory.selectFrom(travelBoard)
                .where(boardIdLt, boardByRegionCodeEq)
                .orderBy(travelBoard.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results);
    }

    private Slice<TravelBoard> checkLastPage(Pageable pageable, List<TravelBoard> results) {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        return new SliceImpl<>(results, pageable, hasNext);
    }
}
