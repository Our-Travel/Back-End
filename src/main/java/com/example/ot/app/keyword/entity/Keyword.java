package com.example.ot.app.keyword.entity;

import com.example.ot.base.entity.BaseTimeEntity;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Keyword extends BaseTimeEntity {
    private String content;

    public static Keyword of(String keywordContent) {
        return new Keyword(keywordContent);
    }
}
