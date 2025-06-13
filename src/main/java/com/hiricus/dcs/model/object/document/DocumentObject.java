package com.hiricus.dcs.model.object.document;

import lombok.ToString;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.jooq.Record;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

import static com.hiricus.dcs.generated.public_.Tables.DOCUMENTS;

@ToString(exclude = {"data"})
public class DocumentObject {
    private long id;
    private String fileName;
    private String mimeType;
    private LocalDateTime uploadTime;  // Сетится на уровне базы

    private byte[] data;
    private long sizeBytes;

    public DocumentObject(MultipartFile file) throws IOException {
        this.fileName = file.getOriginalFilename();
        this.mimeType = file.getContentType();
        this.sizeBytes = file.getSize();
        this.data = file.getBytes();
    }
    public DocumentObject(String fileName, String mimeType, byte[] data) {
        this.fileName = fileName;
        this.mimeType = mimeType;
        this.data = data;
        this.sizeBytes = data.length;
    }
    public DocumentObject(long id, String fileName, String mimeType, byte[] data) {
        this(fileName, mimeType, data);
        this.id = id;
    }
    public DocumentObject() {}

    public DocumentObject(Record record) {
        this.id = record.get(DOCUMENTS.ID);
        this.fileName = record.get(DOCUMENTS.FILENAME);
        this.mimeType = record.get(DOCUMENTS.MIME_TYPE);
        this.data = record.get(DOCUMENTS.FILE_DATA);
        this.uploadTime = record.get(DOCUMENTS.UPLOAD_TIME);
        this.sizeBytes = record.get(DOCUMENTS.FILE_SIZE_BYTES);
    }

    // getters
    public long getId() {
        return id;
    }
    public String getFileName() {
        return fileName;
    }
    public String getMimeType() {
        return mimeType;
    }
    public LocalDateTime getUploadTime() {
        return uploadTime;
    }
    public byte[] getData() {
        return data;
    }
    public long getSizeBytes() {
        return sizeBytes;
    }

    // setters
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    public void setData(byte[] data) {
        this.data = data;
        this.sizeBytes = data.length;
    }
}