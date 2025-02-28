package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Supplier;
import org.example.clothingmanagement.repository.SupplierDAO;

import java.sql.SQLException;
import java.util.List;

public class SupplierService {
    private final SupplierDAO supplierDAO = new SupplierDAO();

    public List<Supplier> getAllSuppliers() {
        return supplierDAO.getAllSuppliers();
    }
    public List<Supplier> getSuppliersWithPagination (int page, int pageSize) {
        return supplierDAO.getSuppliersWithPagination(page, pageSize);
    }
    public int getTotalSupplierCount() {
        return supplierDAO.getTotalSupplierCount();
    }

    public int getMaxSupplierId() throws SQLException {
        return supplierDAO.getMaxSupplierId();
    }
    public boolean createSupplier(Supplier supplier) {
        return supplierDAO.createSupplier(supplier);
    }
    public boolean updateSupplier(Supplier supplier) {
        return supplierDAO.updateSupplier(supplier);
    }

    public boolean isSupplierExistedWhenAdd(String email, String phone) throws SQLException {
        return supplierDAO.isSupplierExistedWhenAdd(email, phone);
    }
    public boolean isSupplierExisted(String supplierId, String email, String phone) throws SQLException {
        return supplierDAO.isSupplierExisted(supplierId, email, phone);
    }
    public Supplier getSupplierById(String supplierId) throws SQLException {
        return supplierDAO.getSupplierById(supplierId);
    }
    public boolean deleteSupplier(String supplierId) throws SQLException {
        return supplierDAO.deleteSupplier(supplierId);
    }



}
