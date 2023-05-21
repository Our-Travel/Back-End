package com.example.ot.app.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class KakaoApiResponseDTO {

    @JsonProperty("meta")
    private MetaDTO metaDTO;

    @JsonProperty("documents")
    private List<DocumentDTO> documentDTOList;
}
