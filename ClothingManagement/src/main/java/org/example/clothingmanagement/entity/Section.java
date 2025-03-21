package org.example.clothingmanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Section {
    private String sectionID;
    private String sectionName;
    private int sectionTypeId;

    private String sectionTypeName;
    private String description;

    public Section(String sectionID, String sectionName, int sectionTypeId) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionTypeId = sectionTypeId;
    }


//    public void setNumberOfBins(int size) {
//
//    }
}
