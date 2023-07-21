package com.example.ot.app.host.entity;

import com.example.ot.app.host.dto.request.WriteHostInfoRequest;
import com.example.ot.base.entity.BaseTimeEntity;
import com.example.ot.app.member.entity.Member;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@Entity
@Getter
@SQLDelete(sql = "UPDATE host SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class Host extends BaseTimeEntity {

    private String introduction;

    @OneToOne(fetch = FetchType.LAZY)
    private Member member;

    private Integer regionCode;

    public static Host of(WriteHostInfoRequest writeHostInfoRequest, Member member){
        return Host.builder()
                .introduction(writeHostInfoRequest.getIntroduction())
                .member(member)
                .regionCode(writeHostInfoRequest.getRegionCode())
                .build();
    }

    public void updateHostInfo(String introduction, Integer regionCode){
        this.introduction = introduction;
        this.regionCode = regionCode;
    }
}
