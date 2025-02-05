package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.SupplierDAO;

import java.util.List;

public class SupplierService {
    private final SupplierDAO supplierDAO = new SupplierDAO();

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }

}
