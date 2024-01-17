package org.scrum.domain.models;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;


import java.io.IOException;
import java.util.List;
import java.util.Random;


public class InvoiceMdl {

    private static final float TITLE_Y = 750;
    private static final float LEFT_X = 50;// leftmost most X coordinate
    private static final float RIGHT_X = 550;// right most X coordinate
    private static final float P_COL = (RIGHT_X-LEFT_X)/5; // column width for the products sec
    private static final float COMMON_Y = 570; // Y-coordinate used for invoice, company, client and product details


    public static void generateInvoice(String outputPath,  Details details, Client client, Company company, List<Product> products, Bank bank) throws IOException {
        String invName = generateRandomPreffix() + "_" + generateRandomSuffix() + ".pdf";


        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {

                 // Title
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 18);//set the title font
                generateText(contentStream,"Factura Fiscala", LEFT_X, TITLE_Y);
                drawLine(contentStream, LEFT_X, TITLE_Y - 15,  LEFT_X+120 , TITLE_Y - 15);

                //set the font for the invoice content
                contentStream.setFont(PDType1Font.HELVETICA, 11);

                // Invoice details section
                generateText(contentStream, "Serie si numar: " + details.getSeriesAndNumber(), RIGHT_X - 150, COMMON_Y + 140);
                generateText(contentStream, "Referinta unica: " + details.getUniqueReference(), RIGHT_X - 150, COMMON_Y + 125);
                generateText(contentStream, "Data emiterii: " + details.getIssueDate(), RIGHT_X - 150, COMMON_Y + 110);
                generateText(contentStream, "Data scadenta: " + details.getDueDate(), RIGHT_X - 150, COMMON_Y + 95);
                drawLine(contentStream, RIGHT_X - 160, COMMON_Y + 80, RIGHT_X, COMMON_Y + 80);

                //client 
                // company details table
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                generateText(contentStream, "Furnizor:", LEFT_X, COMMON_Y);
                contentStream.setFont(PDType1Font.HELVETICA, 11);
                generateText(contentStream, company.getName(), LEFT_X, COMMON_Y - 15);
                generateText(contentStream, company.getCui(), LEFT_X, COMMON_Y - 30);
                generateText(contentStream, company.getAddress(), LEFT_X, COMMON_Y - 45);
                generateText(contentStream, company.getCity(), LEFT_X, COMMON_Y - 60);
                generateText(contentStream, company.getEmail(), LEFT_X, COMMON_Y - 75);

                // client details section
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                generateText(contentStream, "Cumparator:", RIGHT_X - 150, COMMON_Y);
                contentStream.setFont(PDType1Font.HELVETICA, 11);
                generateText(contentStream, client.getName(), RIGHT_X - 150, COMMON_Y - 15);
                generateText(contentStream, client.getCui(), RIGHT_X-150, COMMON_Y - 30);
                generateText(contentStream, client.getAddress(), RIGHT_X-150, COMMON_Y - 45);
                generateText(contentStream, client.getCity(), RIGHT_X-150, COMMON_Y - 60);
                generateText(contentStream, client.getEmail(), RIGHT_X-150, COMMON_Y - 75);


                // Product details section_________________________________________________________
                //variables for multiple product insertion
                float rowHeight = 20; // Height of each row
                int rows = 3;
                float X1 = LEFT_X + P_COL+30;
                float X2 = LEFT_X + (P_COL) * 2;
                float X3 = LEFT_X + (P_COL) * 3;
                float X4 = LEFT_X + (P_COL) * 4;
                float lastRowY = COMMON_Y - 190;
                float subTotal =0;

                // header
                drawHeaderRectangle(contentStream, LEFT_X, COMMON_Y - 150, RIGHT_X, 20); // Outer rectangle around the header+fill
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                generateCenteredText(contentStream, "Serviciu/Produs", LEFT_X, COMMON_Y - 145, X1);
                generateCenteredText(contentStream, "U.M.", X1, COMMON_Y - 145, X2);
                generateCenteredText(contentStream, "Cantitate", X2, COMMON_Y - 145, X3);
                generateCenteredText(contentStream, "Pret unitar(RON)", X3, COMMON_Y - 145, X4);
                generateCenteredText(contentStream, "Valoare(RON)", X4, COMMON_Y - 145, RIGHT_X);
                contentStream.setFont(PDType1Font.HELVETICA, 11);
                // header
                drawHeaderRectangle(contentStream, LEFT_X, COMMON_Y - 170, RIGHT_X, 20); // Outer rectangle around the header+fill
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                generateCenteredText(contentStream, "1", LEFT_X, COMMON_Y - 165, X1);
                generateCenteredText(contentStream, "2", X1, COMMON_Y - 165, X2);
                generateCenteredText(contentStream, "3", X2, COMMON_Y - 165, X3);
                generateCenteredText(contentStream, "4", X3, COMMON_Y - 165, X4);
                generateCenteredText(contentStream, "5(3 x 4)", X4, COMMON_Y - 165, RIGHT_X);
                contentStream.setFont(PDType1Font.HELVETICA, 11);

                // Loop through products and generate rows dynamically....fml
                for (Product product : products) {
                    drawContentRectangle(contentStream, LEFT_X, lastRowY, RIGHT_X, rowHeight);
                    generateCenteredText(contentStream, product.getService(), LEFT_X, lastRowY + 5, X1);
                    generateCenteredText(contentStream, product.getUnit(), X1, lastRowY + 5, X2);
                    generateCenteredText(contentStream, product.getQty(), X2, lastRowY + 5, X3);
                    generateCenteredText(contentStream, product.getPrice(), X3, lastRowY + 5, X4);
                    generateCenteredText(contentStream, product.getValue(), X4, lastRowY + 5, RIGHT_X);
                    lastRowY -= rowHeight; // Move to the next row
                    rows +=1; //increment row
                    subTotal += Float.parseFloat(product.getValue());

                }
                drawLine(contentStream, X1, lastRowY +rowHeight, X1, lastRowY + (rowHeight*rows)); // Vertical line after "Service"
                drawLine(contentStream, X2, lastRowY +rowHeight, X2, lastRowY + (rowHeight*rows)); // Vertical line after "Hrs/Qty"
                drawLine(contentStream, X3, lastRowY +rowHeight, X3, lastRowY + (rowHeight*rows)); // Vertical line after "Rate/Price"
                drawLine(contentStream, X4, lastRowY +rowHeight, X4, lastRowY + (rowHeight*rows)); // Vertical line after "Adjust"

                // Totals section______________________________________________
                float totalsHeaderY = lastRowY - 55; // separation from above table

                // header
                drawHeaderRectangle(contentStream, LEFT_X + P_COL * 3, totalsHeaderY, LEFT_X + P_COL * 4, 60); // Outer rectangle around the header+fill
                contentStream.setFont(PDType1Font.HELVETICA_BOLD_OBLIQUE, 11);
                generateCenteredText(contentStream, "Sub Total(RON)", LEFT_X + P_COL * 3, totalsHeaderY + 45, LEFT_X + P_COL * 4);
                generateCenteredText(contentStream, "TVA(RON)", LEFT_X + P_COL * 3, totalsHeaderY + 25, LEFT_X + P_COL * 4);
                generateCenteredText(contentStream, "Total(RON)", LEFT_X + P_COL * 3, totalsHeaderY + 5, LEFT_X + P_COL * 4);
                contentStream.setFont(PDType1Font.HELVETICA, 11);
                // content
                drawContentRectangle(contentStream, LEFT_X + P_COL * 4, totalsHeaderY, RIGHT_X, 60); // Outer rectangle around the content
                generateCenteredText(contentStream, String.valueOf(subTotal), LEFT_X + P_COL * 4, totalsHeaderY + 45, LEFT_X + P_COL * 5);
                generateCenteredText(contentStream, String.valueOf(subTotal*0.14f), LEFT_X + P_COL * 4, totalsHeaderY + 25, LEFT_X + P_COL * 5);
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 11);
                generateCenteredText(contentStream, String.valueOf((subTotal*0.14f)+subTotal), LEFT_X + P_COL * 4, totalsHeaderY + 5, LEFT_X + P_COL * 5);
                contentStream.setFont(PDType1Font.HELVETICA, 11);
                // Lines
                drawLine(contentStream, LEFT_X + P_COL * 3, totalsHeaderY + 20, RIGHT_X, totalsHeaderY + 20); // Separator line after subtotal
                drawLine(contentStream, LEFT_X + P_COL * 3, totalsHeaderY + 40, RIGHT_X, totalsHeaderY + 40); // Separator line after tax
                drawLine(contentStream, LEFT_X + P_COL * 4, totalsHeaderY, LEFT_X + P_COL * 4, totalsHeaderY + 60); // Vertical line



