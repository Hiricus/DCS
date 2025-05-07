package com.hiricus.dcs.model.object.document.template;

import lombok.ToString;
import org.jooq.Record;

import java.util.Map;

import static com.hiricus.dcs.generated.public_.Tables.DOCUMENT_TEMPLATE;

@ToString
public class DocumentTemplateObject {
    private int id;
    private TemplateType templateType;
    private String templateText;
    private Map<String, String> mappings;

    // constructors
    public DocumentTemplateObject(TemplateType templateType, String templateText) {
        this.templateType = templateType;
        this.templateText = templateText;
    }
    public DocumentTemplateObject(int id, TemplateType templateType, String templateText) {
        this(templateType, templateText);
        this.id = id;
    }
    public DocumentTemplateObject() {}

    // TODO сделать парсинг маппингов
    public DocumentTemplateObject(Record record) {
        this.id = record.get(DOCUMENT_TEMPLATE.ID);
        this.templateType = TemplateType.valueOf(record.get(DOCUMENT_TEMPLATE.TEMPLATE_TYPE));
        this.templateText = record.get(DOCUMENT_TEMPLATE.TEMPLATE_TEXT);
    }

    // getters
    public int getId() {
        return id;
    }
    public TemplateType getTemplateType() {
        return templateType;
    }
    public String getTemplateText() {
        return templateText;
    }
    public Map<String, String> getMappings() {
        return mappings;
    }

    // setters
    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }
    public void setTemplateText(String templateText) {
        this.templateText = templateText;
    }
    public void setMappings(Map<String, String> mappings) {
        this.mappings = mappings;
    }
}
