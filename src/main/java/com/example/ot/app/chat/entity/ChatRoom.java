package com.example.ot.app.chat.entity;

import com.example.ot.app.board.entity.TravelBoard;
import com.example.ot.app.host.entity.Host;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE chat_room SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class ChatRoom extends BaseTimeEntity {

    private String title;
    private Integer currentNumber;

    @OneToOne(fetch = FetchType.LAZY)
    private TravelBoard travelBoard;

    @ManyToOne(fetch = FetchType.LAZY)
    private Host host;

    public static ChatRoom of(TravelBoard travelBoard){
        return ChatRoom.builder()
                .title(travelBoard.getTitle())
                .travelBoard(travelBoard)
                .currentNumber(1)
                .build();

    }
}
