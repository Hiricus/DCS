package com.hiricus.dcs.model.object.document.template;

import com.hiricus.dcs.util.documents.MappingParser;
import lombok.ToString;
import org.jooq.Record;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.hiricus.dcs.generated.public_.Tables.DOCUMENT_TEMPLATE;

@ToString
public class DocumentTemplateObject {
    private int id;
    private TemplateType templateType;
    private byte[] templateData;
    private List<String> mappings;

    // constructors
    public DocumentTemplateObject(TemplateType templateType, byte[] templateData) {
        this.templateType = templateType;
        this.templateData = templateData;
    }
    public DocumentTemplateObject(int id, TemplateType templateType, byte[] templateData) {
        this(templateType, templateData);
        this.id = id;
    }
    public DocumentTemplateObject() {}

    public DocumentTemplateObject(Record record) {
        this.id = record.get(DOCUMENT_TEMPLATE.ID);
        this.templateType = TemplateType.valueOf(record.get(DOCUMENT_TEMPLATE.TEMPLATE_TYPE));
        this.templateData = record.get(DOCUMENT_TEMPLATE.TEMPLATE_DATA);

        String mappingString = record.get(DOCUMENT_TEMPLATE.MAPPINGS);
        this.mappings = Arrays.stream(mappingString.split(";"))
                .map(s -> "{" + s + "}")
                .toList();
    }

    // getters
    public int getId() {
        return id;
    }
    public TemplateType getTemplateType() {
        return templateType;
    }
    public byte[] getTemplateData() {
        return templateData;
    }
    public List<String> getMappings() {
        return mappings;
    }

    // setters
    public void setTemplateType(TemplateType templateType) {
        this.templateType = templateType;
    }
    public void setTemplateText(byte[] templateData) {
        this.templateData = templateData;
    }
    public void setMappings(List<String> mappings) {
        this.mappings = mappings;
    }
}
