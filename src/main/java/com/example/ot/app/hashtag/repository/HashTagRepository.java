package com.example.ot.app.hashtag.repository;

import com.example.ot.app.hashtag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByHostIdAndKeywordId(Long hostId, Long keywordId);

    void deleteAllByHost_Id(Long hostId);

    @Query("select h from HashTag h join fetch h.keyword where h.host.id = :hostId")
    List<HashTag> findByHostId(@Param("hostId") Long hostId);
}
