package com.tyson.inventory.service;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.tyson.inventory.entity.Product;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class PdfService {


    public byte[] generateProductPdf(List<Product> products) {


        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();


        Document document = new Document();


        try {

            PdfWriter.getInstance(document, outputStream);


            document.open();


            // Title

            Font titleFont = FontFactory.getFont(
                    FontFactory.HELVETICA_BOLD,
                    18
            );


            Paragraph title =
                    new Paragraph("StockSphere Inventory Report", titleFont);


            title.setAlignment(Element.ALIGN_CENTER);


            document.add(title);


            document.add(new Paragraph("\n"));


            // Table

            PdfPTable table = new PdfPTable(5);


            table.setWidthPercentage(100);


            table.addCell("ID");
            table.addCell("Name");
            table.addCell("Category");
            table.addCell("Price");
            table.addCell("Quantity");



            for(Product product : products){


                table.addCell(String.valueOf(product.getId()));

                table.addCell(product.getProductName());

                table.addCell(product.getCategory());

                table.addCell(
                        String.valueOf(product.getPrice())
                );

                table.addCell(
                        String.valueOf(product.getQuantity())
                );

            }


            document.add(table);


            document.close();



        } catch (DocumentException e) {

            throw new RuntimeException(e);

        }


        return outputStream.toByteArray();

    }

}