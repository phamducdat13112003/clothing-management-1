package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String id;
    private String email;
    private String password;
    private int roleId;
    private String roleName;
    private String status;
    private String employeeId;

    public Account(String id, String email, String password , int roleId, String status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.status = status;
    }

    public Account( String email, String password, int roleId, String status, String employeeId) {
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.status = status;
        this.employeeId = employeeId;
    }

}
