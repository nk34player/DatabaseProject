/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.pharmacy.db;

import static com.pharmacy.db.DBConnection.getConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

        private List<InvoiceDetail> details;  // <-- Already loaded!

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

            conn.setAutoCommit(false); // START TRANSACTION

            // Step 1: Delete all invoice details
            psDetails = conn.prepareStatement(DELETE_DETAILS_SQL);
            psDetails.setInt(1, invoiceId);
            psDetails.executeUpdate();

            // Step 2: Delete invoice row
            psInvoice = conn.prepareStatement(DELETE_INVOICE_SQL);
            psInvoice.setInt(1, invoiceId);
            int invoiceDeleted = psInvoice.executeUpdate();

            // If the invoice was actually deleted, commit
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
}
