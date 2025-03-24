package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Employee;
import org.example.clothingmanagement.repository.EmployeeDAO;

import java.sql.SQLException;
import java.util.List;

public class EmployeeService {
    private final EmployeeDAO employeeDAO = new EmployeeDAO();

    public List<Employee> getAllEmployees() throws SQLException {
        return employeeDAO.getAllEmployees();
    }

    public List<Employee> getAllEmployee(){
        return employeeDAO.getAllEmployee();
    }

    public List<Employee> getEmployeesWithPagination(int page, int pageSize) throws SQLException {
        return employeeDAO.getEmployeesWithPagination(page, pageSize);
    }

    public int getTotalEmployeeCount() throws SQLException {
        return employeeDAO.getTotalEmployeeCount();
    }

    public List<Employee> searchEmployee(String keyword, int page, int pageSize) throws SQLException {
        return employeeDAO.searchEmployee(keyword, page, pageSize);
    }

    public int getTotalEmployeeCount(String keyword) throws SQLException {
        return employeeDAO.getTotalEmployeeCount(keyword);
    }

    public boolean createEmployee(Employee employee)throws SQLException{
        return employeeDAO.createEmployee(employee);
    }
    public boolean updateEmployee(Employee employee)throws SQLException{
        return EmployeeDAO.updateEmployee(employee);
    }

    public int getMaxEmployeeId() throws SQLException {
        return employeeDAO.getMaxEmployeeId();
    }
    public boolean updateAccountEmail(String employeeId, String newEmail) throws SQLException{
        return EmployeeDAO.updateAccountEmail(employeeId, newEmail);
    }

    public boolean hasAccount(String employeeId) throws SQLException {
        return employeeDAO.hasAccount(employeeId);
    }

    public boolean deleteEmployee(String employeeID)throws SQLException{
        return employeeDAO.deleteEmployee(employeeID);
    }

    public String getEmployeeId() throws SQLException{
        return employeeDAO.getEmployeeId();
    }

    public boolean isEmployeeExistedWhenAdd(String email, String phone) throws SQLException{
        return employeeDAO.isEmployeeExistedWhenAdd(email, phone);
    }

    public List<Employee> getEmployeeIDsWithoutAccount() throws SQLException{
        return employeeDAO.getEmployeesWithoutAccount();
    }

    public boolean isEmailExistForEmployee (String email, String employeeId) throws SQLException{
        return employeeDAO.isEmailExistForEmployee(email, employeeId);
    }

    public boolean isEmployeeExisted(String employeeId ,String email, String phone) throws SQLException{
        return employeeDAO.isEmployeeExisted(employeeId, email, phone);
    }

    public Employee getEmployeeByID(String employeeID) throws SQLException{
        return employeeDAO.getEmployeeByID(employeeID);
    }
    public String getEmployeeIdByAccountId(String accountId) throws Exception{
        return employeeDAO.getEmployeeIdByAccountId(accountId);
    }
    public Employee getEmployeeByEmployeeId(String employeeId) throws SQLException{
        return employeeDAO.getEmployeeByEmployeeId(employeeId);
    }

    public void updateEmployeeImage(Employee employee) throws SQLException {
        employeeDAO.updateEmployeeImage(employee);
    }
}
