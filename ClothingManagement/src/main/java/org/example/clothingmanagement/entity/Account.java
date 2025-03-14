package org.example.clothingmanagement.entity;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private String id;
    private String email;
    private String password;
    private int roleId;
    private String roleName;
    private LocalDateTime lastUpdate;
    private String status;
    private String employeeId;

    public Account(String id, String email, String password ,LocalDateTime lastUpdate, int roleId, String status) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.lastUpdate = lastUpdate;
        this.roleId = roleId;
        this.status = status;
    }

    public Account(String id ,String email, String password, int roleId, LocalDateTime lastUpdate,String status, String employeeId) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.roleId = roleId;
        this.lastUpdate = lastUpdate;
        this.status = status;
        this.employeeId = employeeId;
    }

}
