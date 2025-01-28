package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private int id;
    private String email;
    private String password;
    private int roleId;

    public Account(int id, String email, String password) {
        this.id = id;
        this.email = email;
        this.password = password;
    }

    public Account( String email, String password, int roleId) {
        this.email = email;
        this.password = password;
        this.roleId = roleId;
    }

}
