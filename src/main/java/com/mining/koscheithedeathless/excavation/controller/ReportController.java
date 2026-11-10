package com.mining.koscheithedeathless.excavation.controller;

import com.mining.koscheithedeathless.excavation.dto.ReportRequest;
import com.mining.koscheithedeathless.excavation.service.ReportService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/report")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping(value = "/pdf", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> generateReport(@RequestBody ReportRequest request) {
        try {
            byte[] pdfBytes = reportService.generatePdf(request);

            HttpHeaders headers = new HttpHeaders();
            // Указываем браузеру, что это файл для скачивания
            headers.setContentDispositionFormData("attachment", "passport_крепления.pdf");
            headers.setContentType(MediaType.APPLICATION_PDF);

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }
}