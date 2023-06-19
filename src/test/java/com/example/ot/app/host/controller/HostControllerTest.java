package com.example.ot.app.host.controller;

import com.example.ot.app.keyword.repository.KeywordRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.region.entity.City;
import com.example.ot.app.region.entity.State;
import com.example.ot.app.region.repository.CityRepository;
import com.example.ot.app.region.repository.StateRepository;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
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
    @Autowired
    private CityRepository cityRepository;
    @Autowired
    private StateRepository stateRepository;

    @BeforeEach
    void beforeEach() {
        State state = State
                .builder()
                .id(1)
                .stateName("서울특별시")
                .build();
        stateRepository.save(state);
        City city = City
                .builder()
                .id(1)
                .cityName("관악구")
                .state(state)
                .build();
        cityRepository.save(city);
    }


    @Test
    @DisplayName("host 등록")
    @WithUserDetails("user1@example.com")
    void t1() throws Exception {
        // When
        Member member = memberRepository.findByUsername("user1@example.com").orElse(null);

        ResultActions resultActions = mvc
                .perform(
                        post("/api/host")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hashTag": "#호스트 #여행",
                                        "city": 1
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
                        post("/api/host")
                                .content("""
                                    {
                                        "introduction": "저는 호스트가 되고싶어요.",
                                        "hashTag": "#호스트 #여행"
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
