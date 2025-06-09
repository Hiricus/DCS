package com.hiricus.dcs.service;

import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import com.hiricus.dcs.model.repository.DocumentTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TemplateService {
    private final DocumentTemplateRepository templateRepository;

    public TemplateService(DocumentTemplateRepository templateRepository) {
        this.templateRepository = templateRepository;
    }

    @Transactional
    public List<DocumentTemplateObject> getAllTemplates() {
        return templateRepository.findAll();
    }
}
