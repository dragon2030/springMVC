package com.bigDragon.javase.ioStream.pdf.itext7;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;

/**
 * @author: bigDragon
 * @create: 2022/4/26
 * @Description:
 */
public class AddPicture {
    public static void main(String[] args) {
        try {
            PdfWriter writer = new PdfWriter("D:\\documentOperation\\pdf\\AddPicture.pdf");
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);

            Image fox = new Image(ImageDataFactory.create("D:\\documentOperation\\pdf\\fox.jpg"));
            Image dog = new Image(ImageDataFactory.create("D:\\documentOperation\\pdf\\dog.jpg"));
            Paragraph p = new Paragraph("The quick brown ")
                    .add(fox)
                    .add(" jumps over the lazy ")
                    .add(dog);
            document.add(p);
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
