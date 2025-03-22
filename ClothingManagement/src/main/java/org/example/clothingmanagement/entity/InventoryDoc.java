package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
@Data
@NoArgsConstructor
@AllArgsConstructor

public class InventoryDoc {
    private String inventoryDocId;
    private String createdBy;
    private Date createdDate;
    private String binId;
    private String status;
}
