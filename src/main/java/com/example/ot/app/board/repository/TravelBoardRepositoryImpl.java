package com.example.ot.app.board.repository;

import com.example.ot.app.board.dto.response.ShowBoardResponse;
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
    public Slice<ShowBoardResponse> findMyBoardsWithKeysetPaging(Long lastBoardId, Long memberId, Pageable pageable) {
        BooleanExpression boardIdLt = lastBoardId != null ? travelBoard.id.lt(lastBoardId) : null;
        BooleanExpression boardByMemberEq = memberId != null ? travelBoard.member.id.eq(memberId) : null;

        List<TravelBoard> results = jpaQueryFactory.selectFrom(travelBoard)
                .where(boardIdLt, boardByMemberEq)
                .orderBy(travelBoard.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results, memberId);
    }

    @Override
    public Slice<ShowBoardResponse> findBoardsByRegionWithKeysetPaging(Integer regionCode, Long lastBoardId, Pageable pageable) {
        BooleanExpression boardByRegionCodeEq = regionCode != null ? travelBoard.regionCode.eq(regionCode) : null;
        BooleanExpression boardIdLt = lastBoardId != null ? travelBoard.id.lt(lastBoardId) : null;

        List<TravelBoard> results = jpaQueryFactory.selectFrom(travelBoard)
                .where(boardIdLt, boardByRegionCodeEq)
                .orderBy(travelBoard.id.desc())
                .limit(pageable.getPageSize() + 1)
                .fetch();

        return checkLastPage(pageable, results, null);
    }

    private Slice<ShowBoardResponse> checkLastPage(Pageable pageable, List<TravelBoard> results, Long memberId) {
        boolean hasNext = false;

        if (results.size() > pageable.getPageSize()) {
            hasNext = true;
            results.remove(pageable.getPageSize());
        }

        List<ShowBoardResponse> BoardList = results.stream().map(e -> ShowBoardResponse.fromTravelBoard(e, memberId)).toList();
        return new SliceImpl<>(BoardList, pageable, hasNext);
    }
}
