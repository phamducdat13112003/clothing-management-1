package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.entity.Product;
import org.example.clothingmanagement.entity.ProductDetail;
import org.example.clothingmanagement.repository.ProductDAO;

import java.util.*;

public class ProductService {
    private final ProductDAO pd = new ProductDAO();
    private final CategoryService cs = new CategoryService();
    private final ProductDetailService pds = new ProductDetailService();

    public Optional<Product> getTheLastProduct() {
        return pd.getTheLastProduct();
    }

    public HashMap<Product, String> getAllProductsWithPagination(int page, int pageSize) {
        List<Product> products = pd.getProductsWithPagination(page, pageSize);
        List<Category> categories = cs.selectAll();
        LinkedHashMap<Product, String> map = new LinkedHashMap<>();
        for (Product product : products) {
//            // take the first productDetail of a product
//            if (pds.findTheFirstProductDetailOfProductId(product.getId()).isPresent()) {
//                ProductDetail productDetail = pds.findTheFirstProductDetailOfProductId(product.getId()).get();
//                product.setUrlImage(productDetail.getImage());
//            } else {
//                product.setUrlImage("errorImage-NoDataFound");
//            }
//            for (Category category : categories) {
//                if (product.getCategoryId() == category.getCategoryID()) {
//                    map.put(product, category.getCategoryName());
//                }
//            }
            map.put(product, "");
        }
        for (Map.Entry<Product, String> entry : map.entrySet()) {
            Product key = entry.getKey();     // Lấy khóa (Product)
            if(pds.findTheFirstProductDetailOfProductId(key.getId()).isPresent()) {
                ProductDetail productDetail = pds.findTheFirstProductDetailOfProductId(key.getId()).get();
                entry.getKey().setUrlImage(productDetail.getImage());

            }
            else{
                entry.setValue("errorImage-NoDataFound");
            }
            for (Category category : categories) {
                if (entry.getKey().getCategoryId() == category.getCategoryID()) {
                    entry.setValue(category.getCategoryName());
                }
            }
        }
        return map;
    }

    public List<Product> searchProductsByNameSearch(String nameSearch) {
        return pd.searchProductsByNameSearch(nameSearch);
    }

    public HashMap<Product, String> searchProductsWithPagination(String nameSearch, int page, int pageSize) {
        List<Product> products = pd.searchProductsWithPagination(nameSearch, page, pageSize);
        List<Category> categories = cs.selectAll();
        HashMap<Product, String> map = new HashMap<>();
        for (Product product : products) {
            // take the first productDetail of a product
            if (pds.findTheFirstProductDetailOfProductId(product.getId()).isPresent()) {
                ProductDetail productDetail = pds.findTheFirstProductDetailOfProductId(product.getId()).get();
                product.setUrlImage(productDetail.getImage());
            } else {
                product.setUrlImage("errorImage-NoDataFound");
            }
            for (Category category : categories) {
                if (product.getCategoryId() == category.getCategoryID()) {
                    map.put(product, category.getCategoryName());
                }
            }
        }
        return map;
    }

    /**
     *
     * @param product
     * @return true = duplicated, false = good to go
     */
    public boolean checkDup(Product product){
        List<Product> list = pd.getAllProducts();
        for(Product p : list){
            if(product.getName().equalsIgnoreCase(p.getName())
                    && product.getSeasons().equalsIgnoreCase(p.getSeasons())
                    && Objects.equals(product.getCategoryId(), p.getCategoryId())
                    && Objects.equals(product.getSupplierId(), p.getSupplierId())
                    && Objects.equals(product.getPrice(), p.getPrice())
                    && Objects.equals(product.getGender(), p.getGender())
                    && Objects.equals(product.getMaterial(), p.getMaterial())
                    && Objects.equals(product.getMinQuantity(), p.getMinQuantity())
                    && Objects.equals(product.getDescription(), p.getDescription())
                    && Objects.equals(product.getSeasons(), p.getSeasons())
                    && Objects.equals(product.getMadeIn(), p.getMadeIn())
            )
            {
                return true;
            }
        }
        return false;
    }

    public boolean testDup(String name){
        List<Product> list = pd.getAllProducts();
        for(Product p : list){
            if(p.getName().equalsIgnoreCase(name)){
                return true;
            }
        }
        return false;
    }

    public boolean updateProduct(Product product) {
        return pd.updateProduct(product);
    }

    public List<Product> getAllProducts() {
        return pd.getAllProducts();
    }

    public boolean addProduct(Product product) {
        return pd.addProduct(product);
    }

    public boolean deleteProduct(String id) {
        return pd.deleteProduct(id);
    }

    public boolean recoverProduct(String id) {
        return pd.recoverProduct(id);
    }


    public Optional<Product> getProductById(String id) {
        return pd.getProductById(id);
    }

    public Product getProductByProductID(String productID) throws Exception {
        return pd.getProductByProductID(productID);
    }

    public List<Product> getProductsWithPagination(int page, int pageSize) {
        return pd.getProductsWithPagination(page, pageSize);
    }


//    public HashMap<Product, String> searchProducts(String txt) throws Exception {
//        return pd.searchProducts(txt);

    //}
    public List<Map<String, Object>> getAllProductProductDetail() throws Exception {
        return pd.getAllProductProductDetail();
    }

    public List<Map<String, Object>> getListPodetailByPoID(String poID) throws Exception {
        return pd.getListPodetailByPoID(poID);
    }

    public List<Map<String, Object>> getListProductByPoID(String poID) throws Exception {
        return pd.getListProductByPoID(poID);
    }

    public boolean updatePriceOfProductByProductID(String productID, double price) throws Exception {
        return pd.updatePriceOfProductByProductID(productID, price);
    }

    public static void main(String[] args) {
        Product product = new Product("Product 1",1,"Spring/Summer","SP001");
        ProductService productService = new ProductService();
        boolean check = productService.checkDup(product);
        if(check){
            System.out.println("duplicated");
        }
        else{
            System.out.println("not duplicated");
        }
    }


}

