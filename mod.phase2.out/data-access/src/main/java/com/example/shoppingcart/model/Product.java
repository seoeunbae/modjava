package com.example.shoppingcart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @Column(name = "prod_id")
    private String prodId;

    @Column(name = "prod_name", nullable = false)
    private String prodName;

    @Column(name = "prod_type", nullable = false)
    private String prodType;

    @Column(name = "prod_info")
    private String prodInfo;

    @Column(name = "prod_price", nullable = false)
    private double prodPrice;

    @Column(name = "prod_quantity", nullable = false)
    private int prodQuantity;

    @Lob
    @Column(name = "prod_image")
    private byte[] prodImage;
}