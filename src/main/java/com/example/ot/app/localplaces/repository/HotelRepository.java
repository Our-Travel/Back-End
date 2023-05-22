package com.example.ot.app.localplaces.repository;

import com.example.ot.app.localplaces.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
