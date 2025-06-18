package com.hiricus.dcs.controller;

import com.hiricus.dcs.dto.DocumentDto;
import com.hiricus.dcs.dto.TemplateDto;
import com.hiricus.dcs.dto.request.DocumentGenerationRequest;
import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import com.hiricus.dcs.model.object.document.template.TemplateType;
import com.hiricus.dcs.service.template.TemplateService;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    @PostMapping("/addNew")
    public ResponseEntity<Integer> createNewTemplate(@RequestParam String type,
                                                     @RequestParam String mappings,
                                                     @RequestBody MultipartFile file) throws IOException {
        TemplateType templateType = TemplateType.valueOf(type);
        byte[] data = file.getBytes();

        Integer response = templateService.createTemplate(templateType, mappings, data);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/generate")
    public ResponseEntity<DocumentDto> generateDocumentFromTemplate(@RequestBody DocumentGenerationRequest request) {

        System.out.println("Request: " + request);
        DocumentDto response  = templateService.getFilledTemplate(TemplateType.valueOf(request.getType()), request.getUserId());

        // TODO: Сделать логику формирования ответа
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
