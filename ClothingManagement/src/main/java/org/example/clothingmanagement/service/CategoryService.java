package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.Category;
import org.example.clothingmanagement.repository.CategoryDAO;
import org.example.clothingmanagement.repository.EmployeeDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryService {
    private static final CategoryDAO categoryDAO = new CategoryDAO();

    // Phương thức lấy tất cả Category từ cơ sở dữ liệu
    public List<Category> selectAll()  {
        return categoryDAO.selectAll();
    }

    public void addCategory(Category category) throws SQLException {
        categoryDAO.addCategory(category);
    }

    public void deleteCategory(int categoryID) throws SQLException {
        categoryDAO.deleteCategory(categoryID);
    }

    public void updateCategory(Category category) throws SQLException {
        categoryDAO.updateCategory(category);
    }

    public Category getCategoryByID(int categoryID) throws SQLException {
        return categoryDAO.getCategoryByID(categoryID);
    }

    public boolean checkProductCategory(int categoryID) throws SQLException {
       return categoryDAO.checkProductCategory(categoryID);
    }
    public boolean checkCategoryNameExist(String categoryName) throws SQLException {
        return categoryDAO.checkCategoryNameExist(categoryName);
    }
    public static List<Category> filterCategories(String categoryName, Date startDate, Date endDate, String createdBy) throws SQLException {
        return CategoryDAO.filterCategories( categoryName, startDate, endDate, createdBy);
    }
    public static String getEmployeeNameByCreatedBy(String createdBy){
        return categoryDAO.getEmployeeNameByCreatedBy(createdBy);
    }
    public String getEmployeeIDByAccountID(String accountID) {
        return categoryDAO.getEmployeeIDByAccountID(accountID);
    }
    public String getEmployeeIDByName(String categoryName) throws SQLException {
        return CategoryDAO.getEmployeeIDByName( categoryName);
    }
    public List<String> validateCategoryName(String name) throws SQLException {
        return categoryDAO.validateCategoryName(name);
    }

}
