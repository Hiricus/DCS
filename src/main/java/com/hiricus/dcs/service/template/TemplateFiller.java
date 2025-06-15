package com.hiricus.dcs.service.template;

import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import com.hiricus.dcs.util.documents.DocumentUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

// Содержит логику заполнения документов
@Component
public class TemplateFiller {
    private final KeywordReplacer replacer;

    @Autowired
    public TemplateFiller(KeywordReplacer replacer) {
        this.replacer = replacer;
    }

    public XWPFDocument fillTemplate(DocumentTemplateObject template, Integer userId) throws IOException {
        XWPFDocument document = DocumentUtils.loadDocx(template);
        List<String> mappings = template.getMappings();

        for (String mapping : mappings) {
            String replacement = replacer.replaceKeyword(mapping, userId);
            DocumentUtils.replaceKeywords(document, mapping, replacement);
        }

        return document;
    }
}
