package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.repository.RoleDAO;

import java.sql.SQLException;
import java.util.List;

public class RoleService {
    private final RoleDAO roleDAO = new RoleDAO();
    public List<Role> getAllRoles() throws SQLException {
        return roleDAO.getAllRoles();
    }
}
