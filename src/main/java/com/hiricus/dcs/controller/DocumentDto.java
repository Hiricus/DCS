package com.hiricus.dcs.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiricus.dcs.model.object.document.DocumentObject;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Base64;

@Getter
@Setter
@NoArgsConstructor
public class DocumentDto {
    private String name;

    @JsonProperty("size_bytes")
    private Long sizeBytes;

    @JsonProperty("data")
    private String contendBase64;

    public DocumentDto(DocumentObject document) {
        this.name = document.getFileName();
        this.sizeBytes = document.getSizeBytes();
        this.contendBase64 = Base64.getEncoder().encodeToString(document.getData());
    }
}
