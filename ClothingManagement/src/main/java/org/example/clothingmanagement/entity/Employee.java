package org.example.clothingmanagement.entity;

import lombok.*;

import java.time.LocalDate;
@ToString
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Employee {
    private String employeeID;
    private String employeeName;
    private String email;
    private String phone;
    private String address;
    private boolean gender;
    private LocalDate dob;
    private String status; //  Active, Inactive
    private String warehouseID;
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

    public Employee(String employeeID, String employeeName, String email, String phone, String address, boolean gender, LocalDate dob,String status ,String warehouseID ,  String image) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.status = status;
        this.warehouseID = warehouseID;
        this.image = image;
    }

    public Employee(String employeeID,String employeeName, String email, String phone, String address, boolean gender, LocalDate dob, String warehouseID, String image) {
        this.employeeID = employeeID;
        this.employeeName = employeeName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.dob = dob;
        this.warehouseID = warehouseID;
        this.image=image;
    }

}
