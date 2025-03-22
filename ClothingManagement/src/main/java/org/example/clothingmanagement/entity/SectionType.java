package org.example.clothingmanagement.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class SectionType {
    private Integer sectionTypeId;
    private String sectionTypeName;
    private String warehouseId;
    private String description;

}
