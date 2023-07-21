package com.example.ot.app.board.controller;

import com.example.ot.app.board.dto.request.CreateBoardRequest;
import com.example.ot.app.board.service.BoardService;
import com.example.ot.base.rsData.RsData;
import com.example.ot.config.security.entity.MemberContext;
import com.example.ot.util.Util;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.example.ot.app.board.code.BoardSuccessCode.BOARD_CREATED;

@Tag(name = "게시판")
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/boards")
public class BoardController {

    private final BoardService boardService;

    @Operation(summary = "게시판 생성", security = @SecurityRequirement(name = "bearerAuth"))
    @PreAuthorize("isAuthenticated()")
    @PostMapping
    public ResponseEntity<RsData> createBoard(@Valid @RequestBody CreateBoardRequest createBoardRequest,
                                               @AuthenticationPrincipal MemberContext memberContext){
        boardService.createBoard(createBoardRequest, memberContext.getId());
        return Util.spring.responseEntityOf(RsData.success(BOARD_CREATED));
    }
}
