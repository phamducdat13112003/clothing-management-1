package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.PurchaseOrderDetail;
import org.example.clothingmanagement.repository.PurchaseOrderDetailDAO;

import java.sql.SQLException;
import java.util.List;
public class PurchaseOrderDetailService {
    private final PurchaseOrderDetailDAO purchaseOrderDetailDAO = new PurchaseOrderDetailDAO();

    public List<PurchaseOrderDetail> getListPODetailbypoID(String poID) throws SQLException {
        return purchaseOrderDetailDAO.getListPODetailbypoID(poID);
    }

    public String getProductDetailID(String poID) throws SQLException {
        return purchaseOrderDetailDAO.getProductDetailID(poID);
    }
}

