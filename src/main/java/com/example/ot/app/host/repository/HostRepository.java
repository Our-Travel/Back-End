package com.example.ot.app.host.repository;

import com.example.ot.app.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HostRepository extends JpaRepository<Host, Long> {
    Optional<Host> findByMemberId(long id);

    @Query("select h from Host h where h.regionCode = :regionCode")
    Optional<List<Host>> findHostByRegionCode(@Param("regionCode")Integer regionCode);
}
