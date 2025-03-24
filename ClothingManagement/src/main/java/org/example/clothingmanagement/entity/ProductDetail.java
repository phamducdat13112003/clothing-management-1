package org.example.clothingmanagement.entity;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ProductDetail {
    private String id;
    private Integer quantity;
    private Double weight;
    private String color;
    private String size;
    private String image;
    private String productId;
    private int status;

    // don't existed in database
    private String binId;
    private String sectionId;
    private int totalQuantity;
    private int availableQuantity;
    private int blockedQuantity;
    private String productName;

    private Product product;

    public ProductDetail(String id,String image,Double weight) {
        this.id = id;
        this.image = image;
        this.weight = weight;
    }

    public ProductDetail(String color, String image, Integer quantity, String size, Double weight) {
        this.color = color;
        this.image = image;
        this.quantity = quantity;
        this.size = size;
        this.weight = weight;
    }

    public ProductDetail(Double weight, int status, String size, Integer quantity, String productId, String image, String id, String color) {
        this.weight = weight;
        this.status = status;
        this.size = size;
        this.quantity = quantity;
        this.productId = productId;
        this.image = image;
        this.id = id;
        this.color = color;
    }


}
