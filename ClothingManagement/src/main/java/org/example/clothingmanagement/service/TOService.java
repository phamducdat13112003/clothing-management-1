package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.TransferOrder;
import org.example.clothingmanagement.repository.TransferOrderDAO;

import java.util.List;

public class TOService {

    private final TransferOrderDAO transferOrderDAO = new TransferOrderDAO();

    public boolean hasProcessingTO(String binID){
        return transferOrderDAO.hasProcessingTO(binID);
    }

    public List<TransferOrder> getTransferOrdersPending(int page, int pageSize){
        return transferOrderDAO.getTransferOrdersPending(page, pageSize);
    }

    public boolean updateTransferOrderStatus(String toID){
        return transferOrderDAO.updateTransferOrderStatus(toID);
    }

    public int countTransferOrdersPending(){
        return transferOrderDAO.countTransferOrdersPending();
    }

    public List<TransferOrder> searchTransferOrdersByTOID(String toID, int page, int pageSize){
        return transferOrderDAO.searchTransferOrdersByTOID(toID, page, pageSize);
    }

    public int countTransferOrdersByTOID(String toID){
        return transferOrderDAO.countTransferOrdersByTOID(toID);
    }
}
