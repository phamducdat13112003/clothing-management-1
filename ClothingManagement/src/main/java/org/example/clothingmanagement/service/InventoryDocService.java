package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.BinDetail;
import org.example.clothingmanagement.entity.InventoryDoc;
import org.example.clothingmanagement.repository.InventoryDocDAO;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public class InventoryDocService {

 private InventoryDocDAO dao= new InventoryDocDAO();

    public List<InventoryDoc> getAllInventoryDocs() {
        return dao.getAllInventoryDocs();
    }

    public void createInventoryDoc(String inventoryId, String employeeId, Date createdDate, String binID, String pending) throws SQLException {
        dao.createInventoryDoc(inventoryId,employeeId,createdDate,binID,pending);
    }

    public String generateInventoryDocID() throws SQLException {
        return dao.generateInventoryDocID();
    }

    public List<BinDetail> getBinDetailByBinID(String binID) {
        return dao.getBinDetailByBinID(binID);
    }

}
