package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Category {
    private int categoryID;
    private String categoryName;
    private Date createdDate;
    private String createdBy;
}
