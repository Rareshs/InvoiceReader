package org.scrum.domain.extractors;

import lombok.Getter;

import java.util.logging.Logger;

@Getter
public class FurnizorExtractor {

    private final String name;
    private final String cui;
    private final String address;
    private final String city;
    private final String email;

    private static final Logger logger = Logger.getLogger(DetaliiExtractor.class.getName());

    public FurnizorExtractor(String invoiceText) {
        // Check if invoiceText is valid (contains "Furnizor:")
        if (isValidInvoiceText(invoiceText)) {
            // Perform extraction logic and set properties
            int startIndex = invoiceText.indexOf("Furnizor:");
            this.name = extractName(invoiceText, startIndex);
            this.cui = extractCui(invoiceText, startIndex);
            this.address = extractAddress(invoiceText, startIndex);
            this.city = extractCity(invoiceText, startIndex);
            this.email = extractEmail(invoiceText, startIndex);
        } else {
            // Handle the case where invoiceText is not valid
            logger.warning("Invalid invoice... could not find furnizor information.");
            this.name = null;
            this.cui = null;
            this.address = null;
            this.city = null;
            this.email = null;
        }
    }

    private boolean isValidInvoiceText(String invoiceText) {
        return invoiceText != null && invoiceText.contains("Furnizor:");
    }

    private String extractName(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 1);
    }

    private String extractCui(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 2);
    }

    private String extractAddress(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 3);
    }

    private String extractCity(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 4);
    }

    private String extractEmail(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 5);
    }

    private String extractLineAfterKeyword(String invoiceText, int startIndex, int linesAfter) {
        int currentIndex = startIndex;
        for (int i = 0; i < linesAfter; i++) {
            currentIndex = invoiceText.indexOf('\n', currentIndex) + 1;
        }

        int endIndex = invoiceText.indexOf('\n', currentIndex);
        if (currentIndex != -1 && endIndex != -1) {
            return invoiceText.substring(currentIndex, endIndex).trim();
        }
        return null;
    }


}
