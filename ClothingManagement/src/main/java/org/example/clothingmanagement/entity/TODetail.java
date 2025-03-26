package org.example.clothingmanagement.entity;


import jakarta.servlet.annotation.WebServlet;
import lombok.*;
@Setter
@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TODetail {
    private String toDetailID;
    private String productDetailID;
    private int quantity;
    private String toID;
    private String originBinID;
    private String finalBinID;

    private String productName;
    private double weight;

}
