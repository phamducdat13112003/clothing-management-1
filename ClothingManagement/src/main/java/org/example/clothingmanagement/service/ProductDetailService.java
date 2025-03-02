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

    public List<ProductDetail> getAllProductDetailByProductId(String productId) {
        return productDetailDAO.findByProductId(productId);
    }

    public Optional<ProductDetail> findTheFirstProductDetailOfProductId(String productId) {
        return productDetailDAO.findTheFirstProductDetailOfProductId(productId);
    }

    public ProductDetail getProductDetailByProductDetailID(String productDetailID) throws Exception {
        return productDetailDAO.getProductDetailByProductDetailID(productDetailID);
    }
    public String getProductIDByProductDetailID(String productDetailID) throws Exception {
        return productDetailDAO.getProductIDByProductDetailID(productDetailID);
    }

    public static void main(String[] args){
        ProductDetailService pds = new ProductDetailService();
        if(pds.findTheFirstProductDetailOfProductId("P001").isPresent()){
            ProductDetail productDetail = pds.findTheFirstProductDetailOfProductId("P001").get();
            System.out.println(productDetail.getImage());

        }
            else{
            System.out.println("ERROR");

        }
    }
}
