package com.example.ot.app.user.entity;

import com.example.ot.app.base.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Getter
@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseTimeEntity {

    @Column(unique = true)
    private String email;

    private String password;

    @Column(unique = true)
    private String nickName;

    private String regionLevel1;

    private String regionLevel2;
}
