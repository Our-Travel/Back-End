package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.TravelBoard;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TravelBoardRepositoryCustom {

    Slice<TravelBoard> findMyBoardsWithKeysetPaging(Long lastBoardId, Long memberId, Pageable pageable);

    Slice<TravelBoard> findBoardsByRegionWithKeysetPaging(Integer regionCode, Long lastBoardId, Long memberId, Pageable pageable);
}
