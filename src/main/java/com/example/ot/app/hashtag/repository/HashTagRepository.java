package com.example.ot.app.hashtag.repository;

import com.example.ot.app.hashtag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByHostIdAndKeywordId(Long hostId, Long keywordId);
    List<HashTag> findByHostId(Long hostId);
}
