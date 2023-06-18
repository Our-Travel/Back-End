package com.example.ot.app.host.service;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.hashtag.service.HashTagService;
import com.example.ot.app.host.dto.HostDTO;
import com.example.ot.app.host.dto.RegionDTO;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.region.entity.City;
import com.example.ot.app.region.entity.State;
import com.example.ot.app.region.repository.CityRepository;
import com.example.ot.app.region.repository.StateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;
    private final MemberRepository memberRepository;
    private final CityRepository cityRepository;
    private final StateRepository stateRepository;
    private final HashTagService hashTagService;

    @Transactional
    public RsData<Host> createHost(HostDTO.RegisterHostDTO registerHostDTO, long id){
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null){
            return RsData.of("F-1", "사용자를 찾을 수 없습니다.");
        }
        City city = cityRepository.findById(registerHostDTO.getCity()).orElse(null);
        if(city == null){
            return RsData.of("F-1", "지역을 선택해주세요.");
        }
        Host host = Host
                .builder()
                .introduction(registerHostDTO.getIntroduction())
                .member(member)
                .city(city)
                .build();
        hostRepository.save(host);
        member.setHostAuthority(true);
        hashTagService.applyHashTags(host, registerHostDTO.getHashTag());
        return RsData.of("S-1", "Host 등록이 완료되었습니다.");
    }

    public RegionDTO getRegion() {
        List<City> cityList = new ArrayList<>(cityRepository.findAll());
        List<State> stateList = new ArrayList<>(stateRepository.findAll());

        return RegionDTO.builder()
                .cityList(cityList)
                .stateList(stateList)
                .build();
    }
}
