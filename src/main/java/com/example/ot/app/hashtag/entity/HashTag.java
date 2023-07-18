package com.example.ot.app.hashtag.entity;

import com.example.ot.base.entity.BaseTimeEntity;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.keyword.entity.Keyword;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class HashTag extends BaseTimeEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    private Host host;

    @ManyToOne(fetch = FetchType.LAZY)
    private Keyword keyword;
}
