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
public class GroupCreationRequest {
    private String name;
    @JsonProperty("curator_id")
    private Integer curatorId;
    @JsonProperty("head_id")
    private Integer headId;
    private Integer course;
    @JsonProperty("entrance_year")
    private Integer entranceYear;
}
