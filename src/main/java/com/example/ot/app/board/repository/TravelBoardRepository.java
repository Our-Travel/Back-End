package com.example.ot.app.board.repository;

import com.example.ot.app.board.entity.TravelBoard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TravelBoardRepository extends JpaRepository<TravelBoard, Long> {
}
