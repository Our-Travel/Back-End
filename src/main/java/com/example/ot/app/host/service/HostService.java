package com.example.ot.app.host.service;

import com.example.ot.app.base.rsData.RsData;
import com.example.ot.app.host.dto.HostDTO;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.host.repository.HostRepository;
import com.example.ot.app.member.entity.Member;
import com.example.ot.app.member.repository.MemberRepository;
import com.example.ot.app.region.entity.City;
import com.example.ot.app.region.repository.CityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HostService {

    private final HostRepository hostRepository;
    private final MemberRepository memberRepository;
    private final CityRepository cityRepository;

    @Transactional
    public RsData<Host> createHost(HostDTO.registerHostDTO registerHostDTO, long id){
        Member member = memberRepository.findById(id).orElse(null);
        if(member == null){
            return RsData.of("F-1", "사용자를 찾을 수 없습니다.");
        }
//        City city = cityRepository.findById(registerHostDTO.getCity()).orElse(null);
//        if(city == null){
//            return RsData.of("F-1", "지역을 선택해주세요.");
//        }
        Host host = Host
                .builder()
                .introduction(registerHostDTO.getIntroduction())
                .member(member)
//                .city(city)
                .build();
        hostRepository.save(host);
        member.setHostAuthority(true);

        return RsData.of("S-1", "Host 등록이 완료되었습니다.");
    }

}
