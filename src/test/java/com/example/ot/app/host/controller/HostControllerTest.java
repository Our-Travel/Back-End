package com.example.ot.app.host.controller;

import com.example.ot.app.keyword.repository.KeywordRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class HostControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private KeywordRepository keywordRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Test
    @DisplayName("host 등록")
    @WithUserDetails("user1@example.com")
    void t1() throws Exception {
        // When
        Member member = memberRepository.findByUsername("user1@example.com").orElse(null);

        ResultActions resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hash_tag": "#호스트 #여행",
                                        "region": "관악구"
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is2xxSuccessful());
        assertThat(member.isHostAuthority()).isTrue();
        assertThat(keywordRepository.count()).isEqualTo(2);
    }

    @Test
    @DisplayName("host 등록에서 지역을 안넣는 경우 오류발생")
    @WithUserDetails("user1@example.com")
    void t2() throws Exception {
        // When
        ResultActions resultActions = mvc
                .perform(
                        post("/api/hosts")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hash_tag": "#호스트 #여행"
                                    }
                                    """)
                                .contentType(new MediaType(MediaType.APPLICATION_JSON, StandardCharsets.UTF_8))
                )
                .andDo(print());

        // Then
        resultActions
                .andExpect(status().is4xxClientError());

    }

}
