package com.hiricus.dcs.dto.task;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiricus.dcs.model.object.task.TaskObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDto {
    private Integer id;
    private String name;
    private String type;

    @JsonProperty("is_completed")
    private Boolean isCompleted;
    @JsonProperty("has_documents")
    private Boolean hasDocuments;
    @JsonProperty("group_id")
    private Integer groupId;
    @JsonProperty("group_name")
    private String groupName;

    public TaskDto(TaskObject task) {
        this.id = task.getId();
        this.name = task.getName();
        this.type = task.getType().toString();
        this.isCompleted = task.isCompleted();
        this.hasDocuments = task.isHasDocuments();
        this.groupId = task.getRelatedGroup() != null ? task.getRelatedGroup().getId() : null;
        this.groupName = task.getRelatedGroup() != null ? task.getRelatedGroup().getName() : "";
    }
}
