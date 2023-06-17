package com.example.ot.app.host.repository;

import com.example.ot.app.host.entity.Host;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HostRepository extends JpaRepository<Host, Long> {
}
