/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pharmacy.db;

import static com.pharmacy.db.DBConnection.getConnection;
import com.pharmacy.db.MedicinesDB.Medicine;
import com.pharmacy.ui.SalesPage.CartItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class InvoicesDB {

    public static class Invoice {

        private int invoiceId;
        private int customerId;
        private int userId;
        private String invoiceDate;
        private double totalAmount;
        private String paymentMethod;

        private List<InvoiceDetail> details;

        public Invoice(int invoiceId, int customerId, int userId,
                String invoiceDate, double totalAmount, String paymentMethod) {

            this.invoiceId = invoiceId;
            this.customerId = customerId;
            this.userId = userId;
            this.invoiceDate = invoiceDate;
            this.totalAmount = totalAmount;
            this.paymentMethod = paymentMethod;

            this.details = new ArrayList<>();
        }

        public int getInvoiceId() {
            return invoiceId;
        }

        public int getCustomerId() {
            return customerId;
        }

        public int getUserId() {
            return userId;
        }

        public String getInvoiceDate() {
            return invoiceDate;
        }

        public double getTotalAmount() {
            return totalAmount;
        }

        public String getPaymentMethod() {
            return paymentMethod;
        }

        public List<InvoiceDetail> getDetails() {
            return details;
        }

        public void addDetail(InvoiceDetail detail) {
            details.add(detail);
        }
    }

    public static class InvoiceDetail {

        private int detailId;
        private int invoiceId;
        private int medicineId;
        private int quantity;
        private double unitPrice;
        private double subtotal;

        public InvoiceDetail(int detailId, int invoiceId, int medicineId,
                int quantity, double unitPrice, double subtotal) {

            this.detailId = detailId;
            this.invoiceId = invoiceId;
            this.medicineId = medicineId;
            this.quantity = quantity;
            this.unitPrice = unitPrice;
            this.subtotal = subtotal;
        }

        public int getDetailId() {
            return detailId;
        }

        public int getInvoiceId() {
            return invoiceId;
        }

        public int getMedicineId() {
            return medicineId;
        }

        public int getQuantity() {
            return quantity;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public double getSubtotal() {
            return subtotal;
        }
    }

    public static List<Invoice> getAllInvoicesFull() {

        List<Invoice> invoices = new ArrayList<>();

        String SQL_INVOICES = "SELECT * FROM sales_invoices ORDER BY invoice_id ASC";

        try (Connection conn = getConnection(); PreparedStatement ps = conn.prepareStatement(SQL_INVOICES); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Invoice inv = new Invoice(
                        rs.getInt("invoice_id"),
                        rs.getInt("customer_id"),
                        rs.getInt("user_id"),
                        rs.getString("invoice_date"),
                        rs.getDouble("total_amount"),
                        rs.getString("payment_method")
                );
                invoices.add(inv);
            }

            loadAllDetails(conn, invoices);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return invoices;
    }

    private static void loadAllDetails(Connection conn, List<Invoice> invoices) throws Exception {

        if (invoices.isEmpty()) {
            return;
        }

        String SQL_DETAILS = "SELECT * FROM sales_details ORDER BY invoice_id ASC";

        try (PreparedStatement ps = conn.prepareStatement(SQL_DETAILS); ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                InvoiceDetail detail = new InvoiceDetail(
                        rs.getInt("sale_detail_id"),
                        rs.getInt("invoice_id"),
                        rs.getInt("medicine_id"),
                        rs.getInt("quantity"),
                        rs.getDouble("unit_price"),
                        rs.getDouble("subtotal")
                );

                for (Invoice inv : invoices) {
                    if (inv.getInvoiceId() == detail.getInvoiceId()) {
                        inv.addDetail(detail);
                        break;
                    }
                }
            }
        }
    }

    public static boolean deleteInvoice(int invoiceId) {

        String DELETE_DETAILS_SQL = "DELETE FROM sales_details WHERE invoice_id = ?";
        String DELETE_INVOICE_SQL = "DELETE FROM sales_invoices WHERE invoice_id = ?";

        Connection conn = null;
        PreparedStatement psDetails = null;
        PreparedStatement psInvoice = null;

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            conn.setAutoCommit(false);

            psDetails = conn.prepareStatement(DELETE_DETAILS_SQL);
            psDetails.setInt(1, invoiceId);
            psDetails.executeUpdate();

            psInvoice = conn.prepareStatement(DELETE_INVOICE_SQL);
            psInvoice.setInt(1, invoiceId);
            int invoiceDeleted = psInvoice.executeUpdate();

            conn.commit();
            return invoiceDeleted > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (Exception e) {
            }
            return false;

        } finally {
            try {
                if (psDetails != null) {
                    psDetails.close();
                }
            } catch (Exception e) {
            }
            try {
                if (psInvoice != null) {
                    psInvoice.close();
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.setAutoCommit(true);
                }
            } catch (Exception e) {
            }
            try {
                if (conn != null) {
                    conn.close();
                }
            } catch (Exception e) {
            }
        }
    }

    public static int createInvoice(int customerId, int userId, String paymentMethod, List<CartItem> cart) throws SQLException {
        Connection conn = null;
        PreparedStatement invoiceStmt = null;
        PreparedStatement detailStmt = null;
        PreparedStatement stockStmt = null;
        ResultSet rs = null;
        int invoiceId = -1;

        String insertInvoiceSQL = "INSERT INTO sales_invoices (customer_id, user_id,payment_method, total_amount, invoice_date) VALUES (?,?, ?, ?, NOW())";
        String insertDetailSQL = "INSERT INTO sales_details (invoice_id, medicine_id, quantity, unit_price) VALUES (?, ?, ?, ?)";
        String updateStockSQL = "UPDATE medicines SET stock_quantity = stock_quantity - ? WHERE medicine_id = ? AND stock_quantity >= ?";

        try {
            conn = DBConnection.getConnection();
            if (conn == null) {
                throw new SQLException("Unable to obtain DB connection");
            }
            conn.setAutoCommit(false);

            double totalPrice = 0.0;
            for (CartItem item : cart) {
                totalPrice += item.getMedicine().getUnitPrice() * item.getQuantity();
            }

            invoiceStmt = conn.prepareStatement(insertInvoiceSQL, Statement.RETURN_GENERATED_KEYS);
            invoiceStmt.setInt(1, customerId);
            invoiceStmt.setInt(2, userId);
            invoiceStmt.setString(3, paymentMethod);
            invoiceStmt.setDouble(4, totalPrice);
            invoiceStmt.executeUpdate();

            rs = invoiceStmt.getGeneratedKeys();
            if (rs.next()) {
                invoiceId = rs.getInt(1);
            } else {
                conn.rollback();
                throw new SQLException("Failed to retrieve generated invoice id");
            }

            detailStmt = conn.prepareStatement(insertDetailSQL);
            stockStmt = conn.prepareStatement(updateStockSQL);

            for (CartItem item : cart) {
                int medId = item.getMedicine().getMedicineId();
                int qty = item.getQuantity();
                double unitPrice = item.getMedicine().getUnitPrice();

                detailStmt.setInt(1, invoiceId);
                detailStmt.setInt(2, medId);
                detailStmt.setInt(3, qty);
                detailStmt.setDouble(4, unitPrice);
                detailStmt.addBatch();
            }

            detailStmt.executeBatch();

            for (CartItem item : cart) {
                int medId = item.getMedicine().getMedicineId();
                int qty = item.getQuantity();

                stockStmt.setInt(1, qty);
                stockStmt.setInt(2, medId);
                stockStmt.setInt(3, qty);
                int updated = stockStmt.executeUpdate();
                if (updated == 0) {
                    conn.rollback();
                    throw new SQLException("Insufficient stock for medicine_id=" + medId);
                }
            }

            conn.commit();
            return invoiceId;

        } catch (SQLException ex) {
            if (conn != null) try {
                conn.rollback();
            } catch (SQLException ignore) {
            }
            throw ex;
        } finally {
            if (rs != null) try {
                rs.close();
            } catch (SQLException ignore) {
            }
            if (invoiceStmt != null) try {
                invoiceStmt.close();
            } catch (SQLException ignore) {
            }
            if (detailStmt != null) try {
                detailStmt.close();
            } catch (SQLException ignore) {
            }
            if (stockStmt != null) try {
                stockStmt.close();
            } catch (SQLException ignore) {
            }
            if (conn != null) try {
                conn.setAutoCommit(true);
                conn.close();
            } catch (SQLException ignore) {
            }
        }
    }

}
