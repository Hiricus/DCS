package com.hiricus.dcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiricus.dcs.model.object.document.template.TemplateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TemplateDto {
    @JsonProperty("type")
    private String templateType;
    private String name;
    private String description;
}
