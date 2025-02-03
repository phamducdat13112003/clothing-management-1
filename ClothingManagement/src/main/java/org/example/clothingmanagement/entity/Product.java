package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
public class Product {
    private Long id;
    private String name;
    private Double price;
    private Integer binId;
    private Integer categoryId;
    private String material;
    private String gender;
    private String seasons;
    private Integer minQuantity;
    private Date createdDate;
    private String description;
    private Integer createdBy;
    private Integer supplierId;
    private String madeIn;

    private Integer totalQuantity;
    private String urlImage;


}
