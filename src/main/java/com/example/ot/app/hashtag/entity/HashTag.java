package com.example.ot.app.hashtag.entity;

import com.example.ot.app.host.entity.Host;
import com.example.ot.app.keyword.entity.Keyword;
import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@SuperBuilder
@SQLDelete(sql = "UPDATE hash_tag SET deleted_date = NOW() where id = ?")
@Where(clause = "deleted_date is NULL")
public class HashTag extends BaseTimeEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Host host;

    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;

    public static HashTag of(Host host, Keyword keyword){
        return HashTag.builder()
                .host(host)
                .keyword(keyword)
                .build();
    }
}
