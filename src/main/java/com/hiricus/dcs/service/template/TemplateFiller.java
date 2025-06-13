package com.hiricus.dcs.service.template;

import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import com.hiricus.dcs.model.object.document.template.TemplateType;
import com.hiricus.dcs.model.repository.UserDataRepository;
import com.hiricus.dcs.model.repository.UserRepository;
import com.hiricus.dcs.util.documents.DocumentUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

// Содержит логику заполнения документов, а также поиска нужной инфы для этого
@Component
public class TemplateFiller {
    private final UserRepository userRepository;
    private final UserDataRepository userDataRepository;

    public TemplateFiller(UserRepository userRepository,
                          UserDataRepository userDataRepository) {
        this.userRepository = userRepository;
        this.userDataRepository = userDataRepository;
    }

    public XWPFDocument fillTemplate(DocumentTemplateObject template, Integer userId) throws IOException {
        XWPFDocument document = DocumentUtils.loadDocx(template);
        Map<String, String> mappings = template.getMappings();

        // Stub
        if (template.getTemplateType() == TemplateType.RHAPSODY) {

        }

        // TODO: Добавить логику сборки маппингов
        System.out.println("Mappings:");
        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            System.out.println("Key: " + entry.getKey() + "   Value: " + entry.getValue());
            DocumentUtils.replacePlaceholders(document, entry.getKey(), entry.getValue());
        }

        return document;
    }
}
