package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.PurchaseOrder;
import org.example.clothingmanagement.repository.PurchaseOrderDAO;

import java.sql.SQLException;
import java.util.List;

public class PurchaseOrderService {
    private final PurchaseOrderDAO purchaseorderDAO = new PurchaseOrderDAO();

    public List<PurchaseOrder> getAllPurchaseOrder() throws SQLException {
        return purchaseorderDAO.getAllPurchaseOrder();
    }

    public List<PurchaseOrder> getAllPurchaseOrderHaveStatusProcessingAndConfirmed() throws SQLException {
        return purchaseorderDAO.getAllPurchaseOrderHaveStatusProcessingAndConfirmed();
    }

    public String getSupplierIDByPoID(String poID) throws SQLException {
        return purchaseorderDAO.getSupplierIDByPoID(poID);
    }

    public PurchaseOrder getPObyPoID(String poID) throws SQLException {
        return purchaseorderDAO.getPObyPoID(poID);
    }

    public List<PurchaseOrder> searchPO(String searchQuery) throws SQLException {
        return purchaseorderDAO.searchPO(searchQuery);
    }

    public List<PurchaseOrder> searchDO(String searchQuery) throws SQLException {
        return purchaseorderDAO.searchDO(searchQuery);
    }

    public String generateUniquePoId(String date) throws SQLException {
        return purchaseorderDAO.generateUniquePoId(date);
    }

    public boolean addPurchaseOrder(PurchaseOrder purchaseOrder) throws SQLException {
        return purchaseorderDAO.addPurchaseOrder(purchaseOrder);
    }

    public boolean updateStatusPO(String poID, String newStatus) throws SQLException {
        return purchaseorderDAO.updateStatusPO(poID, newStatus);
    }

    public boolean updatePO(String poID, String supplierID, float totalPrice) throws SQLException {
        return purchaseorderDAO.updatePO(poID, supplierID, totalPrice);
    }

}
