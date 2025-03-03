package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {
    private String poID;
    private Date CreatedDate;
    private String supplierID;
    private String createdBy;
    private String status;
    private float totalPrice;

}
