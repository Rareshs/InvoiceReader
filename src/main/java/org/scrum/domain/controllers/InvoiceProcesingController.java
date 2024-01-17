package org.scrum.domain.controllers;

import org.scrum.domain.services.InvProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceProcesingController {
    private static final Logger logger = Logger.getLogger(InvProcessingService.class.getName());
    private final InvProcessingService invProcessingService;

    @Autowired
    public InvoiceProcesingController(InvProcessingService invProcessingService) {
        this.invProcessingService = invProcessingService;
    }
//pdf upload logic w message..
    @PostMapping("/process")
    public ResponseEntity<Map<String, Object>> processInvoice(@RequestPart("pdfFile") MultipartFile pdfFile) {
        try {
            Map<String, Object> result = invProcessingService.processInvoice(pdfFile);
//            return a json of the proccessed invoice
             return ResponseEntity.ok(result);

        } catch (Exception e) {
            e.printStackTrace();

            // Return an error response with a message
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("message", "Error processing the invoice");

            return ResponseEntity.status(500).body(errorResponse);
        }
    }



}

