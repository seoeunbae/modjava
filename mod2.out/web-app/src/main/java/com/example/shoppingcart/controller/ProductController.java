package com.example.shoppingcart.controller;

import com.example.shoppingcart.dataaccess.product.Product;
import com.example.shoppingcart.service.ProductService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/admin/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new Product());
        return "addProduct";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") Product product, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            product.setProdImage(imageFile.getBytes());
        }
        productService.addProduct(product);
        return "redirect:/admin/products/view";
    }

    @GetMapping("/view")
    public String viewAllProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        model.addAttribute("products", products);
        return "viewProducts";
    }

    @GetMapping("/edit/{prodId}")
    public String showEditProductForm(@PathVariable String prodId, Model model) {
        Product product = productService.getProductById(prodId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + prodId));
        model.addAttribute("product", product);
        return "editProduct";
    }

    @PostMapping("/edit")
    public String editProduct(@ModelAttribute("product") Product product, @RequestParam("imageFile") MultipartFile imageFile) throws IOException {
        if (!imageFile.isEmpty()) {
            product.setProdImage(imageFile.getBytes());
        }
        productService.updateProduct(product);
        return "redirect:/admin/products/view";
    }

    @GetMapping("/delete/{prodId}")
    public String deleteProduct(@PathVariable String prodId) {
        productService.deleteProduct(prodId);
        return "redirect:/admin/products/view";
    }

    @GetMapping("/search")
    public String searchProducts(@RequestParam("prodName") String prodName, Model model) {
        List<Product> products = productService.searchProductsByName(prodName);
        model.addAttribute("products", products);
        return "viewProducts";
    }
}
