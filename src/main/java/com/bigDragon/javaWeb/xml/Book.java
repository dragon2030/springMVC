package com.bigDragon.javaWeb.xml;

import java.math.BigDecimal;

public class Book {
    private String attribute;
    private String name;
    private BigDecimal price;
    private String author;

    public Book(String attribute, String name, BigDecimal price, String author) {
        this.attribute = attribute;
        this.name = name;
        this.price = price;
        this.author = author;
    }

    public Book() {
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "Book{" +
                "attribute='" + attribute + '\'' +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", author='" + author + '\'' +
                '}';
    }
}
