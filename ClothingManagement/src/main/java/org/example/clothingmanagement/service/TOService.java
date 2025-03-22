package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.TransferOrderDAO;

import java.util.List;

public class TOService {

    private final TransferOrderDAO transferOrderDAO = new TransferOrderDAO();

    public boolean hasProcessingTO(String binID){
        return transferOrderDAO.hasProcessingTO(binID);
    }

    public List<TransferOrder> getAllTransferOrders(){
        return transferOrderDAO.getAllTransferOrders();
    }
}
