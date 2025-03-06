package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BinDetail {
    private String binDetailId;
    private String binId;
    private String productDetailId;
    private int quantity;
}
