package org.example.clothingmanagement.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Supplier {
    private String supplierId;
    private String supplierName;
    private String address;
    private String email;
    private String phone;
    private boolean status;
}
