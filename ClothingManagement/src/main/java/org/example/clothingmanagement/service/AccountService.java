package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.repository.AccountDAO;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();

    public List<Account> getAccountsByPage(int page, int pageSize) throws SQLException {
        return accountDAO.getAccountsByPage(page, pageSize);
    }

    public int getTotalAccounts() throws SQLException {
        return accountDAO.getTotalAccounts();
    }

    public int getMaxAccountId() throws SQLException {
        return accountDAO.getMaxAccountId();
    }

    public int getTotalAccounts(String keyword) throws SQLException {
        return accountDAO.getTotalAccounts(keyword);
    }

    public List<Account> searchAccount(String keyword, int page, int pageSize) throws SQLException {
        return accountDAO.searchAccount(keyword, page, pageSize);
    }
    public Account getAccountById(String accountId) throws SQLException {
        return accountDAO.getAccountById(accountId);
    }
    public void createAccount(Account account) throws SQLException {
        accountDAO.createAccount(account);
    }
    public void updateAccount(Account account) throws SQLException {
         accountDAO.updateAccount(account);
    }
    public boolean deleteAccount(String employeeId) throws SQLException {
        return accountDAO.deleteAccount(employeeId);
    }

    public boolean deleteAccountWhenDeleteEmployee (String employeeId) throws SQLException {
        return accountDAO.deleteAccountWhenDeleteEmployee(employeeId);
    }
    public List<Role> getAllRoles() throws SQLException{
        return accountDAO.getAllRoles();
    }
    public boolean updateStatusAccount(String status, String employeeId) throws SQLException{
        return accountDAO.updateStatusAccount(status, employeeId);
    }

    public boolean isAccountExist(String email, String accountId) throws SQLException{
        return accountDAO.isAccountExist(email, accountId);
    }

    public boolean isAccountExists(int accountId) throws SQLException {
        return AccountDAO.isAccountExists(accountId);
    }

    public String getEmployeeNameById(String employeeId) throws SQLException {
        return accountDAO.getEmployeeNameById(employeeId);
    }

    public Account findAccount(String email, String password) throws SQLException{
        return accountDAO.findAccount(email, password);
    }
    public String checkEmailExist(String email) throws SQLException{
        return accountDAO.checkEmailExist(email);
    }
    public void updatePassByEmail(String password, String email)throws SQLException{
        accountDAO.updatePassByEmail(password, email);
    }

    public void updatePassword(String accountId, String newPassword) throws SQLException {
        accountDAO.updatePassword(accountId, newPassword);
    }
}
