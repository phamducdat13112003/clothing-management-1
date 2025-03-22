package org.example.clothingmanagement.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
public class Section {
    private String sectionID;
    private String sectionName;
    private int sectionTypeId;
    private int numberOfBins;

    private String sectionTypeName;
    private String description;

    @Setter
    private int totalBins;

    public Section(String sectionID, String sectionName, int sectionTypeId) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionTypeId = sectionTypeId;
    }


//    public void setNumberOfBins(int size) {
//
//    }
}
