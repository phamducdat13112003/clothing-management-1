package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bin {
    private String binID;
    private String binName;
    private double maxCapacity;
    private boolean status;
    private String sectionID;
}
