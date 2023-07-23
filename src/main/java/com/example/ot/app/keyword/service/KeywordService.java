package com.example.ot.app.keyword.service;

import com.example.ot.app.keyword.entity.Keyword;
import com.example.ot.app.keyword.repository.KeywordRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    public Keyword saveKeyword(String keywordContent) {
        Optional<Keyword> optKeyword = keywordRepository.findByContent(keywordContent);

        if ( optKeyword.isPresent() ) {
            return optKeyword.get();
        }

        Keyword keyword = Keyword.of(keywordContent);

        keywordRepository.save(keyword);

        return keyword;
    }
}
