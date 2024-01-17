package org.scrum.domain.models;

import lombok.Getter;

@Getter
public class Bank {
    private final String bankName;
    private final String accountNumber;
    private final String bsbNumber;

    public Bank(String bankName, String accountNumber, String bsbNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
        this.bsbNumber = bsbNumber;
    }
}
