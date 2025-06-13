package com.hiricus.dcs.service.template;

import com.hiricus.dcs.dto.DocumentDto;
import com.hiricus.dcs.exception.EntityNotFoundException;
import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import com.hiricus.dcs.model.object.document.template.TemplateType;
import com.hiricus.dcs.model.repository.DocumentTemplateRepository;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
public class TemplateService {
    private final DocumentTemplateRepository templateRepository;
    private final TemplateFiller templateFiller;

    public TemplateService(DocumentTemplateRepository templateRepository,
                           TemplateFiller templateFiller) {
        this.templateRepository = templateRepository;
        this.templateFiller = templateFiller;
    }

    @Transactional
    public List<DocumentTemplateObject> getAllTemplates() {
        return templateRepository.findAll();
    }

    @Transactional
    public DocumentDto getFilledTemplate(TemplateType type, Integer userId) {
        Optional<DocumentTemplateObject> template = templateRepository.findTemplateByType(type);
        if (template.isEmpty()) {
            throw new EntityNotFoundException("Template of type: " + type + " not found");
        }

        try {
            XWPFDocument filledTemplate = templateFiller.fillTemplate(template.get(), userId);


            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            filledTemplate.write(bos);
            byte[] templateData = bos.toByteArray();

            // TODO: убрать
            // Кусок кода для записи сгенеренного файла
//            {
//                FileOutputStream fos = new FileOutputStream("C:/Users/user/Desktop/Replacement_test/Filled_template.docx");
//                filledTemplate.write(fos);
//
//                fos.close();
//                filledTemplate.close();
//            }


            return new DocumentDto("FilledTemplate.docx", 1L, templateData);
        } catch (IOException e) {
            throw new RuntimeException("Какая то фигня с заполнением темплейта в сервисном классе");
        }
    }
}
