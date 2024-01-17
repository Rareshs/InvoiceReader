package org.scrum.domain;


import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.scrum.domain.services.InvGenerationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.io.File;
import java.util.Random;

import org.json.JSONObject;

import static org.junit.Assert.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertTrue;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(locations = "classpath:application-test.properties")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RestAPITests {
    @Autowired
    private InvGenerationService invGenerationService;
    @Autowired
    private MockMvc mockMvc;
    static String testInvoicesDir = "src/test/resources/test_invoices/";

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
    public void testProcessInvoiceEndpoint() throws Exception {
        String testInvoicesDir = "src/test/resources/test_invoices/";
        File[] testInvoices = new File(testInvoicesDir).listFiles((dir, name) -> name.endsWith(".pdf"));

        assert testInvoices != null;
        for (File invoice : testInvoices) {
            // Perform a POST request to the /api/invoices/process endpoint
            MvcResult result = mockMvc.perform(MockMvcRequestBuilders.post("/api/invoices/process")
                            .content(invoice.getAbsolutePath())
                            .contentType(MediaType.TEXT_PLAIN_VALUE)) // Use appropriate content type
                    .andReturn();

            // Validate the response status
            int status = result.getResponse().getStatus();
            if (status == 500) {
                // handle the exception if needed
                Exception resolvedException = result.getResolvedException();
                assertNotNull("Exception should not be null for a 500 status", resolvedException);
                resolvedException.printStackTrace();
            } else {
                // Validate the content of the response
                String content = result.getResponse().getContentAsString();
                assertNotNull("Response content should not be null", content);

                // Check for the presence of keys in the JSON structure
                JSONObject jsonObject = new JSONObject(content);

                assertTrue(jsonObject.has("factura"), "JSON must contain 'factura' key");
                assertTrue(jsonObject.has("furnizor"), "JSON must contain 'furnizor' key");
                assertTrue(jsonObject.has("client"), "JSON must contain 'client' key");

                // validate the structure under each key....
                JSONObject factura = jsonObject.getJSONObject("factura");
                assertTrue(factura.has("invoiceNr"), "factura must contain 'invoiceNr' key");
                assertTrue(factura.has("refNr"), "factura must contain 'refNr' key");
                assertTrue(factura.has("invoiceDate"), "factura must contain 'invoiceDate' key");
                assertTrue(factura.has("dueDate"), "factura must contain 'dueDate' key");

                JSONObject furnizor = jsonObject.getJSONObject("furnizor");
                assertTrue(furnizor.has("name"), "furnizor must contain 'name' key");
                assertTrue(furnizor.has("cui"), "furnizor must contain 'cui' key");
                assertTrue(furnizor.has("address"), "furnizor must contain 'address' key");
                assertTrue(furnizor.has("city"), "furnizor must contain 'city' key");
                assertTrue(furnizor.has("email"), "furnizor must contain 'email' key");

                JSONObject client = jsonObject.getJSONObject("client");
                assertTrue(client.has("name"), "client must contain 'name' key");
                assertTrue(client.has("cui"), "client must contain 'cui' key");
                assertTrue(client.has("address"), "client must contain 'address' key");
                assertTrue(client.has("city"), "client must contain 'city' key");
                assertTrue(client.has("email"), "client must contain 'email' key");
            }
        }
    }

    @AfterAll
    public static void cleanup() {
        // delete files in the testInvoicesDir directory
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