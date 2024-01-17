package org.scrum.domain.services;

import jakarta.transaction.Transactional;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

//data extractors
import org.scrum.domain.converters.Converters;
import org.scrum.domain.dto.ClientDTO;
import org.scrum.domain.dto.FacturaDTO;
import org.scrum.domain.dto.FurnizorDTO;
import org.scrum.domain.extractors.ClientExtractor;
import org.scrum.domain.extractors.DetaliiExtractor;
import org.scrum.domain.extractors.FurnizorExtractor;

//db models
import org.scrum.domain.models.ClientMdl;
import org.scrum.domain.models.FurnizorMdl;
import org.scrum.domain.models.FacturaMdl;

//repos
import org.scrum.domain.repositories.ClientRepo;
import org.scrum.domain.repositories.FurnizorRepo;
import org.scrum.domain.repositories.FacturaRepo;

//springboot stuff
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.multipart.MultipartFile;

//java lang
import java.io.File;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;




@Service
public class InvProcessingService {
    @Autowired
    private FacturaRepo facturaRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private FurnizorRepo furnizorRepo;
    @Autowired
    private Converters converters;
    private static final Logger logger = Logger.getLogger(InvProcessingService.class.getName());

    @Transactional
    public Map<String, Object> processInvoice(MultipartFile pdfFile) {
        Map<String, Object> result = new HashMap<>();
        logger.info("Processing invoice");

        // Get the original filename of the uploaded file
        String fileName = pdfFile.getOriginalFilename();
        logger.info("Original filename: " + fileName);

        // _______________read the invoice _________________________
        String invoiceText = readInvoice(pdfFile);

        if (invoiceText == null) {
            // Log or handle the case where reading the invoice text fails
            return result;
        }

        //________________extract relevant info into sections___________________
        ClientExtractor clientExtractor = new ClientExtractor(invoiceText);
        FurnizorExtractor furnizorExtractor = new FurnizorExtractor(invoiceText);
        DetaliiExtractor detaliiExtractor = new DetaliiExtractor(invoiceText);

        // _____________use the table models to create new DB objects _________________
        ClientMdl newClient = new ClientMdl();//client
        FurnizorMdl newFurnizor = new FurnizorMdl();//furnizor
        FacturaMdl newFactura = new FacturaMdl();//invoiceDetails

        // _____________populate the objects w the extracted data_______________________
        //client
        newClient.populate(clientExtractor.getName(), clientExtractor.getCui(), clientExtractor.getAddress(), clientExtractor.getCity(), clientExtractor.getEmail());
        //furnizor
        newFurnizor.populate(furnizorExtractor.getName(), furnizorExtractor.getCui(), furnizorExtractor.getAddress(), furnizorExtractor.getCity(), furnizorExtractor.getEmail());
        //factura
        newFactura.populate(fileName, detaliiExtractor.getInvoiceNr(), detaliiExtractor.getRefNr(),detaliiExtractor.getInvoiceDate(), detaliiExtractor.getDueDate());


        // ____________________convert entities to DTOs and return them___________________
        if (converters != null) {
            ClientDTO clientDTO = converters.convertToDTO(newClient);
            FurnizorDTO furnizorDTO = converters.convertToDTO(newFurnizor);
            FacturaDTO facturaDTO = converters.convertToDTO(newFactura);
            result.put("client", clientDTO);
            result.put("furnizor", furnizorDTO);
            result.put("factura", facturaDTO);
        } else {
            logger.warning("Converters not initialized. Unable to convert entities to DTOs.");
        }


        //__________________________save the entities in the db__________________________
        //client entity
        try {
            // look for a client with the same name(we can add more search querries here) in the db
            Optional<ClientMdl> existingClientOptional = clientRepo.findByName(newClient.getName());
            if (existingClientOptional.isPresent()) {// Existing client found, update, link him to the invoice and save
                ClientMdl existingClient = existingClientOptional.get();
                existingClient.setCui(newClient.getCui());
                existingClient.setAddress(newClient.getAddress());
                existingClient.setCity(newClient.getCity());
                existingClient.setEmail(newClient.getEmail());
                newFactura.setClient(existingClient);
                clientRepo.save(existingClient);
            } else {// No existing client found, link him to the invoice and save
                newFactura.setClient(newClient);
                clientRepo.save(newClient);
            }
        } catch (DataIntegrityViolationException e) {
            logger.warning("Error saving client/furnizor: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Unexpected error during invoice processing: " + e.getMessage());
        }
        //furnizor entity
        try {
            // look for a furnizor with the same name in the db
            Optional<FurnizorMdl> existingFurnizorOptional = furnizorRepo.findByName(newFurnizor.getName());
            if (existingFurnizorOptional.isPresent()) {// Existing furnizor found, update, link him to the invoice and save
                FurnizorMdl existingFurnizor = existingFurnizorOptional.get();
                existingFurnizor.setCui(newClient.getCui());
                existingFurnizor.setAddress(newClient.getAddress());
                existingFurnizor.setCity(newClient.getCity());
                existingFurnizor.setEmail(newClient.getEmail());
                newFactura.setFurnizor(existingFurnizor);
                furnizorRepo.save(existingFurnizor);
            } else {// No existing furnizor found link him to the invoice and save
                newFactura.setFurnizor(newFurnizor);
                furnizorRepo.save(newFurnizor);
            }
        } catch (DataIntegrityViolationException e) {
            logger.warning("Error saving client/furnizor: " + e.getMessage());
        } catch (Exception e) {
            logger.severe("Unexpected error during invoice processing: " + e.getMessage());
        }
        //factura entity
        facturaRepo.save(newFactura);//save the invoice with the client and the furnizor(and so on) linked

        return result;
    }




    public String readInvoice(MultipartFile pdfFile) {
        try (PDDocument document = PDDocument.load(pdfFile.getInputStream())) {
            PDFTextStripper pdfTextStripper = new PDFTextStripper();
            return pdfTextStripper.getText(document);
        } catch (IOException e) {
            // Log the exceptions if any
            logger.warning("Error reading the invoice text: " + e.getMessage());
            return null;
        }
    }
}