                // Bank details section___________________________________________
                generateText(contentStream, bank.getBankName(), LEFT_X, 105);
                generateText(contentStream, bank.getAccountNumber(), LEFT_X, 90);
                generateText(contentStream, bank.getBsbNumber(), LEFT_X, 75);
                drawLine(contentStream, LEFT_X, 60, LEFT_X + RIGHT_X, 60);

                // Endnote
                contentStream.setFont(PDType1Font.HELVETICA_OBLIQUE, 9);
                generateText(contentStream,"Factura circula fara semnatura si stampila conform Legii 227/2015, privind Codul Fiscal, art. 319(29)", LEFT_X, 45);
                generateText(contentStream,"Ai n-ai mingea...tragi la poarta", LEFT_X, 35);

            }
            System.out.println(outputPath);
            document.save(outputPath+invName);
        }
    }
    private static void generateText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
    private static void drawHeaderRectangle(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException {
        contentStream.setLineWidth(1.0f); // Set the line width for the rectangle
        contentStream.moveTo(x, y);
        contentStream.lineTo(width, y);
        contentStream.lineTo(width, y + height);
        contentStream.lineTo(x, y + height);
        contentStream.setNonStrokingColor(211, 211, 211);
        contentStream.closeAndFillAndStroke();
        contentStream.setNonStrokingColor(0, 0, 0);
    }
    private static void drawContentRectangle(PDPageContentStream contentStream, float x, float y, float width, float height) throws IOException {
        contentStream.setLineWidth(1.0f); // Set the line width for the rectangle
        contentStream.moveTo(x, y);
        contentStream.lineTo(width, y);
        contentStream.lineTo(width, y + height);
        contentStream.lineTo(x, y + height);
        contentStream.closeAndStroke();
    }
    private static void drawLine(PDPageContentStream contentStream, float startX, float startY, float endX, float endY) throws IOException {
        contentStream.setLineWidth(1.0f); // Set the line width for the line
        contentStream.moveTo(startX, startY);
        contentStream.lineTo(endX, endY);
        contentStream.stroke();
    }
    private static void generateCenteredText(PDPageContentStream contentStream, String text, float leftX, float centerY, float rightX) throws IOException {
        float textWidth = PDType1Font.HELVETICA.getStringWidth(text) / 1000 * 12; // Assuming font size 12
        float xOffset = (rightX - leftX - textWidth) / 2;
        generateText(contentStream, text, leftX + xOffset, centerY);
    }
    // generate a random prefix with letters and numbers
    private static String generateRandomPreffix() {
        Random random = new Random();
        int length = random.nextInt(15);
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789_.-";
        StringBuilder randomPreffix = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomPreffix.append(characters.charAt(index));
        }
        return randomPreffix.toString();

    }

    // generate a random suffix with numbers
    private static String generateRandomSuffix() {
        Random random = new Random();
        int length = random.nextInt(5);
        String characters = "0123456789";
        StringBuilder randomSuffix = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            randomSuffix.append(characters.charAt(index));
        }
        return randomSuffix.toString();
    }
}


