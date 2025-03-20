package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bin {
    private String binID;
    private String binName;
    private double maxCapacity;
    private boolean status;
    private String sectionID;

    // attribute that does not exist in db
    private Double currentCapacity;
    private Double availableCapacity;
}
