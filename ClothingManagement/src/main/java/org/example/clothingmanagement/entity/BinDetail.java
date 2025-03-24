package org.example.clothingmanagement.entity;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class BinDetail {
    private String binDetailId;
    private String binId;
    private String productDetailId;
    private int quantity;

    // trong database không có
    private ProductDetail productDetail;
    private double maxCapacity;
    private double binFullPercentage;
    private double binRemainingPercentage;
    private int totalQuantity;
    private int status;
    private double weight;
    private String color;
    private String size;
    private String image;
    private String productName;

    public BinDetail(String binDetailId, int quantity) {
        this.binDetailId = binDetailId;
        this.totalQuantity = quantity;
    }


}
