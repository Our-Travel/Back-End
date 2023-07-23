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
                .filter(s -> s.length() > 0).toList();

        keywordContents.forEach(keywordContent -> {
            saveHashTag(host, keywordContent);
        });
    }

    private void saveHashTag(Host host, String keywordContent) {
        Keyword keyword = keywordService.saveKeyword(keywordContent);

        Optional<HashTag> opHashTag = hashTagRepository.findByHostIdAndKeywordId(host.getId(), keyword.getId());

        if (opHashTag.isPresent()) {
            return;
        }

        HashTag hashTag = HashTag.of(host, keyword);
        hashTagRepository.save(hashTag);
    }

    public String getHashTag(Long hostId){
        List<HashTag> hashTags = hashTagRepository.findByHostId(hostId);
        return hashTags.stream()
                .map(hashTag -> "#" + hashTag.getKeyword().getContent())
                .collect(Collectors.joining());
    }

    public void updateHashTag(String hashTag, Host host){
        deleteHashTag(host.getId());
        applyHashTags(host, hashTag);
    }

    public void deleteHashTag(Long hostId) {
        List<HashTag> hostHashTags = hashTagRepository.findByHostId(hostId);
        hashTagRepository.deleteAll(hostHashTags);
    }
}
