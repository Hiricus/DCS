package com.hiricus.dcs.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
public class DisciplineDto {
    private final int id;
    private final String name;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public DisciplineDto(@JsonProperty("id") int id,
                         @JsonProperty("name") String name) {
        this.id = id;
        this.name = name;
    }
}
