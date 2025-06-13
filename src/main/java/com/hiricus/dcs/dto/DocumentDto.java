package com.hiricus.dcs.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.hiricus.dcs.model.object.document.DocumentObject;
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

    public DocumentDto(String name, Long sizeBytes, byte[] data) {
        this.name = name;
        this.sizeBytes = sizeBytes;
        this.contendBase64 = Base64.getEncoder().encodeToString(data);
    }
}
