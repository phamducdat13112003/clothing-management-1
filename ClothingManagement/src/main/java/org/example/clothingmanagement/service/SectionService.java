package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Section;
import org.example.clothingmanagement.repository.DBContext;
import org.example.clothingmanagement.repository.SectionDAO;

import java.util.List;

public class SectionService {
    private final SectionDAO sd = new SectionDAO();

    public List<Section> getAllSection(){
        return sd.getAllSection();
    }

    public List<Section> getSectionsBySectionTypeId(int sectionTypeId){
        return sd.getSectionsBySectionTypeId(sectionTypeId);
    }

}
