package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Warehouse {
    private String warehouseId;
    private String warehouseName;
    private String address;
    private int branchId;
}
