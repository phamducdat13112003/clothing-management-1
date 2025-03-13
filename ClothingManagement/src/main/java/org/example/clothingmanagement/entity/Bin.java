package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.clothingmanagement.service.BinDetailService;
import org.example.clothingmanagement.service.BinService;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Bin {
    private final BinDetailService bds = new BinDetailService();

    private String binID;
    private String binName;
    private double maxCapacity;
    private boolean status;
    private String sectionID;

    // attribute that does not exist in db
    private Double currentCapacity;
    private Double availableCapacity;
}
