package com.example.ot.app.board.repository;

import com.example.ot.app.board.dto.response.ShowBoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TravelBoardRepositoryCustom {

    Slice<ShowBoardResponse> findMyBoardsWithKeysetPaging(Long lastBoardId, Long memberId, Pageable pageable);

    Slice<ShowBoardResponse> findBoardsByRegionWithKeysetPaging(Integer regionCode, Long lastBoardId, Pageable pageable);
}
