package com.hiricus.dcs.service;

import com.hiricus.dcs.dto.TemplateDto;
import com.hiricus.dcs.dto.request.DocumentGenerationRequest;
import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/v1/templates")
public class TemplateController {
    private final TemplateService templateService;

    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @GetMapping("/describe")
    public ResponseEntity<List<TemplateDto>> getAllTemplates() {
        List<DocumentTemplateObject> templates = templateService.getAllTemplates();

        List<TemplateDto> response = new ArrayList<>();
        templates.stream().forEach(template -> {
            TemplateDto templateDto = new TemplateDto(template.getTemplateType().name(), "name_placeholder", "description_placeholder");
            response.add(templateDto);
        });

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/generate")
    public ResponseEntity<?> generateDocumentFromTemplate(DocumentGenerationRequest request) {

        // TODO: сделать логику генерации документов
        return new ResponseEntity<>("Пока логики генерации нет", HttpStatus.OK);
    }
}
