package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.DocumentDto;
import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.service.DocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/documents")
public class DocumentController {
    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<DocumentDto> getFile(@PathVariable Long id) {
        DocumentObject document = documentService.getDocumentById(id);
        DocumentDto response = new DocumentDto(document);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Long> createFile(@RequestBody MultipartFile file) {
        DocumentObject document;
        try {
            document = new DocumentObject(file);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Long documentId = documentService.createDocument(document);
        return new ResponseEntity<>(documentId, HttpStatus.CREATED);
    }
}
