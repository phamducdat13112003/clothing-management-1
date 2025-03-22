package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SectionType {
    private Integer sectionTypeId;
    private String sectionTypeName;
    private String warehouseId;
    private String description;

}
