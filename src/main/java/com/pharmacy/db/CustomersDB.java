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

public class CustomersDB {

    public static class Customer {

        private int customerId;
        private String fullName;
        private String phone;
        private String email;
        private String streetAddress;
        private String city;
        private String registeredDate;

        public Customer(int customerId, String fullName, String phone, String email,
                String streetAddress, String city, String registeredDate) {

            this.customerId = customerId;
            this.fullName = fullName;
            this.phone = phone;
            this.email = email;
            this.streetAddress = streetAddress;
            this.city = city;
            this.registeredDate = registeredDate;
        }

        public int getCustomerId() {
            return customerId;
        }

        public String getFullName() {
            return fullName;
        }

        public String getPhone() {
            return phone;
        }

        public String getEmail() {
            return email;
        }

        public String getStreetAddress() {
            return streetAddress;
        }

        public String getCity() {
            return city;
        }

        public String getRegisteredDate() {
            return registeredDate;
        }

        public void setCustomerId(int customerId) {
            this.customerId = customerId;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public void setStreetAddress(String streetAddress) {
            this.streetAddress = streetAddress;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public void setRegisteredDate(String registeredDate) {
            this.registeredDate = registeredDate;
        }
    }

    public static List<Customer> getAllCustomers() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Customer> customers = new ArrayList<>();

        final String SELECT_ALL_SQL
                = "SELECT customer_id, full_name, phone, email, street_address, city, registered_date "
                + "FROM customers ORDER BY full_name";

        try {
            conn = getConnection();
            if (conn == null) {
                return customers;
            }

            ps = conn.prepareStatement(SELECT_ALL_SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Customer c = new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("street_address"),
                        rs.getString("city"),
                        rs.getString("registered_date")
                );
                customers.add(c);
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

        return customers;
    }

    public static Customer getCustomerById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        final String SQL = "SELECT customer_id, full_name, phone, email, street_address, city, registered_date "
                + "FROM customers WHERE customer_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return null;
            }

            ps = conn.prepareStatement(SQL);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new Customer(
                        rs.getInt("customer_id"),
                        rs.getString("full_name"),
                        rs.getString("phone"),
                        rs.getString("email"),
                        rs.getString("street_address"),
                        rs.getString("city"),
                        rs.getString("registered_date")
                );
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

        return null;
    }

    public static boolean addCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String INSERT_SQL
                = "INSERT INTO customers (full_name, phone, email, street_address, city) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(INSERT_SQL);
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getStreetAddress());
            ps.setString(5, customer.getCity());

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

    public static boolean updateCustomer(Customer customer) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String UPDATE_SQL
                = "UPDATE customers SET full_name = ?, phone = ?, email = ?, "
                + "street_address = ?, city = ? WHERE customer_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getPhone());
            ps.setString(3, customer.getEmail());
            ps.setString(4, customer.getStreetAddress());
            ps.setString(5, customer.getCity());
            ps.setInt(6, customer.getCustomerId());

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

    public static boolean deleteCustomer(int customerId) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String DELETE_SQL
                = "DELETE FROM customers WHERE customer_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(DELETE_SQL);
            ps.setInt(1, customerId);

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
