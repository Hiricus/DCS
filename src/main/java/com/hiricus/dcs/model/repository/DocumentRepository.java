package com.hiricus.dcs.model.repository;

import com.hiricus.dcs.model.object.document.DocumentObject;
import org.jooq.DSLContext;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.hiricus.dcs.generated.public_.Tables.DOCUMENTS;

@Repository
public class DocumentRepository {
    private final DSLContext jooq;

    public DocumentRepository(DSLContext jooq) {
        this.jooq = jooq;
    }

    // CRUD methods
    public List<DocumentObject> findAll() {
        return jooq.select(DOCUMENTS.asterisk())
                .from(DOCUMENTS)
                .fetch(DocumentObject::new);
    }

    public Optional<DocumentObject> findDocumentById(long id) {
        return jooq.select(DOCUMENTS.asterisk())
                .from(DOCUMENTS)
                .where(DOCUMENTS.ID.eq(id))
                .fetchOptional(DocumentObject::new);
    }

    public Optional<Long> createDocument(DocumentObject document) {
        return jooq.insertInto(DOCUMENTS)
                .set(DOCUMENTS.FILENAME, document.getFileName())
                .set(DOCUMENTS.MIME_TYPE, document.getMimeType())
                .set(DOCUMENTS.UPLOAD_TIME, document.getUploadTime())
                .set(DOCUMENTS.FILE_DATA, document.getData())
                .set(DOCUMENTS.FILE_SIZE_BYTES, document.getSizeBytes())
                .returningResult(DOCUMENTS.ID)
                .fetchOptional(DOCUMENTS.ID);
    }

    public int updateDocument(DocumentObject document) {
        return jooq.update(DOCUMENTS)
                .set(DOCUMENTS.FILENAME, document.getFileName())
                .set(DOCUMENTS.MIME_TYPE, document.getMimeType())
                .set(DOCUMENTS.UPLOAD_TIME, document.getUploadTime())
                .set(DOCUMENTS.FILE_DATA, document.getData())
                .set(DOCUMENTS.FILE_SIZE_BYTES, document.getSizeBytes())
                .where(DOCUMENTS.ID.eq(document.getId()))
                .execute();
    }

    public int deleteDocumentById(long id) {
        return jooq.delete(DOCUMENTS)
                .where(DOCUMENTS.ID.eq(id))
                .execute();
    }

    public boolean isDocumentsExistsById(long id) {
        return findDocumentById(id).isPresent();
    }
}
