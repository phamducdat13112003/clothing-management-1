package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeliveryOrderDetail {
    private String doDetailID;
    private String productDetailID;
    private int quantity;
    private String doID;
}
