package org.example.clothingmanagement.entity;

public class PurchaseOrderDetailDTO {
    private String image;
    private String productName;
    private Double weight;
    private String color;
    private String size;
    private Integer quantity;
    private Float price;
    private Float totalPrice;

    public PurchaseOrderDetailDTO(String image, String productName, Double weight, String color,
                                  String size, int quantity, float price, float totalPrice) {
        this.image = image;
        this.productName = productName;
        this.weight = weight;
        this.color = color;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.totalPrice = totalPrice;
    }

    public String getImage() {
        return image;
    }

    public String getProductName() {
        return productName;
    }

    public Double getWeight() {
        return weight;
    }

    public String getColor() {
        return color;
    }

    public String getSize() {
        return size;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Float getPrice() {
        return price;
    }

    public Float getTotalPrice() {
        return totalPrice;
    }
}
