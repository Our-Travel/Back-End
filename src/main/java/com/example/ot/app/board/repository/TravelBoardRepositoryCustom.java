package com.example.ot.app.board.repository;

import com.example.ot.app.board.dto.response.BoardListResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TravelBoardRepositoryCustom {

    Slice<BoardListResponse> findMyBoardsWithKeysetPaging(Long lastBoardId, Long memberId, Pageable pageable);

    Slice<BoardListResponse> findBoardsByRegionWithKeysetPaging(Integer regionCode, Long lastBoardId, Pageable pageable);
}
