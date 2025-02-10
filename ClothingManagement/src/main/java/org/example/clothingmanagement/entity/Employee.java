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
    private String gender; // 1 for Male, 0 for Female
    private LocalDate dateOfBirth;
    private String status; // 1 for Active, 0 for Inactive
    private int accountID;
    private int warehouseID;
    private String image;

    public Employee(int employeeID,String email, String phone) {
        this.employeeID = employeeID;
        this.email = email;
        this.phone = phone;
    }

    public Employee(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public Employee(int employeeID, String employeeName, String email, String phone, String address, String gender, LocalDate dateOfBirth,String status, int warehouseID, String image) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.warehouseID = warehouseID;
        this.image = image;
    }

    public Employee(String name, String email, String phone, String address, String gender, LocalDate dateOfBirth, int accountID, int warehouseID, String image) {
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.accountID = accountID;
        this.warehouseID = warehouseID;
        this.image=image;
    }
}
