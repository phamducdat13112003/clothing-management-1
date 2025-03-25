package org.example.clothingmanagement.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class VirtualBin {
    private String virtualBinID;
    private String productDetailID;
    private String quantity;
    private String toID;
    private String originBinID;
    private String finalBinID;
}
