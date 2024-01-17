package org.scrum.domain.services;

//java lang
import java.io.IOException;
import java.util.*;
//springboot
import org.scrum.domain.models.*;
import org.springframework.stereotype.Service;
//section models


@Service
public class InvGenerationService {
    private final List<Client> clients;
    private final List<Company> companies;
    private final List<Product> products;
    private final List<Bank> banks;

    private InvGenerationService() {// predefined data to use for our invoices...hehe boy

        clients = Arrays.asList( // Clients
                new Client("Ana Popescu", "RO123456789", "123 Strada Principala", "Bucuresti", "ana.popescu@email.com"),
                new Client("Mihai Ionescu", "RO987654321", "456 Strada Alba", "Cluj-Napoca", "mihai.ionescu@email.com"),
                new Client("Elena Udrea", "RO567890123", "789 Strada Verde", "Timisoara", "galben_de_tot@email.com"),
                new Client("Alexandra Neagu", "RO456789012", "101 Strada Mare", "Iasi", "alexandra.neagu@email.com"),
                new Client("Cristian Vasile", "RO234567890", "202 Strada Mica", "Constanta", "cristian.vasile@email.com"),
                new Client("Niccolo Machiavelli", "RO345678901", "303 Strada Noua", "Craiova", "art.of.war@email.com"),
                new Client("Lin Bin Fanta", "RO678901234", "404 Strada Maro", "Oradea", "ur.anus@email.com"),
                new Client("Adrian Gheorghe", "RO789012345", "505 Strada Frumoasa", "Arad", "adrian.gheorghe@email.com"),
                new Client("Simona Dumitru", "RO890123456", "606 Strada Colorata", "Ploiesti", "simona.dumitru@email.com"),
                new Client("Traian Basescu", "RO901234567", "707 Strada Linistita", "Brasov", "whereismynavy_69@email.com")
        );

        companies = Arrays.asList(// Companies
                new Company("Design Creativ", "RO12345678", "123 Strada Principala", "Bucuresti, RO 12345", "admin@design-creativ.com"),
                new Company("Creatii Vizuale", "RO87654321", "456 Strada Alba", "Cluj-Napoca, RO 56789", "admin@creatiiv-vizuale.com"),
                new Company("Inovatii Redundante", "RO98765432", "789 Strada Verde", "Timisoara, RO 98765", "admin@inovatii-pixel.com"),
                new Company("Minte Creativa", "RO11112222", "101 Strada Mare", "Iasi, RO 54321", "admin@mintecreativa.com"),
                new Company("Vrajitori IT", "RO33334444", "202 Strada Mica", "Constanta, RO 67890", "admin@vrajitori-it.com"),
                new Company("Solutii Smart", "RO55556666", "303 Strada Noua", "Craiova, RO 23456", "admin@solutii-smart.com"),
                new Company("Design Infinit", "RO77778888", "404 Strada Veche", "Oradea, RO 87654", "admin@design-infinit.com"),
                new Company("Creatori Elita", "RO99990000", "505 Strada Frumoasa", "Arad, RO 45678", "admin@creatori-elita.com"),
                new Company("Genii Autisti", "RO12121212", "606 Strada Colorata", "Ploiesti, RO 76543", "admin@genii-digitali.com"),
                new Company("Creatii Dinamice", "RO34343434", "707 Strada Linistita", "Brasov, RO 32109", "admin@creatiidinamice.com")
        );


        products = Arrays.asList(// Products
                new Product("Web Design", "ore", "1", "85.00", "85.00"),
                new Product("Design Grafic", "proiecte", "2", "100.00", "190.00"),
                new Product("Dezvoltare Software", "proiecte", "3", "120.00", "360.00"),
                new Product("Consultanta XXX", "ore", "1", "150.00", "150.00"),
                new Product("Servicii SEO", "proiecte", "2", "80.00", "160.00"),
                new Product("Blogging", "luni", "1", "75.00", "75.00"),
                new Product("Redactare Continut", "proiecte", "4", "40.00", "160.00"),
                new Product("Fotografie", "ore", "2", "120.00", "240.00"),
                new Product("Productie Video", "ore", "1", "200.00", "200.00"),
                new Product("Campanie Marketing", "luni", "3", "180.00", "540.00")

        );

        banks = Arrays.asList(//banks
                new Bank("Banca Nationala a Romaniei", "RO49 AAAA 1B31 0075 9384 0000", "4321 432"),
                new Bank("Banca Comerciala Romana", "RO44 AAAA 1B31 0075 9384 0001", "8765 876"),
                new Bank("BRD - Groupe Societe Generale", "RO29 AAAA 1B31 0075 9384 0002", "6543 654"),
                new Bank("Raiffeisen Bank", "RO36 AAAA 1B31 0075 9384 0003", "9999 999"),
                new Bank("UniCredit Bank", "RO40 AAAA 1B31 0075 9384 0004", "3333 333"),
                new Bank("ING Bank", "RO80 AAAA 1B31 0075 9384 0005", "5555 555"),
                new Bank("Alpha Bank", "RO02 AAAA 1B31 0075 9384 0006", "7777 777"),
                new Bank("Cec Bank", "RO14 AAAA 1B31 0075 9384 0007", "0000 000"),
                new Bank("OTP Bank", "RO91 AAAA 1B31 0075 9384 0008", "4322 433"),
                new Bank("EximBank", "RO53 AAAA 1B31 0075 9384 0009", "8766 877")
        );
    }

    public void generateRandomInvoice(String outputPath) throws IOException {
        Random random = new Random();

        // Select random client
        Client randomClient = clients.get(random.nextInt(clients.size()));

        // Select random company
        Company randomCompany = companies.get(random.nextInt(companies.size()));

        // Select between 1 and 4 random products
        int numberOfProducts = random.nextInt(4) + 1;
        List<Product> randomProducts = new ArrayList<>();
        for (int i = 0; i < numberOfProducts; i++) {
            randomProducts.add(products.get(random.nextInt(products.size())));
        }

        // generate Invoice details using random dates and data
        Date issueDate = new Date();// Generate random dates for the invoice
        Date dueDate = new Date(System.currentTimeMillis() + (7 * 24 * 60 * 60 * 1000)); // Due date is 7 days from now
        Details details = new Details("INV-" + random.nextInt(10000), String.valueOf(random.nextInt(100000)),
                issueDate, dueDate);

        //select random bank
        Bank randomBank = banks.get(random.nextInt(banks.size()));

        // Generate the invoice
        InvoiceMdl.generateInvoice(outputPath, details, randomClient, randomCompany, randomProducts, randomBank);
    }
}


