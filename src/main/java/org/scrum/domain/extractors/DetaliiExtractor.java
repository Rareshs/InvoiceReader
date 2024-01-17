package org.scrum.domain.extractors;
import lombok.Getter;

@Getter
public class DetaliiExtractor {
    private final String invoiceNr;
    private final String refNr;
    private final String invoiceDate;
    private final String dueDate;
    public DetaliiExtractor(String invoiceText) {
        this.invoiceNr = extractValue(invoiceText, "Serie si numar:");
        this.refNr = extractValue(invoiceText, "Referinta unica:");
        this.invoiceDate = extractValue(invoiceText, "Data emiterii:");
        this.dueDate = extractValue(invoiceText, "Data scadenta:");
    }

    private static String extractValue(String invoiceText, String keyword) {
        int keywordIndex = invoiceText.indexOf(keyword);
        if (keywordIndex != -1) {
            int valueStartIndex = keywordIndex + keyword.length() + 1; // +1 to skip the space after the keyword
            int valueEndIndex = invoiceText.indexOf('\n', valueStartIndex);
            if (valueEndIndex != -1) {
                return invoiceText.substring(valueStartIndex, valueEndIndex).trim();
            }
        }
        return null; // iiif no text is found, return null :D
    }






}


