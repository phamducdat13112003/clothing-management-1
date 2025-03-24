package org.example.clothingmanagement.entity;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@NoArgsConstructor
@Data
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product {
    private String id;
    private String name;
    private Double price;
    private String material;
    private String gender;
    private String seasons;
    private Integer minQuantity;
    private LocalDate createdDate;
    private String description;
    private String madeIn;
    private Integer categoryId;
    private String createdBy;
    private String supplierId;
    private int Status;
    //attributes not exist in database
    private Integer totalQuantity;
    private String urlImage;
    private Category category;
    private Supplier supplier;
    private Employee employee;

    public Product(Integer categoryId, String description, String gender, String madeIn, String material, Integer minQuantity, String name, Double price, String seasons,String id,String supplierId) {
        this.categoryId = categoryId;
        this.description = description;
        this.gender = gender;
        this.madeIn = madeIn;
        this.material = material;
        this.minQuantity = minQuantity;
        this.name = name;
        this.price = price;
        this.seasons = seasons;
        this.id = id;
        this.supplierId = supplierId;
    }

    public Product(String name, Integer categoryId, String seasons, String supplierId) {
        this.name = name;
        this.categoryId = categoryId;
        this.seasons = seasons;
        this.supplierId = supplierId;
    }

    public Product(String name, Double price, String seasons, String supplierId, String material, String madeIn, String gender, String description, Integer categoryId, Integer minQuantity, String createdBy) {
        this.name = name;
        this.price = price;
        this.seasons = seasons;
        this.supplierId = supplierId;
        this.material = material;
        this.madeIn = madeIn;
        this.gender = gender;
        this.description = description;
        this.categoryId = categoryId;
        this.minQuantity = minQuantity;
        this.createdBy = createdBy;
    }
    public Product(String id,String name, Double price, String seasons, String supplierId, String material, String madeIn, String gender, String description, Integer categoryId, Integer minQuantity,String createdBy, int Status,LocalDate createdDate) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.seasons = seasons;
        this.supplierId = supplierId;
        this.material = material;
        this.madeIn = madeIn;
        this.gender = gender;
        this.description = description;
        this.categoryId = categoryId;
        this.minQuantity = minQuantity;
        this.createdBy = createdBy;
        this.Status = Status;
        this.createdDate = createdDate;
    }



}
