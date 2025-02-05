package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Account;
import org.example.clothingmanagement.entity.Role;
import org.example.clothingmanagement.repository.AccountDAO;

import java.sql.SQLException;
import java.util.List;

public class AccountService {
    private final AccountDAO accountDAO = new AccountDAO();

    public List<Account> getAllAccounts() throws SQLException {
        return accountDAO.getAllAccount();
    }
    public Account getAccountById(int id) throws SQLException {
        return accountDAO.getAccountById(id);
    }
    public void createAccount(Account account) throws SQLException {
        accountDAO.createAccount(account);
    }
    public void updateAccount(Account account) throws SQLException {
         accountDAO.updateAccount(account);
    }
    public boolean deleteAccount(int id) throws SQLException {
        return accountDAO.deleteAccount(id);
    }
    public List<Role> getAllRoles() throws SQLException{
        return accountDAO.getAllRoles();
    }

    public boolean isAccountExist(String email) throws SQLException{
        return accountDAO.isAccountExist(email);
    }

    public List<Account> getAllAccountAvaiable() throws SQLException{
        return accountDAO.getAllAccountAvaiable();
    }

    public boolean isAccountExists(int accountId) throws SQLException {
        return AccountDAO.isAccountExists(accountId);
    }
}
