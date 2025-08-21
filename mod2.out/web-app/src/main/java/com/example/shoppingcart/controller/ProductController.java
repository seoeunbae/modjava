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
import org.springframework.web.bind.annotation.PathVariable;


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

    @GetMapping("/admin/products/update/{prodId}")
    public String showUpdateProductForm(@PathVariable String prodId, Model model) {
        productService.getProductById(prodId).ifPresent(product -> {
            model.addAttribute("product", product);
        });
        return "updateProduct";
    }

    @PostMapping("/admin/products/update")
    public String updateProduct(
            @RequestParam("prodId") String prodId,
            @RequestParam("prodName") String prodName,
            @RequestParam("prodType") String prodType,
            @RequestParam("prodInfo") String prodInfo,
            @RequestParam("prodPrice") double prodPrice,
            @RequestParam("prodQuantity") int prodQuantity,
            @RequestParam("prodImage") MultipartFile prodImageFile,
            Model model
    ) {
        try {
            Product product = productService.getProductById(prodId).orElse(null);
            if (product == null) {
                model.addAttribute("message", "Product not found!");
                return "redirect:/admin/products/view";
            }

            product.setProdName(prodName);
            product.setProdType(prodType);
            product.setProdInfo(prodInfo);
            product.setProdPrice(prodPrice);
            product.setProdQuantity(prodQuantity);

            if (!prodImageFile.isEmpty()) {
                byte[] prodImage = prodImageFile.getBytes();
                product.setProdImage(prodImage);
            }

            productService.updateProduct(product);
            return "redirect:/admin/products/view";
        } catch (IOException e) {
            e.printStackTrace();
            model.addAttribute("message", "Product update failed due to image processing error!");
            return "redirect:/admin/products/update/" + prodId;
        }
    }

    @GetMapping("/admin/products/delete/{prodId}")
    public String deleteProduct(@PathVariable String prodId) {
        productService.deleteProduct(prodId);
        return "redirect:/admin/products/view";
    }
}