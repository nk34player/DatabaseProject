package com.pharmacy.db;

import static com.pharmacy.db.DBConnection.getConnection;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class MedicinesDB {

    public static class Medicine {

        private int medicineId;
        private int categoryId;
        private String medicineName;
        private String batchNumber;
        private String companyName;
        private double unitPrice;
        private int stockQuantity;
        private Date expiryDate;
        private Date manufacturingDate;

        public Medicine(int medicineId, int categoryId, String medicineName,
                String batchNumber, String companyName, double unitPrice,
                int stockQuantity, Date expiryDate, Date manufacturingDate) {

            this.medicineId = medicineId;
            this.categoryId = categoryId;
            this.medicineName = medicineName;
            this.batchNumber = batchNumber;
            this.companyName = companyName;
            this.unitPrice = unitPrice;
            this.stockQuantity = stockQuantity;
            this.expiryDate = expiryDate;
            this.manufacturingDate = manufacturingDate;
        }

        public int getMedicineId() {
            return medicineId;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getMedicineName() {
            return medicineName;
        }

        public String getBatchNumber() {
            return batchNumber;
        }

        public String getCompanyName() {
            return companyName;
        }

        public double getUnitPrice() {
            return unitPrice;
        }

        public int getStockQuantity() {
            return stockQuantity;
        }

        public Date getExpiryDate() {
            return expiryDate;
        }

        public Date getManufacturingDate() {
            return manufacturingDate;
        }

        public void setMedicineId(int medicineId) {
            this.medicineId = medicineId;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public void setMedicineName(String medicineName) {
            this.medicineName = medicineName;
        }

        public void setBatchNumber(String batchNumber) {
            this.batchNumber = batchNumber;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public void setUnitPrice(double unitPrice) {
            this.unitPrice = unitPrice;
        }

        public void setStockQuantity(int stockQuantity) {
            this.stockQuantity = stockQuantity;
        }

        public void setExpiryDate(Date expiryDate) {
            this.expiryDate = expiryDate;
        }

        public void setManufacturingDate(Date manufacturingDate) {
            this.manufacturingDate = manufacturingDate;
        }
    }
    
    public static List<Medicine> getAllMedicines() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Medicine> medicines = new ArrayList<>();

        final String SELECT_ALL_SQL =
            "SELECT medicine_id, category_id, medicine_name, batch_number, " +
            "company_name, unit_price, stock_quantity, expiry_date, manufacturing_date " +
            "FROM medicines";

        try {
            conn = getConnection();
            if (conn == null) return medicines;

            ps = conn.prepareStatement(SELECT_ALL_SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Medicine m = new Medicine(
                        rs.getInt("medicine_id"),
                        rs.getInt("category_id"),
                        rs.getString("medicine_name"),
                        rs.getString("batch_number"),
                        rs.getString("company_name"),
                        rs.getDouble("unit_price"),
                        rs.getInt("stock_quantity"),
                        rs.getDate("expiry_date"),
                        rs.getDate("manufacturing_date")
                );
                medicines.add(m);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception e) {}
            try { if (ps != null) ps.close(); } catch (Exception e) {}
            try { if (conn != null) conn.close(); } catch (Exception e) {}
        }

        return medicines;
    }

    public static boolean addMedicine(Medicine med) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String INSERT_SQL
                = "INSERT INTO medicines (category_id, medicine_name, batch_number, "
                + "company_name, unit_price, stock_quantity, expiry_date, manufacturing_date) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(INSERT_SQL);
            ps.setInt(1, med.getCategoryId());
            ps.setString(2, med.getMedicineName());
            ps.setString(3, med.getBatchNumber());
            ps.setString(4, med.getCompanyName());
            ps.setDouble(5, med.getUnitPrice());
            ps.setInt(6, med.getStockQuantity());
            ps.setDate(7, med.getExpiryDate());
            ps.setDate(8, med.getManufacturingDate());

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) {
                    ps.close();
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

    public static boolean updateMedicine(Medicine med) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String UPDATE_SQL
                = "UPDATE medicines SET category_id = ?, medicine_name = ?, batch_number = ?, "
                + "company_name = ?, unit_price = ?, stock_quantity = ?, expiry_date = ?, "
                + "manufacturing_date = ? WHERE medicine_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(UPDATE_SQL);
            ps.setInt(1, med.getCategoryId());
            ps.setString(2, med.getMedicineName());
            ps.setString(3, med.getBatchNumber());
            ps.setString(4, med.getCompanyName());
            ps.setDouble(5, med.getUnitPrice());
            ps.setInt(6, med.getStockQuantity());
            ps.setDate(7, med.getExpiryDate());
            ps.setDate(8, med.getManufacturingDate());
            ps.setInt(9, med.getMedicineId());

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) {
                    ps.close();
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

    public static boolean deleteMedicine(int medicineId) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String DELETE_SQL
                = "DELETE FROM medicines WHERE medicine_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(DELETE_SQL);
            ps.setInt(1, medicineId);

            return ps.executeUpdate() > 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            return false;

        } finally {
            try {
                if (ps != null) {
                    ps.close();
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
