package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.SupplierDAO;

import java.util.List;

public class SupplierService {
    private final SupplierDAO supplierDAO = new SupplierDAO();

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }
    public List<Supplier> getSuppliersWithPagination(int page, int pageSize){
        return supplierDAO.getSuppliersWithPagination(page, pageSize);
    }
    public int getTotalSupplierCount() {
        return supplierDAO.getTotalSupplierCount();
    }

    public Supplier getSupplierBySupplierID(String supplierID) throws Exception{
        return supplierDAO.getSupplierBySupplierID(supplierID);
    }

}
