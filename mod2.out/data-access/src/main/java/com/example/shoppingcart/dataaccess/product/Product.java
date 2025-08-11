package com.example.shoppingcart.dataaccess.product;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {

    @Id
    private String prodId;

    @Column(nullable = false)
    private String prodName;

    @Column(nullable = false)
    private String prodType;

    @Column(nullable = false)
    private String prodInfo;

    @Column(nullable = false)
    private double prodPrice;

    @Column(nullable = false)
    private int prodQuantity;

    @Lob
    @Column(name = "prod_image")
    private byte[] prodImage;

    // Constructors
    public Product() {
    }

    public Product(String prodId, String prodName, String prodType, String prodInfo, double prodPrice,
                   int prodQuantity, byte[] prodImage) {
        this.prodId = prodId;
        this.prodName = prodName;
        this.prodType = prodType;
        this.prodInfo = prodInfo;
        this.prodPrice = prodPrice;
        this.prodQuantity = prodQuantity;
        this.prodImage = prodImage;
    }

    // Getters and Setters
    public String getProdId() {
        return prodId;
    }

    public void setProdId(String prodId) {
        this.prodId = prodId;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdType() {
        return prodType;
    }

    public void setProdType(String prodType) {
        this.prodType = prodType;
    }

    public String getProdInfo() {
        return prodInfo;
    }

    public void setProdInfo(String prodInfo) {
        this.prodInfo = prodInfo;
    }

    public double getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(double prodPrice) {
        this.prodPrice = prodPrice;
    }

    public int getProdQuantity() {
        return prodQuantity;
    }

    public void setProdQuantity(int prodQuantity) {
        this.prodQuantity = prodQuantity;
    }

    public byte[] getProdImage() {
        return prodImage;
    }

    public void setProdImage(byte[] prodImage) {
        this.prodImage = prodImage;
    }
}
