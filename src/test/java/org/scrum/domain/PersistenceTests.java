package org.scrum.domain;

//java lang
import java.io.File;
import java.util.Optional;
import java.util.Random;
//springboot


import org.junit.jupiter.api.*;
import org.scrum.domain.services.InvGenerationService;
import org.scrum.domain.services.InvProcessingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
//jupiter
import static org.junit.jupiter.api.Assertions.*;

import org.testcontainers.junit.jupiter.Testcontainers;
//models
import org.scrum.domain.models.*;
//repos
import org.scrum.domain.repositories.*;
//services


@Testcontainers
@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class PersistenceTests {
    static String testInvoicesDir = "src/test/resources/test_invoices/";
    @Autowired
    private InvGenerationService invGenerationService;
    @Autowired
    private InvProcessingService invProcessingService;
    @Autowired
    private FacturaRepo facturaRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private FurnizorRepo furnizorRepo;

    @Test@Order(1)
    public void testInvoiceGeneration() {

        // Generate a random number of invoices (between 10 and 50)
        Random random = new Random();
        int numberOfInvoices = random.nextInt(41) + 10;
        for (int i = 0; i < numberOfInvoices; i++) {

           assertDoesNotThrow(() -> invGenerationService.generateRandomInvoice(testInvoicesDir));
        }
        // test the contents of the testInvoices folder for unwanted file types
        File[] testInvoices = new File(testInvoicesDir).listFiles((dir, name) -> name.endsWith(".pdf"));
        assert testInvoices != null;
        for (File invoice : testInvoices) {
            System.out.println(invoice);
            assertTrue(invoice.isFile(), "Not a file: " + invoice.getName());
            assertTrue(invoice.getName().endsWith(".pdf"), "Not a PDF file: " + invoice.getName());
        }
    }
    @Test@Order(2)
    public void testInvoiceProccesing() {
        File[] testInvoices = new File(testInvoicesDir).listFiles((dir, name) -> name.endsWith(".pdf"));

        assert testInvoices != null;
        for (File invoice : testInvoices) {

               assertDoesNotThrow(() -> invProcessingService.processInvoice(invoice.getAbsolutePath()));

           }

    }
    @Test@Order(3)
    public void testDbEntries() {
        // Get the number of invoices in the testfolder
        File[] testInvoices = new File(testInvoicesDir).listFiles((dir, name) -> name.endsWith(".pdf"));
        assert testInvoices != null;
        int noOfInvoices = testInvoices.length;
        // Print the no of invoices in the test folder
        System.out.println("Number of Facturi de test: " + noOfInvoices);
        // Get the count of Factura entities in the repository
        long facturiCount = facturaRepo.count();
        // Print the count of Factura entities recorded in the DB
        System.out.println("No of invoice entries in the DB: " + facturiCount);
        // Assert that the count matches the expected count
        assertEquals(noOfInvoices, facturiCount);
    }
    @Test@Order(4)
    public void testClientRepository() {
        // Save a new client
        ClientMdl newClient = new ClientMdl();
        newClient.populate("New Client", "123456", "Street 123", "City", "newclient@example.com");
        clientRepo.save(newClient);

        // Find client by name
        Optional<ClientMdl> foundClientOptional = clientRepo.findByName("New Client");
        assertTrue(foundClientOptional.isPresent(), "Client not found");
        ClientMdl foundClient = foundClientOptional.get();
        assertEquals("123456", foundClient.getCui());

        // Update client's information
        foundClient.setCity("Updated City");
        clientRepo.save(foundClient);

        // Find and assert the updated city
        Optional<ClientMdl> updatedClientOptional = clientRepo.findByName("New Client");
        assertTrue(updatedClientOptional.isPresent(), "Updated client not found");
        assertEquals("Updated City", updatedClientOptional.get().getCity());

        // Delete the client
        clientRepo.delete(foundClient);

        // Check that the client is deleted
        Optional<ClientMdl> deletedClientOptional = clientRepo.findByName("New Client");
        assertFalse(deletedClientOptional.isPresent(), "Client should be deleted");
    }
    @Test@Order(5)
    public void testFurnizorRepository() {
        // Save a new furnizor
        FurnizorMdl newFurnizor = new FurnizorMdl();
        newFurnizor.populate("New Furnizor", "123456", "Street 123", "City", "newfurnizor@example.com");
        furnizorRepo.save(newFurnizor);

        // Find furnizor by name
        Optional<FurnizorMdl> foundFurnizorOptional = furnizorRepo.findByName("New Furnizor");
        assertTrue(foundFurnizorOptional.isPresent(), "Furnizor not found");
        FurnizorMdl foundFurnizor = foundFurnizorOptional.get();
        assertEquals("123456", foundFurnizor.getCui());

        // Update furnizor's information
        foundFurnizor.setCity("Updated City");
        furnizorRepo.save(foundFurnizor);

        // Find and assert the updated city
        Optional<FurnizorMdl> updatedFurnizorOptional = furnizorRepo.findByName("New Furnizor");
        assertTrue(updatedFurnizorOptional.isPresent(), "Updated furnizor not found");
        assertEquals("Updated City", updatedFurnizorOptional.get().getCity());

        // Delete the furnizor
        furnizorRepo.delete(foundFurnizor);

        // Check that the furnizor is deleted
        Optional<FurnizorMdl> deletedFurnizorOptional = furnizorRepo.findByName("New Furnizor");
        assertFalse(deletedFurnizorOptional.isPresent(), "Furnizor should be deleted");
    }
    @Test@Order(6)
    public void testFacturaRepository() {
        // Save a new factura with associated client and furnizor
        FacturaMdl newFactura = new FacturaMdl();
        newFactura.populate("Invoice123.pdf", "123456", "Ref123", "2023-01-01", "2023-02-01");

        ClientMdl client = new ClientMdl();
        client.populate("Client123", "654321", "Street 321", "City", "client@example.com");
        clientRepo.save(client);
        newFactura.setClient(client);

        FurnizorMdl furnizor = new FurnizorMdl();
        furnizor.populate("Furnizor123", "987654", "Street 987", "City", "furnizor@example.com");
        furnizorRepo.save(furnizor);
        newFactura.setFurnizor(furnizor);

        facturaRepo.save(newFactura);

        // Find factura by invoice number
        Optional<FacturaMdl> foundFacturaOptional = facturaRepo.findByInvoiceNr("123456");
        assertTrue(foundFacturaOptional.isPresent(), "Factura not found");
        FacturaMdl foundFactura = foundFacturaOptional.get();
        assertEquals("Ref123", foundFactura.getRefNr());

        // Update factura's information
        foundFactura.setDueDate("2023-03-01");
        facturaRepo.save(foundFactura);

        // Find and assert the updated due date
        Optional<FacturaMdl> updatedFacturaOptional = facturaRepo.findByInvoiceNr("123456");
        assertTrue(updatedFacturaOptional.isPresent(), "Updated factura not found");
        assertEquals("2023-03-01", updatedFacturaOptional.get().getDueDate());

        // Delete the factura
        facturaRepo.delete(foundFactura);

        // Check that the factura is deleted
        Optional<FacturaMdl> deletedFacturaOptional = facturaRepo.findByInvoiceNr("123456");
        assertFalse(deletedFacturaOptional.isPresent(), "Factura should be deleted");
    }
    @AfterAll
    public static void cleanup() {
        // Delete files in the testInvoicesDir directory
        File testInvoicesDirectory = new File(testInvoicesDir);

        if (testInvoicesDirectory.exists() && testInvoicesDirectory.isDirectory()) {
            File[] filesToDelete = testInvoicesDirectory.listFiles();

            if (filesToDelete != null) {
                for (File file : filesToDelete) {
                    if (!file.delete()) {
                        System.err.println("Failed to delete file: " + file.getAbsolutePath());
                    }
                }
            }
        }
    }

}
