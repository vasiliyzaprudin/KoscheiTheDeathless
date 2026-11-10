package com.mining.koscheithedeathless.excavation.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import com.mining.koscheithedeathless.excavation.dto.ReportRequest;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;

@Service
public class ReportService {

    public byte[] generatePdf(ReportRequest request) throws Exception {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 1. Создаем документ (A4)
        Document document = new Document(PageSize.A4, 50, 50, 50, 50);
        PdfWriter.getInstance(document, baos);
        document.open();

        // 2. Заголовок
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Paragraph title = new Paragraph("Паспорт крепления горной выработки", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(new Paragraph("Дата формирования: " + LocalDate.now()));
        document.add(new Paragraph(" ")); // Отступ

        // 3. Параметры выработки
        Font boldFont = new Font(Font.HELVETICA, 12, Font.BOLD);
        document.add(new Paragraph("Параметры выработки:", boldFont));
        document.add(new Paragraph("Ширина: " + request.getWidth() + " м"));
        document.add(new Paragraph("Высота стенок: " + request.getWallHeight() + " м"));
        document.add(new Paragraph(" "));

        // 4. Вставка чертежа (конвертируем SVG в PNG и вставляем)
        if (request.getSvgContent() != null && !request.getSvgContent().isEmpty()) {
            byte[] pngBytes = convertSvgToPng(request.getSvgContent());
            Image svgImage = Image.getInstance(pngBytes);

            // Масштабируем картинку под ширину страницы (примерно 500px)
            svgImage.scaleToFit(500, 400);
            svgImage.setAlignment(Element.ALIGN_CENTER);
            document.add(svgImage);
        }

        document.add(new Paragraph(" "));

        // 5. Описание процесса
        document.add(new Paragraph("Описание процесса возведения крепи:", boldFont));
        document.add(new Paragraph(request.getDescription(), new Font(Font.HELVETICA, 12)));

        document.close();
        return baos.toByteArray();
    }

    // Вспомогательный метод: конвертация SVG (строки) в PNG
    private byte[] convertSvgToPng(String svgContent) throws Exception {
        // Убедимся, что SVG имеет правильный namespace
        if (!svgContent.contains("xmlns=\"http://www.w3.org/2000/svg\"")) {
            svgContent = svgContent.replaceFirst("<svg", "<svg xmlns=\"http://www.w3.org/2000/svg\"");
        }

        PNGTranscoder transcoder = new PNGTranscoder();
        // Задаем размеры выходного PNG (можно подогнать под A4)
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, 800f);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, 600f);

        try (InputStream is = new ByteArrayInputStream(svgContent.getBytes("UTF-8"));
             ByteArrayOutputStream pngBaos = new ByteArrayOutputStream()) {

            TranscoderInput input = new TranscoderInput(is);
            TranscoderOutput output = new TranscoderOutput(pngBaos);
            transcoder.transcode(input, output);

            return pngBaos.toByteArray();
        }
    }
}