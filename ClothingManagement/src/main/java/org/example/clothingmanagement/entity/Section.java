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



    public void setNumberOfBins(int size) {

    }
}
