package com.example.ot.app.board.controller;

import com.example.ot.app.board.dto.request.CreateBoardRequest;
import com.example.ot.app.board.dto.request.EditBoardRequest;
import com.example.ot.app.board.dto.response.BoardListResponse;
import com.example.ot.app.board.dto.response.EditBoardResponse;
import com.example.ot.app.board.dto.response.ShowBoardResponse;
import com.example.ot.app.board.service.TravelBoardService;
import com.example.ot.base.code.Code;
import com.example.ot.base.rsData.RsData;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static com.example.ot.app.board.code.TravelBoardSuccessCode.*;

@Tag(name = "동행 게시판")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class TravelBoardController {

    private final TravelBoardService travelBoardService;

    @Operation(summary = "게시글들 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping
    public ResponseEntity<RsData> showBoardList(@RequestParam(value = "regionCode", required = false) Integer regionCode,
                                                @RequestParam(value = "lastId", required = false) Long lastBoardId,
                                                  @AuthenticationPrincipal MemberContext memberContext){
        Slice<BoardListResponse> BoardList = travelBoardService.getBoardListByRegion(regionCode, lastBoardId);
        return Util.spring.responseEntityOf(RsData.success(BOARD_LIST, BoardList));
    }

    @Operation(summary = "내가 작성한 게시글들 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/my")
    public ResponseEntity<RsData> showMyBoardList(@RequestParam(value = "lastId", required = false) Long lastBoardId,
                                                  @AuthenticationPrincipal MemberContext memberContext){
        Slice<BoardListResponse> allMyBoardList = travelBoardService.getMyBoardList(lastBoardId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(BOARD_LIST_BY_MEMBER, allMyBoardList));
    }

    @Operation(summary = "동행 구하기 게시판 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/{boardId}")
    public ResponseEntity<RsData> showBoard(@PathVariable Long boardId,
                                            @AuthenticationPrincipal MemberContext memberContext){
        ShowBoardResponse showBoardResponse = travelBoardService.getBoardInfo(boardId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(BOARD_FOUND, showBoardResponse));
    }

    @Operation(summary = "동행 구하기 게시판 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping
    public ResponseEntity<RsData> createBoard(@Valid @RequestBody CreateBoardRequest createBoardRequest,
                                              @AuthenticationPrincipal MemberContext memberContext){
        travelBoardService.createBoard(createBoardRequest, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(BOARD_CREATED));
    }

    @Operation(summary = "동행 구하기 게시판 수정 페이지 조회", security = @SecurityRequirement(name = "bearerAuth"))
    @GetMapping("/edit/{boardId}")
    public ResponseEntity<RsData> updateBoardPage(@PathVariable Long boardId,
                                            @AuthenticationPrincipal MemberContext memberContext){
        EditBoardResponse editBoardResponse = travelBoardService.getBoardInfoForEdit(boardId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(BOARD_EDIT_PAGE, editBoardResponse));
    }

    @Operation(summary = "동행 구하기 게시판 수정", security = @SecurityRequirement(name = "bearerAuth"))
    @PatchMapping("/edit/{boardId}")
    public ResponseEntity<RsData> updateBoard(@Valid @RequestBody EditBoardRequest editBoardRequest, @PathVariable Long boardId,
                                              @AuthenticationPrincipal MemberContext memberContext){
        travelBoardService.updateBoard(editBoardRequest, memberContext.getId(), boardId);
        return Util.spring.responseEntityOf(RsData.success(BOARD_UPDATED));
    }

    @Operation(summary = "동행 구하기 게시판 삭제", security = @SecurityRequirement(name = "bearerAuth"))
    @DeleteMapping("/{boardId}")
    public ResponseEntity<RsData> deleteBoard(@PathVariable Long boardId,
                                              @AuthenticationPrincipal MemberContext memberContext){
        travelBoardService.deleteBoard(boardId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(BOARD_DELETED));
    }

    @Operation(summary = "동행 구하기 게시판 좋아요 및 좋아요 취소", security = @SecurityRequirement(name = "bearerAuth"))
    @PostMapping("/{boardId}/likes")
    public ResponseEntity<RsData> likeBoard(@PathVariable Long boardId,
                                            @AuthenticationPrincipal MemberContext memberContext){
        Code likeBoardResult = travelBoardService.likeBoard(boardId, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(likeBoardResult));
    }
}
