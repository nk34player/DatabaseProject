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

/**
 *
 * @author PC
 */
public class SuppliersDB {

    public static class Supplier {

        private int supplierId;
        private String supplierName;
        private String companyName;
        private String email;
        private String phone;
        private String address;
        private boolean isActive;

        public Supplier(int supplierId, String supplierName, String companyName,
                String email, String phone, String address, boolean isActive) {
            this.supplierId = supplierId;
            this.supplierName = supplierName;
            this.companyName = companyName;
            this.email = email;
            this.phone = phone;
            this.address = address;
            this.isActive = isActive;
        }

        public int getSupplierId() {
            return supplierId;
        }

        public String getSupplierName() {
            return supplierName;
        }

        public String getCompanyName() {
            return companyName;
        }

        public String getEmail() {
            return email;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public boolean isActive() {
            return isActive;
        }

        public void setSupplierId(int supplierId) {
            this.supplierId = supplierId;
        }

        public void setSupplierName(String supplierName) {
            this.supplierName = supplierName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setActive(boolean active) {
            isActive = active;
        }

        public String getStatusString() {
            return isActive ? "Active" : "Inactive";
        }
    }

    public static List<Supplier> getAllSuppliers() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Supplier> suppliers = new ArrayList<>();

        final String SELECT_ALL_SQL = "SELECT supplier_id, supplier_name, company_name, email, phone, address, is_active FROM suppliers ORDER BY supplier_name";

        try {
            conn = getConnection();
            if (conn == null) {
                return suppliers;
            }

            ps = conn.prepareStatement(SELECT_ALL_SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Supplier supplier = new Supplier(
                        rs.getInt("supplier_id"),
                        rs.getString("supplier_name"),
                        rs.getString("company_name"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("address"),
                        rs.getBoolean("is_active")
                );
                suppliers.add(supplier);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
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

        return suppliers;
    }

    public static boolean deleteSupplier(int supplierId) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String DELETE_SQL = "DELETE FROM suppliers WHERE supplier_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(DELETE_SQL);
            ps.setInt(1, supplierId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

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

    public static boolean updateSupplier(Supplier supplier) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String UPDATE_SQL = "UPDATE suppliers SET supplier_name = ?, company_name = ?, email = ?, phone = ?, address = ?, is_active = ? WHERE supplier_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getCompanyName());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhone());
            ps.setString(5, supplier.getAddress());
            ps.setBoolean(6, supplier.isActive());
            ps.setInt(7, supplier.getSupplierId());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

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

    public static boolean updateSupplierStatus(int supplierId, boolean isActive) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String UPDATE_STATUS_SQL = "UPDATE suppliers SET is_active = ? WHERE supplier_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(UPDATE_STATUS_SQL);
            ps.setBoolean(1, isActive);
            ps.setInt(2, supplierId);

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

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

    public static boolean addSupplier(Supplier supplier) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String INSERT_SQL = "INSERT INTO suppliers (supplier_name, company_name, email, phone, address, is_active) VALUES (?, ?, ?, ?, ?, ?)";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(INSERT_SQL);
            ps.setString(1, supplier.getSupplierName());
            ps.setString(2, supplier.getCompanyName());
            ps.setString(3, supplier.getEmail());
            ps.setString(4, supplier.getPhone());
            ps.setString(5, supplier.getAddress());
            ps.setBoolean(6, supplier.isActive());

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

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
