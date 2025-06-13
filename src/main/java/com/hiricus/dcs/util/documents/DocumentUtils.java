package com.hiricus.dcs.util.documents;

import com.hiricus.dcs.model.object.document.DocumentObject;
import com.hiricus.dcs.model.object.document.template.DocumentTemplateObject;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// Класс-библиотека для работы с документами
public class DocumentUtils {
    public static XWPFDocument loadDocx(DocumentTemplateObject template) throws IOException {
//        if (!template.getMimeType().equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
//            throw new IllegalArgumentException("Неверный MIME-тип. Ожидался .docx файл.");
//        }

        ByteArrayInputStream inputStream = new ByteArrayInputStream(template.getTemplateData());
        return new XWPFDocument(inputStream);
    }

    public static void replacePlaceholders(XWPFDocument document, String placeholder, String replacement) {
        for (XWPFParagraph paragraph : document.getParagraphs()) {
            replaceInParagraph(paragraph, placeholder, replacement);
        }
    }

    private static void replaceInParagraph(XWPFParagraph paragraph, String placeholder, String replacement) {
        List<XWPFRun> runs = paragraph.getRuns();
        if (runs == null || runs.isEmpty()) return;

        StringBuilder fullText = new StringBuilder();
        for (XWPFRun run : runs) {
            fullText.append(run.getText(0));
        }

        String combinedText = fullText.toString();
        if (!combinedText.contains(placeholder)) return;

        // Удаляем старые runs
        for (int i = runs.size() - 1; i >= 0; i--) {
            paragraph.removeRun(i);
        }

        String replacedText = combinedText.replaceAll(Pattern.quote(placeholder), Matcher.quoteReplacement(replacement));

        XWPFRun newRun = paragraph.createRun();
        newRun.setText(replacedText);
    }
}
