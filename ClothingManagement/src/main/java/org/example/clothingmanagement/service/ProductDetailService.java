package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.repository.ProductDetailDAO;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDetailService {
    private final ProductDetailDAO productDetailDAO = new ProductDetailDAO();

    public List<ProductDetail> getAllProductDetails() {
        return  productDetailDAO.findAll();
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

    public Optional<ProductDetail> getOptionalProductDetailByProductDetailId(String productDetailId){
        return productDetailDAO.getOptionalProductDetailByProductDetailID(productDetailId);
    }

    public String getProductIDByProductDetailID(String productDetailID) throws Exception {
        return productDetailDAO.getProductIDByProductDetailID(productDetailID);
    }

    public boolean deleteProductDetail(String id){
        return productDetailDAO.deleteProductDetail(id);
    }

    public boolean recoverProductDetail(String id){
        return productDetailDAO.recoverProductDetail(id);
    }

    public boolean updateProductDetail(ProductDetail productDetail){
        return productDetailDAO.updateProductDetail(productDetail);
    }

    public static void main(String[] args){
        ProductDetailService pds = new ProductDetailService();
        List<ProductDetail> pdList = pds.getAllProductDetailByProductId("P001");
        for(ProductDetail pd : pdList){
            System.out.println(pd);
        }
    }
}
