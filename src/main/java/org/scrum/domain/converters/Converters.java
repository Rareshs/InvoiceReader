package org.scrum.domain.converters;

import org.scrum.domain.dto.ClientDTO;
import org.scrum.domain.dto.FacturaDTO;
import org.scrum.domain.dto.FurnizorDTO;
import org.scrum.domain.models.ClientMdl;
import org.scrum.domain.models.FacturaMdl;
import org.scrum.domain.models.FurnizorMdl;
import org.springframework.stereotype.Service;

// Converters.java
@Service
public class Converters {
    public ClientDTO convertToDTO(ClientMdl clientEntity) {
        ClientDTO clientDTO = new ClientDTO();
        clientDTO.setName(clientEntity.getName());
        clientDTO.setCui(clientEntity.getCui());
        clientDTO.setAddress(clientEntity.getAddress());
        clientDTO.setCity(clientEntity.getCity());
        clientDTO.setEmail(clientEntity.getEmail());
        return clientDTO;
    }

    public FurnizorDTO convertToDTO(FurnizorMdl furnizorEntity) {
        FurnizorDTO furnizorDTO = new FurnizorDTO();
        furnizorDTO.setName(furnizorEntity.getName());
        furnizorDTO.setCui(furnizorEntity.getCui());
        furnizorDTO.setAddress(furnizorEntity.getAddress());
        furnizorDTO.setCity(furnizorEntity.getCity());
        furnizorDTO.setEmail(furnizorEntity.getEmail());
        return furnizorDTO;
    }

    public FacturaDTO convertToDTO(FacturaMdl facturaEntity) {
        FacturaDTO facturaDTO = new FacturaDTO();
        facturaDTO.setInvoiceNr(facturaEntity.getInvoiceNr());
        facturaDTO.setRefNr(facturaEntity.getRefNr());
        facturaDTO.setInvoiceDate(facturaEntity.getInvoiceDate());
        facturaDTO.setDueDate(facturaEntity.getDueDate());
        return facturaDTO;
    }
}
