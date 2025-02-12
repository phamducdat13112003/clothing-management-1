package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.repository.EmployeeDAO;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployee();
    }

    public boolean createEmployee(Employee employee)throws SQLException{
        return employeeDAO.createEmployee(employee);
    }
    public boolean updateEmployee(Employee employee)throws SQLException{
        return EmployeeDAO.updateEmployee(employee);
    }
    public boolean deleteEmployee(String employeeID)throws SQLException{
        return employeeDAO.deleteEmployee(employeeID);
    }

    public String getEmployeeId() throws SQLException{
        return employeeDAO.getEmployeeId();
    }

    public static String getEmployeeNameByEmployeeId(int employeeID) throws SQLException{
        return EmployeeDAO.getEmployeeNameByEmployeeId(employeeID);
    }

    public static int getAccountIdByEmployeeId(int employeeID) throws SQLException{
        return EmployeeDAO.getAccountIdByEmployeeId(employeeID);
    }

    public boolean isEmployeeExistedWhenAdd(String email, String phone) throws SQLException{
        return employeeDAO.isEmployeeExistedWhenAdd(email, phone);
    }

    public List<String> getEmployeeIDsWithoutAccount() throws SQLException{
        return employeeDAO.getEmployeeIDsWithoutAccount();
    }

    public boolean isEmployeeExisted(String employeeId ,String email, String phone) throws SQLException{
        return employeeDAO.isEmployeeExisted(employeeId, email, phone);
    }

    public Employee getEmployeeByID(String employeeID) throws SQLException{
        return employeeDAO.getEmployeeByID(employeeID);
    }


}
