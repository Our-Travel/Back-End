package com.example.ot.app.region.repository;

import com.example.ot.app.member.entity.Member;
import com.example.ot.app.region.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CityRepository extends JpaRepository<City, Integer> {
}

