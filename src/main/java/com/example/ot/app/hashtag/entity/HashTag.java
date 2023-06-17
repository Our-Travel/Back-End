package com.example.ot.app.hashtag.entity;

import com.example.ot.app.base.entity.BaseTimeEntity;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.keyword.entity.Keyword;
import jakarta.persistence.Entity;
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
    @ManyToOne
    private Host host;
    @ManyToOne
    private Keyword keyword;
}
