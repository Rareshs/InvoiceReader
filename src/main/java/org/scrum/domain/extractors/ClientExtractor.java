package org.scrum.domain.extractors;

import lombok.Getter;

import java.util.logging.Logger;

@Getter
public class ClientExtractor {

    private final String name;
    private final String cui;
    private final String address;
    private final String city;
    private final String email;
    
    private static final Logger logger = Logger.getLogger(ClientExtractor.class.getName());

    public ClientExtractor(String invoiceText) {
        // Check if invoiceText is valid (contains "Cumparator:")
        if (isValidInvoiceText(invoiceText)) {
            // Perform extraction logic and set properties
            int startIndex = invoiceText.indexOf("Cumparator:");
            this.name = extractClientName(invoiceText, startIndex);
            this.cui = extractClientCUI(invoiceText, startIndex);
            this.address = extractClientAddress(invoiceText, startIndex);
            this.city = extractClientCity(invoiceText, startIndex);
            this.email = extractClientEmail(invoiceText, startIndex);
        } else {
            // Handle the case where invoiceText is not valid
            logger.warning("Invalid invoice... could not find client information.");
            this.name = null;
            this.cui = null;
            this.address = null;
            this.city = null;
            this.email = null;
        }
    }

    private boolean isValidInvoiceText(String invoiceText) {
        return invoiceText != null && invoiceText.contains("Cumparator:");
    }

    private String extractClientName(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 1);
    }

    private String extractClientCUI(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 2);
    }

    private String extractClientAddress(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 3);
    }

    private String extractClientCity(String invoiceText, int startIndex) {
        return extractLineAfterKeyword(invoiceText, startIndex, 4);
    }

    private String extractClientEmail(String invoiceText, int startIndex) {
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
