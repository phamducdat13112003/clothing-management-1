package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.repository.ProductDetailDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDetailService {
    private final ProductDetailDAO productDetailDAO = new ProductDetailDAO();

    public List<ProductDetail> getAllProductDetails() {
        List<ProductDetail> productDetails = productDetailDAO.findAll();
        return productDetails;
    }

    public List<ProductDetail> getProductDetailById(Long id) {
        List<ProductDetail> productDetails = productDetailDAO.findByProductId(id);
        return productDetails;
    }


}
