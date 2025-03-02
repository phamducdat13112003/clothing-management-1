package org.example.clothingmanagement.entity;


import jakarta.servlet.annotation.WebServlet;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
}
