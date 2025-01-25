package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    private int accountID; // Primary key
    private String email; // Unique email
    private String password; // Password for authentication
    private int roleID; // Foreign key to Role table

}