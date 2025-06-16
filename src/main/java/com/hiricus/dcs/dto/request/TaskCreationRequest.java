package com.hiricus.dcs.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TaskCreationRequest {
    @JsonProperty("task_name")
    private String taskName;
    @JsonProperty("task_type")
    private String taskType;
    @JsonProperty("group_id")
    private Integer groupId;
}
