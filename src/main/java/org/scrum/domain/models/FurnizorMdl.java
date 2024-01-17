package org.scrum.domain.models;

//JPA
import jakarta.persistence.*;
import lombok.Data;



@Data
@Entity
@Table(name = "furnizori", uniqueConstraints = {@UniqueConstraint(columnNames = {"name"})})
//public class FurnizorMdl implements NamedEntity {
    public class FurnizorMdl  {

    @Id @GeneratedValue(strategy = GenerationType.AUTO) private Long id;
    @Column(unique = true) private String name ;// ensure uniqueness at the DB level
    private String cui;
    private String address;
    private String city;
    private String email;
    public void populate(String name,String cui, String address,String city, String email) {
        this.setName(name);
        this.setCui(cui);
        this.setAddress(address);
        this.setCity(city);
        this.setEmail(email);
    }

}
