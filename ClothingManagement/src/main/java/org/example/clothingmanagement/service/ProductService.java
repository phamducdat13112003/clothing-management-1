package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.repository.ProductDAO;

import java.util.List;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

    public List<Product> getAllProducts() {
        List<Product> products = productDAO.getAllProducts();
        return products;
    }
}
