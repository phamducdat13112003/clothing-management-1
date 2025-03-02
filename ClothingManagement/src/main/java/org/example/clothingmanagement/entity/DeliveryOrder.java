package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrder {
    private String doID;
    private Date plannedShippingDate ;
    private Date receiptDate;
    private String poID;
    private String createdBy;
    private String recipient;
    private Boolean status;
}
