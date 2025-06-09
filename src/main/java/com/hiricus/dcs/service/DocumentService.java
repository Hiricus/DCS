package com.hiricus.dcs.service;

import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.model.repository.DocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class DocumentService {
    private final DocumentRepository documentRepository;

    public DocumentService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    @Transactional
    public DocumentObject getDocumentById(Long id) {
        Optional<DocumentObject> document = documentRepository.findDocumentById(id);

        if (document.isEmpty()) {
            throw new EntityNotFoundException("Document not found");
        }

        return document.get();
    }

    @Transactional
    public Long createDocument(DocumentObject document) {
        return documentRepository.createDocument(document).get();
    }
}
