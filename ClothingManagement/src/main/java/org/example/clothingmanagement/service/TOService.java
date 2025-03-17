package org.example.clothingmanagement.service;

import org.example.clothingmanagement.repository.TransferOrderDAO;

public class TOService {

    private final TransferOrderDAO transferOrderDAO = new TransferOrderDAO();
    
    public boolean hasProcessingTO(String binID){
        return transferOrderDAO.hasProcessingTO(binID);
    }
}
