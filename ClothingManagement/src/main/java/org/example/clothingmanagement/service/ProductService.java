package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.repository.ProductDAO;

import java.util.List;
import java.util.Optional;

public class ProductService {
    private final ProductDAO productDAO = new ProductDAO();

//    public List<Product> getAllProducts() {
//        List<Product> products = productDAO.getAllProducts();
//        return products;
//    }

//    public boolean addProduct(Product product) {
//        return productDAO.addProduct(product);
//    }

    public boolean deleteProduct(Integer id) {
        return productDAO.deleteProduct(id);
    }

//    public Optional<Product> getProductById(Integer id) {
//        return productDAO.getProductById(id);
//    }
//    public Optional<Product> getProductById(String id) {
//        return productDAO.getProductById(id);
//    }
public Product getProductByProductID(String productID) throws Exception {
          return productDAO.getProductByProductID(productID);
      }
}
