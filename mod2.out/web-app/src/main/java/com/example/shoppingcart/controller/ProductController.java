package com.example.shoppingcart.controller;

import com.example.shoppingcart.model.Product;
import com.example.shoppingcart.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/admin/addProduct")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/admin/products/add")
    public String addProduct(
            @RequestParam("prodName") String prodName,
            @RequestParam("prodType") String prodType,
            @RequestParam("prodInfo") String prodInfo,
            @RequestParam("prodPrice") double prodPrice,
            @RequestParam("prodQuantity") int prodQuantity,
            @RequestParam("prodImage") MultipartFile prodImageFile,
            Model model
    ) {
        try {
            byte[] prodImage = prodImageFile.getBytes();
            if (productService.addProduct(prodName, prodType, prodInfo, prodPrice, prodQuantity, prodImage)) {
                System.out.println("Product added successfully, redirecting to /admin/products/view");
                return "redirect:/admin/products/view";
            } else {
                model.addAttribute("message", "Product Registration Failed!");
                return "addProduct";
            }
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Product Registration Failed due to image processing error!");
            return "addProduct";
        }
    }

    
}