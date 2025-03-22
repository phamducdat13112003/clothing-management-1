package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.InventoryDocDetail;
import org.example.clothingmanagement.repository.InventoryDocDetailDAO;

import java.util.List;

public class InventoryDocDetailService {
    private InventoryDocDetailDAO dao  = new InventoryDocDetailDAO();
    public List<InventoryDocDetail> getInventoryDocDetailsByDocID(String inventoryDocID) {
        return dao.getInventoryDocDetailsByDocID(inventoryDocID);
    }

}
