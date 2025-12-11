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
public class UsersDB {

    private static final String LOGIN_SQL = "SELECT * FROM users WHERE username = ? AND password_hash = ?";

    public static int login(String user, String pass) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Database connection failed.");
                return -1; // Database error
            }
            ps = conn.prepareStatement(LOGIN_SQL);

            ps.setString(1, user);
            ps.setString(2, pass);

            rs = ps.executeQuery();

            if (rs.next()) {
                String status = rs.getString("status");
                SharedInformation.setUsername(rs.getString("username"));
                if ("Active".equals(status)) {
                    switch (rs.getString("role")) {
                        case "Admin" -> {
                            return 2;
                        }
                        case "Pharmacist" -> {
                            return 3;
                        }
                        case "Cashier" -> {
                            return 4;
                        }
                        default ->
                            throw new AssertionError();
                    }
                } else if ("Inactive".equals(status)) {
                    return 1;
                }
            } else {
                return 0;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;

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
        return 0;
    }

    private static final String REGISTER_SQL = "INSERT INTO users (username, password_hash, role, full_name, status) VALUES (?, ?, ?, ?, 'Inactive')";
    private static final String CHECK_USER_SQL = "SELECT 1 FROM users WHERE username = ?";

    public static int register(String user, String pass, String role) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            if (conn == null) {
                System.err.println("Database connection failed.");
                return -1;
            }

            ps = conn.prepareStatement(CHECK_USER_SQL);
            ps.setString(1, user);
            rs = ps.executeQuery();

            if (rs.next()) {
                return 0;
            }
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

            ps = conn.prepareStatement(REGISTER_SQL);

            ps.setString(1, user);
            ps.setString(2, pass);
            ps.setString(3, role);
            ps.setString(4, user);

            int rowsInserted = ps.executeUpdate();

            if (rowsInserted > 0) {
                return 1;
            } else {
                return -1;
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            return -1;

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
    }

    public static List<User> getAllUsers() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<User> users = new ArrayList<>();

        final String SELECT_ALL_SQL = "SELECT user_id, username, full_name, role, status FROM users ORDER BY username";

        try {
            conn = getConnection();
            if (conn == null) {
                return users;
            }

            ps = conn.prepareStatement(SELECT_ALL_SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getString("status")
                );
                users.add(user);
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
        return users;
    }

    public static User getUserById(int id) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        final String SQL = "SELECT user_id, username, full_name, role, status "
                + "FROM users "
                + "WHERE user_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return null;
            }

            ps = conn.prepareStatement(SQL);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return new User(
                        rs.getInt("user_id"),
                        rs.getString("username"),
                        rs.getString("full_name"),
                        rs.getString("role"),
                        rs.getString("status")
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
    
    public static boolean deleteUser(int userId) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String DELETE_SQL = "DELETE FROM users WHERE user_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(DELETE_SQL);
            ps.setInt(1, userId);

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

    public static boolean updateUser(User user) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String UPDATE_SQL = "UPDATE users SET username = ?, full_name = ?, role = ?, status = ? WHERE user_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getFullName());
            ps.setString(3, user.getRole());
            ps.setString(4, user.getAccountStatus());
            ps.setInt(5, user.getId());

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

    public static boolean updateUserStatus(int userId, String status) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String UPDATE_STATUS_SQL = "UPDATE users SET status = ? WHERE user_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(UPDATE_STATUS_SQL);
            ps.setString(1, status);
            ps.setInt(2, userId);

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
