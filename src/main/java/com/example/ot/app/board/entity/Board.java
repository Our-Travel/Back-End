package com.example.ot.app.board.entity;

import com.example.ot.app.board.dto.request.CreateBoardRequest;
import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Getter
public class Board extends BaseTimeEntity {

    private String title;
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    public static Board of(CreateBoardRequest createBoardRequest, Member member){
        return Board.builder()
                .title(createBoardRequest.getTitle())
                .content(createBoardRequest.getContent())
                .member(member)
                .build();
    }
}
