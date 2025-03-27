package org.example.clothingmanagement.service;

import org.example.clothingmanagement.entity.*;
import org.example.clothingmanagement.repository.DBContext;
import org.example.clothingmanagement.repository.InventoryDocDAO;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryDocService {

 private static InventoryDocDAO dao= new InventoryDocDAO();

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

    public static void updateInventoryDocStatus(String inventoryDocId, String newStatus) {
    dao.updateInventoryDocStatus(inventoryDocId,newStatus);
    }

    public static String getEmployeeIDByAccountID(String accountID) {
        return dao.getEmployeeIDByAccountID(accountID);
    }

    public static List<SectionType> getAllSectionTypes() {
        return dao.getAllSectionTypes();
    }

    public static List<Section> getSectionsByType(int sectionTypeID) {
        return dao.getSectionsByType(sectionTypeID);
    }

    public static List<Bin> getBinsBySection(String sectionID) {
        return dao.getBinsBySection(sectionID);
    }

    public static void changeBinStatus(String binID, int status) {
        dao.changeBinStatus(binID, status);
    }

    public static boolean canAddProductsToBin(String binId, List<String> productDetailIds, List<Integer> quantities) {
        return dao.canAddProductsToBin(binId, productDetailIds, quantities);
    }

    public static void updateProductDetailQuantity(List<String> productDetailIds, List<Integer> differenceQuantities) {
        dao.updateProductDetailQuantity(productDetailIds, differenceQuantities);
    }

    public static Map<String, String> getProductDetailToProductNameMap() {
        return dao.getProductDetailToProductNameMap();
    }

    public static Map<String, String> getCreatedByToEmployeeNameMap() {
       return dao.getCreatedByToEmployeeNameMap();
    }

    public static void updateBinDetails(String binID, List<String> productDetailIDs, List<Integer> quantities) throws SQLException {
        dao.updateBinDetails(binID, productDetailIDs, quantities);
    }

    public static List<Employee> getAllEmployee() {
        return dao.getAllEmployee();
    }

    public static List<Bin> getBinsBySectionWithStatusOne(String sectionID) {
       return dao.getBinsBySectionWithStatusOne(sectionID);
    }
    public List<InventoryDoc> searchInventoryDoc(String inventoryDocID, String binID) {
        return dao.searchInventoryDoc(inventoryDocID, binID);
    }

    }
