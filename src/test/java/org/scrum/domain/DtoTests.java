package org.scrum.domain;

import org.junit.jupiter.api.Test;
import org.scrum.domain.converters.Converters;
import org.scrum.domain.dto.ClientDTO;
import org.scrum.domain.dto.FacturaDTO;
import org.scrum.domain.dto.FurnizorDTO;
import org.scrum.domain.models.ClientMdl;
import org.scrum.domain.models.FacturaMdl;
import org.scrum.domain.models.FurnizorMdl;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
@SpringBootTest
public class DtoTests {

    @Test
    public void tesClientDTO() {
        // Create a sample ClientMdl
        ClientMdl clientEntity = new ClientMdl();
        clientEntity.setName("John Doe");
        clientEntity.setCui("123456");
        clientEntity.setAddress("123 Main St");
        clientEntity.setCity("City");
        clientEntity.setEmail("john.doe@example.com");

        // Convert to ClientDTO
        Converters converters = new Converters();
        ClientDTO clientDTO = converters.convertToDTO(clientEntity);

        // Verify the conversion
        assertEquals("John Doe", clientDTO.getName());
        assertEquals("123456", clientDTO.getCui());
        assertEquals("123 Main St", clientDTO.getAddress());
        assertEquals("City", clientDTO.getCity());
        assertEquals("john.doe@example.com", clientDTO.getEmail());
    }

    @Test
    public void testurnizorDTO() {
        // Create a sample FurnizorMdl
        FurnizorMdl furnizorEntity = new FurnizorMdl();
        furnizorEntity.setName("ABC Company");
        furnizorEntity.setCui("654321");
        furnizorEntity.setAddress("456 Oak St");
        furnizorEntity.setCity("Town");
        furnizorEntity.setEmail("abc@example.com");

        // Convert to FurnizorDTO
        Converters converters = new Converters();
        FurnizorDTO furnizorDTO = converters.convertToDTO(furnizorEntity);

        // Verify the conversion
        assertEquals("ABC Company", furnizorDTO.getName());
        assertEquals("654321", furnizorDTO.getCui());
        assertEquals("456 Oak St", furnizorDTO.getAddress());
        assertEquals("Town", furnizorDTO.getCity());
        assertEquals("abc@example.com", furnizorDTO.getEmail());
    }

    @Test
    public void testFacturaDTO() {
        // Create a sample FacturaMdl
        FacturaMdl facturaEntity = new FacturaMdl();
        facturaEntity.setInvoiceNr("789012");
        facturaEntity.setRefNr("REF123");
        facturaEntity.setInvoiceDate("2023-01-01");
        facturaEntity.setDueDate("2023-02-01");

        // Convert to FacturaDTO
        Converters converters = new Converters();
        FacturaDTO facturaDTO = converters.convertToDTO(facturaEntity);

        // Verify the conversion
        assertEquals("789012", facturaDTO.getInvoiceNr());
        assertEquals("REF123", facturaDTO.getRefNr());
        assertEquals("2023-01-01", facturaDTO.getInvoiceDate());
        assertEquals("2023-02-01", facturaDTO.getDueDate());
    }

}
