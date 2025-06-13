package com.hiricus.dcs.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DocumentGenerationRequest {
    private String type;
    @JsonProperty("user_id")
    private Integer userId;

    @JsonProperty("file_format")
    private String fileFormat;
}
