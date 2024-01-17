package org.scrum.domain.models;

import lombok.Getter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Getter
public class Details {
    private final String seriesAndNumber;
    private final String uniqueReference;
    private final String issueDate;
    private final String dueDate;

    public Details(String seriesAndNumber, String uniqueReference, Date issueDate, Date dueDate) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("M/d/yyyy");
        this.seriesAndNumber = seriesAndNumber;
        this.uniqueReference = uniqueReference;
        this.issueDate = dateFormat.format(issueDate);
        this.dueDate = dateFormat.format(dueDate);
    }


}
