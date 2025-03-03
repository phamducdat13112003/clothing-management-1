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
    public String getSupplierIDByPoID(String poID) throws SQLException {
        return purchaseorderDAO.getSupplierIDByPoID(poID);
    }
    public PurchaseOrder getPObyPoID(String poID) throws SQLException {
        return purchaseorderDAO.getPObyPoID(poID);
    }
    public List<PurchaseOrder> searchPO(String searchQuery) throws SQLException {
        return purchaseorderDAO.searchPO(searchQuery);
    }

}
