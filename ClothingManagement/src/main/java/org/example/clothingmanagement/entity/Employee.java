package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private int roleId;
    private int warehouseID;
    private String image;
    private String roleName;
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

    public Employee(String employeeID, String employeeName, String email, String phone, String address, String gender, LocalDate dateOfBirth,String status, int roleId ,int warehouseID ,  String image) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.roleId = roleId;
        this.warehouseID = warehouseID;
        this.image = image;
    }

    public Employee(String employeeName, String email, String phone, String address, String gender, LocalDate dateOfBirth, int roleId, int warehouseID, String image) {
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dateOfBirth = dateOfBirth;
        this.roleId = roleId;
        this.warehouseID = warehouseID;
        this.image=image;
    }

}
