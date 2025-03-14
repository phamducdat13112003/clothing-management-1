package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.SectionType;
import org.example.clothingmanagement.repository.SectionTypeDAO;

import java.util.List;

public class SectionTypeService {
    private final SectionTypeDAO std = new SectionTypeDAO();
    public List<SectionType> getAllSectionTypes() {
        return std.getAllSectionTypes();
    }
}
