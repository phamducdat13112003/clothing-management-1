package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Employee {
    private String employeeID;
    private String employeeName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private LocalDate dateOfBirth;
    private String status; //  Active, Inactive
    private int warehouseID;
    private String image;
    private String warehouseName;

    public Employee(String employeeID,String email, String phone) {
        this.employeeID = employeeID;
        this.email = email;
        this.phone = phone;
    }

    public Employee(String email, String phone) {
        this.email = email;
        this.phone = phone;
    }

    public Employee(String employeeID, String employeeName, String email, String phone, String address, String gender, LocalDate dateOfBirth,String status ,int warehouseID ,  String image) {
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

    public Employee(String employeeName, String email, String phone, String address, String gender, LocalDate dateOfBirth, int warehouseID, String image) {
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.warehouseID = warehouseID;
        this.image=image;
    }

}
