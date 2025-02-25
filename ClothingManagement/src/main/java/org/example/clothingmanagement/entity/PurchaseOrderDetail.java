package org.example.clothingmanagement.entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrderDetail {
    private String poDetailID;
    private String poID;
    private String productDetailID;
    private int quantity;
    private int price;
    private int totalPrice;
}
