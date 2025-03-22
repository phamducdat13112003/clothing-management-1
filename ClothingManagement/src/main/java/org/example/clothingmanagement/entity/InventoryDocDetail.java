package org.example.clothingmanagement.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class InventoryDocDetail {
    private String inventoryDocDetailId;
    private String productDetailId;
    private String counterId;
    private String recounterId;
    private int originalQuantity;
    private int recountQuantity;
    private Date updateDate;
    private String inventoryDocId;


    //
    private String size;
    private String color;
    private int weight;
    private String productName;
}
