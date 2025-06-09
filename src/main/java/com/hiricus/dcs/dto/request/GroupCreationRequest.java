package com.hiricus.dcs.dto.request;

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
    private Integer curatorId;
    private Integer headId;
    private Integer course;
    private Integer entranceYear;
}
