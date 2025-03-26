package org.example.clothingmanagement.entity;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class Section {
    private String sectionID;
    private String sectionName;
    private int sectionTypeId;
    private int numberOfBins;

    private String sectionTypeName;
    private String description;
    private int totalBins;
    private int status;


    public Section(String sectionID, String sectionName, int sectionTypeID) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionTypeId = sectionTypeID;
    }

    public Section(String sectionID, String sectionName, int sectionTypeID, int status) {
        this.sectionID = sectionID;
        this.sectionName = sectionName;
        this.sectionTypeId = sectionTypeID;
        this.status = status;
    }
}
