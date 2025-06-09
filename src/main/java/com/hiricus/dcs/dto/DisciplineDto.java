package com.hiricus.dcs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiricus.dcs.model.object.discipline.DisciplineObject;
import lombok.*;

@Getter
public class DisciplineDto {
    private int id;
    private String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DisciplineDto(@JsonProperty("id") int id,
                         @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }

    public DisciplineDto(DisciplineObject discipline) {
        this.id = discipline.getId();
        this.name = discipline.getName();
    }
}
