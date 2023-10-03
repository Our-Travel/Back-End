package com.example.ot.app.host.repository;

import com.example.ot.app.host.dto.response.HostCountResponse;
import com.example.ot.app.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    Optional<Host> findHostByMember_Id(Long id);

    @Query("select h from Host h where h.regionCode = :regionCode")
    List<Host> findHostByRegionCode(@Param("regionCode")Integer regionCode);

    @Query("select new com.example.ot.app.host.dto.response.HostCountResponse(h.regionCode, count(h)) from Host h group by h.regionCode")
    List<HostCountResponse> countHostsByRegionCode();
}
