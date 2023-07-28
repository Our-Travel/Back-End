package com.example.ot.app.board.repository;

import com.example.ot.app.board.dto.response.ShowBoardResponse;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

public interface TravelBoardRepositoryCustom {

    Slice<ShowBoardResponse> findAllMyBoardsWithKeysetPaging(Long lastBoardId, Long memberId, Pageable pageable);
}
