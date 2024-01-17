package org.scrum.domain.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FacturaDTO {

    private String invoiceNr;
    private String refNr;
    private String invoiceDate;
    private String dueDate;

    // Getters and setters
}
