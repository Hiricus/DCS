package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import com.hiricus.dcs.model.object.document.template.TemplateType;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hiricus.dcs.generated.public_.Tables.DOCUMENT_TEMPLATE;

@Repository
public class DocumentTemplateRepository {
    private final DSLContext jooq;

    public DocumentTemplateRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // CRUD methods
    public List<DocumentTemplateObject> findAll() {
        return jooq.select(DOCUMENT_TEMPLATE.asterisk())
                .from(DOCUMENT_TEMPLATE)
                .fetch(DocumentTemplateObject::new);
    }

    public Optional<DocumentTemplateObject> findTemplateByType(TemplateType type) {
        return jooq.select(DOCUMENT_TEMPLATE.asterisk())
                .from(DOCUMENT_TEMPLATE)
                .where(DOCUMENT_TEMPLATE.TEMPLATE_TYPE.eq(type.name()))
                .fetchOptional(DocumentTemplateObject::new);
    }

    public Optional<Integer> createTemplate(DocumentTemplateObject template) {
        return jooq.insertInto(DOCUMENT_TEMPLATE)
                .set(DOCUMENT_TEMPLATE.TEMPLATE_TYPE, template.getTemplateType().name())
                .set(DOCUMENT_TEMPLATE.TEMPLATE_TEXT, template.getTemplateText())
                .set(DOCUMENT_TEMPLATE.MAPPINGS, template.getMappings().toString())
                .returningResult(DOCUMENT_TEMPLATE.ID)
                .fetchOptional(DOCUMENT_TEMPLATE.ID);
    }

    public int updateTemplate(DocumentTemplateObject template) {
        return jooq.update(DOCUMENT_TEMPLATE)
                .set(DOCUMENT_TEMPLATE.TEMPLATE_TYPE, template.getTemplateType().name())
                .set(DOCUMENT_TEMPLATE.TEMPLATE_TEXT, template.getTemplateText())
                .set(DOCUMENT_TEMPLATE.MAPPINGS, template.getMappings().toString())
                .where(DOCUMENT_TEMPLATE.ID.eq(template.getId()))
                .execute();
    }

    public int deleteTemplateById(int id) {
        return jooq.delete(DOCUMENT_TEMPLATE)
                .where(DOCUMENT_TEMPLATE.ID.eq(id))
                .execute();
    }

    public boolean isTemplateExistsByType(TemplateType type) {
        return findTemplateByType(type).isPresent();
    }
}
