package org.scrum.domain.models;

import lombok.Getter;

@Getter
public class Product {
    private final String service;
    private final String unit;
    private final String qty;
    private final String price;
    private final String value;
    public Product(String service, String unit, String qty, String price, String value) {
        this.qty = qty;
        this.service = service;
        this.unit = unit;
        this.price = price;
        this.value = value;
    }

}
