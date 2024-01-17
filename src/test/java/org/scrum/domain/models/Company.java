package org.scrum.domain.models;

import lombok.Getter;

@Getter
public class Company {
    private final String name;
    private final String cui;
    private final String address;
    private final String city;
    private final String email;

    public Company(String name, String cui, String address, String city, String email) {
        this.name = name;
        this.cui = cui;
        this.address = address;
        this.city = city;
        this.email = email;
    }

}
