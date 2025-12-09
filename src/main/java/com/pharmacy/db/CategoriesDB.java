package com.pharmacy.db;

import static com.pharmacy.db.DBConnection.getConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CategoriesDB {

    public static class Category {

        private int categoryId;
        private String categoryName;
        private String description;

        public Category(int categoryId, String categoryName, String description) {
            this.categoryId = categoryId;
            this.categoryName = categoryName;
            this.description = description;
        }

        public int getCategoryId() {
            return categoryId;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public String getDescription() {
            return description;
        }

        public void setCategoryId(int categoryId) {
            this.categoryId = categoryId;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public void setDescription(String description) {
            this.description = description;
        }
    }

    public static List<Category> getAllCategories() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<Category> categories = new ArrayList<>();

        final String SELECT_ALL_SQL
                = "SELECT category_id, category_name, description FROM categories ORDER BY category_name";

        try {
            conn = getConnection();
            if (conn == null) {
                return categories;
            }

            ps = conn.prepareStatement(SELECT_ALL_SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                Category category = new Category(
                        rs.getInt("category_id"),
                        rs.getString("category_name"),
                        rs.getString("description")
                );
                categories.add(category);
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

        return categories;
    }

    public static List<String> getAllCategoriesNames() {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        List<String> categories = new ArrayList<>();

        final String SELECT_ALL_SQL
                = "SELECT ccategory_name FROM categories";

        try {
            conn = getConnection();
            if (conn == null) {
                return categories;
            }

            ps = conn.prepareStatement(SELECT_ALL_SQL);
            rs = ps.executeQuery();

            while (rs.next()) {
                categories.add(rs.getString("category_name"));
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
        return categories;
    }

    ;

    public static int getCategoryIdByName(String categoryName) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        final String SQL = "SELECT category_id FROM categories WHERE category_name = ?";

        try {
            conn = getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setString(1, categoryName);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getInt("category_id");
            }

        } catch (Exception e) {
            e.printStackTrace();
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

        return -1;
    }

    public static String getCategoryNameById(int categoryId) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        final String SQL = "SELECT category_name FROM categories WHERE category_id = ?";

        try {
            conn = getConnection();
            ps = conn.prepareStatement(SQL);
            ps.setInt(1, categoryId);
            rs = ps.executeQuery();

            if (rs.next()) {
                return rs.getString("category_name");
            }

        } catch (Exception e) {
            e.printStackTrace();
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

    public static boolean addCategory(Category category) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String INSERT_SQL
                = "INSERT INTO categories (category_name, description) VALUES (?, ?)";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(INSERT_SQL);
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());

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

    public static boolean updateCategory(Category category) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String UPDATE_SQL
                = "UPDATE categories SET category_name = ?, description = ? WHERE category_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(UPDATE_SQL);
            ps.setString(1, category.getCategoryName());
            ps.setString(2, category.getDescription());
            ps.setInt(3, category.getCategoryId());

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

    public static boolean deleteCategory(int categoryId) {
        Connection conn = null;
        PreparedStatement ps = null;

        final String DELETE_SQL
                = "DELETE FROM categories WHERE category_id = ?";

        try {
            conn = getConnection();
            if (conn == null) {
                return false;
            }

            ps = conn.prepareStatement(DELETE_SQL);
            ps.setInt(1, categoryId);

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
