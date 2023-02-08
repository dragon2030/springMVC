package com.bigDragon.javase.ioStream.pdf.itext7;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

import java.io.FileNotFoundException;

/**
 * @author: bigDragon
 * @create: 2022/4/26
 * @Description:
 *      HelloWorld
 */
public class HelloWorld {
    public static void main(String[] args) {
        try {
            PdfWriter writer = new PdfWriter("D:\\documentOperation\\pdf\\HelloWorld.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph("Hello World!"));
            document.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
