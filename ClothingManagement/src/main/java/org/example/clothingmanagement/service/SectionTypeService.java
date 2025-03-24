package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.repository.SectionTypeDAO;

import java.util.List;
import java.util.Optional;

public class SectionTypeService {
    private final SectionTypeDAO std = new SectionTypeDAO();
    public List<SectionType> getAllSectionTypes() {
        return std.getAllSectionTypes();
    }
    public List<SectionType> getSectionTypesWithPagination(int page, int pageSize) {
        return std.getSectionTypesWithPagination(page, pageSize);
    }

    public List<SectionType> searchSectionTypes(String search) {
        return std.searchSectionTypes(search);
    }

    public List<SectionType> searchSectionTypesWithPagination(String search, int page, int pageSize) {
        return std.searchSectionTypesWithPagination(search, page, pageSize);
    }
    public Optional<SectionType> getSectionTypeById(int sectionTypeId) {
        return Optional.ofNullable(std.getSectionTypeById(sectionTypeId));
    }

    public SectionType getSectionTypeBySectionId(String sectionId){
        return std.getSectionTypeBySectionId(sectionId);
    }

    public String getSectionTypeNameById(int sectionTypeID){
        return std.getSectionTypeNameById(sectionTypeID);
    }

    public static void main(String[] args){
        SectionTypeService sts = new SectionTypeService();
        List<SectionType> list = sts.searchSectionTypesWithPagination("ss",1,5);
        List<SectionType> list2 = sts.searchSectionTypes("ss");
        for(SectionType st : list2){
            System.out.println(st);
        }
    }
}
