package com.example.ot.app.board.entity;

import com.example.ot.app.member.entity.Member;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.GenerationType.IDENTITY;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
public class LikeBoard {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TravelBoard travelBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdDate;

}
