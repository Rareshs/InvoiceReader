package org.scrum.domain.models;

//JPA
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "facturi")
public class FacturaMdl {

    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)private Long id;
    private String fileName;
    private String invoiceNr;
    private String refNr;
    private String invoiceDate;
    private String dueDate;
    @ManyToOne@JoinColumn(name = "client_id")private ClientMdl client;
    @ManyToOne@JoinColumn(name = "furnizor_id")private FurnizorMdl furnizor;

    public void populate(String fileName, String invoiceNr, String refNr, String invoiceDate, String dueDate) {
        setFileName(fileName);
        setInvoiceNr(invoiceNr);
        setRefNr(refNr);
        setInvoiceDate(invoiceDate);
        setDueDate(dueDate);
    }

    public void setClient(ClientMdl client) {
        this.client = client;
    }
    public void setFurnizor(FurnizorMdl furnizor) {
        this.furnizor = furnizor;
    }

}
