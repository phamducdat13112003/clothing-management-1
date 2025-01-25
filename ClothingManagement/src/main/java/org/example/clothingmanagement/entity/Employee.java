package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private int employeeID;
    private String employeeName;
    private String email;
    private String phone;
    private String address;
    private int gender; // 1 for Male, 0 for Female
    private LocalDate dateOfBirth;
    private int status; // 1 for Active, 0 for Inactive
    private int accountID;
    private int warehouseID;

}
