package com.example.ot.app.hashtag.service;

import com.example.ot.app.hashtag.entity.HashTag;
import com.example.ot.app.hashtag.repository.HashTagRepository;
import com.example.ot.app.host.entity.Host;
import com.example.ot.app.keyword.entity.Keyword;
import com.example.ot.app.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final KeywordService keywordService;
    private final HashTagRepository hashTagRepository;

    public void applyHashTags(Host host, String keywordContentsStr) {
        List<String> keywordContents = Arrays.stream(keywordContentsStr.split("#"))
                .map(String::trim)
                .filter(s -> s.length() > 0)
                .collect(Collectors.toList());

        keywordContents.forEach(keywordContent -> {
            saveHashTag(host, keywordContent);
        });
    }

    private HashTag saveHashTag(Host host, String keywordContent) {
        Keyword keyword = keywordService.save(keywordContent);

        Optional<HashTag> opHashTag = hashTagRepository.findByArticleIdAndKeywordId(host.getId(), keyword.getId());

        if (opHashTag.isPresent()) {
            return opHashTag.get();
        }

        HashTag hashTag = HashTag.builder()
                .host(host)
                .keyword(keyword)
                .build();

        hashTagRepository.save(hashTag);

        return hashTag;
    }
}
