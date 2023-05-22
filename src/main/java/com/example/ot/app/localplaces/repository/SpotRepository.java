package com.example.ot.app.localplaces.repository;

import com.example.ot.app.localplaces.entity.Spot;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpotRepository extends JpaRepository<Spot, Long> {
}
