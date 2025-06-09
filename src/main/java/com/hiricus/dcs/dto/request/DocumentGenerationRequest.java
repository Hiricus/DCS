package com.hiricus.dcs.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class DocumentGenerationRequest {
    private String type;
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("file_format")
    private String fileFormat;
}
