/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package com.pharmacy.ui;

import com.pharmacy.db.InvoicesDB;
import com.pharmacy.db.MedicinesDB;
import com.pharmacy.db.MedicinesDB.Medicine;
import com.pharmacy.db.SharedInformation;
import com.pharmacy.db.UsersDB;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 *
 * @author PC
 */
public class SalesPage extends javax.swing.JFrame {

    /**
     * Creates new form ManagerDashboard
     */
    public SalesPage() {
        initComponents();
        updateTotalPrice();
        createProducts();
    }
    private double totalPrice = 0;
    private List<CartItem> cart = new ArrayList<>();
    private Timer searchTimer;
    
    public class CartItem {
        
        private Medicine medicine;
        private int quantity;
        
        public CartItem(Medicine medicine, int quantity) {
            this.medicine = medicine;
            this.quantity = quantity;
        }
        
        public Medicine getMedicine() {
            return medicine;
        }
        
        public int getQuantity() {
            return quantity;
        }
        
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        
        public double getTotalPrice() {
            return medicine.getUnitPrice() * quantity;
        }
    }
    private List<ProductTemplate> cardList = new ArrayList<>();
    
    private void createProducts() {
        jPanel2.removeAll();
        cardList.clear();
        jPanel2.setLayout(new GridLayout(0, 2, 10, 10));
        
        List<Medicine> medicines = MedicinesDB.getAllMedicines();
        
        for (Medicine med : medicines) {
            ProductTemplate card = new ProductTemplate(med, new ProductTemplate.CartListener() {
                @Override
                public void onAdd(ProductTemplate card, Medicine medicine, int quantity) {
                    addMedicineToCart(card, medicine, quantity);
                }
                
                @Override
                public void onRemove(ProductTemplate card, Medicine medicine) {
                    removeMedicineFromCart(card, medicine);
                }
            });
            jPanel2.add(card);
            cardList.add(card);
        }
        
        jPanel2.revalidate();
        jPanel2.repaint();
    }
    
    private void updateTotalPrice() {
        double total = 0.0;
        if (!cart.isEmpty()) {
            jButton9.setEnabled(true);
            for (CartItem item : cart) {
                total += item.getTotalPrice();
            }
        } else {
            
            jButton9.setEnabled(false);
        }
        totalPrice = total;
        jLabel1.setText("Total Price: " + total);
        
    }
    
    private void addMedicineToCart(ProductTemplate card, Medicine medicine, int quantity) {
        if (quantity > medicine.getStockQuantity()) {
            JOptionPane.showMessageDialog(this,
                    "You cannot add more than the available stock: " + medicine.getStockQuantity());
            return;
        }
        
        for (CartItem item : cart) {
            int totalRequested = item.getQuantity() + quantity;
            if (item.getMedicine().getMedicineId() == medicine.getMedicineId()) {
                
                if (totalRequested > medicine.getStockQuantity()) {
                    JOptionPane.showMessageDialog(this,
                            "Total quantity exceeds available stock (" + medicine.getStockQuantity() + ")");
                    return;
                }
                item.setQuantity(totalRequested);
                card.updateOrderedAmount(totalRequested);
                updateTotalPrice();
                return;
            }
        }
        
        cart.add(new CartItem(medicine, quantity));
        card.updateOrderedAmount(quantity);
        updateTotalPrice();
    }
    
    private void removeMedicineFromCart(ProductTemplate card, Medicine medicine) {
        for (CartItem item : cart) {
            if (item.getMedicine().getMedicineId() == medicine.getMedicineId()) {
                if (item.getQuantity() > 0) {
                    cart.remove(item);
                    card.updateOrderedAmount(0);
                    updateTotalPrice();
                    return;
                }
            }
        }
    }
    
    private void emptyCart() {
        cart.clear();
        createProducts();
        updateTotalPrice();
    }
    
    private void autoSearchHelper() {
        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                debounce();
            }
            
            @Override
            public void removeUpdate(DocumentEvent e) {
                debounce();
            }
            
            @Override
            public void changedUpdate(DocumentEvent e) {
                debounce();
            }
            
            private void filterProducts() {
                String text = jTextField1.getText().trim().toLowerCase();
                
                jPanel2.removeAll();
                
                for (ProductTemplate card : cardList) {
                    String medName = card.getMedicine().getMedicineName().toLowerCase();
                    
                    if (medName.contains(text)) {
                        jPanel2.add(card);
                    }
                }
                jPanel2.revalidate();
                jPanel2.repaint();
            }
            
            private void debounce() {
                if (searchTimer != null && searchTimer.isRunning()) {
                    searchTimer.restart();
                } else {
                    searchTimer = new Timer(150, evt -> {
                        searchTimer.stop();
                        filterProducts();
                    });
                    searchTimer.setRepeats(false);
                    searchTimer.start();
                }
            }
            
        });
    }
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        gradientPanel1 = new com.pharmacy.ui.GradientPanel();
        jPanel1 = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jPanel2 = new javax.swing.JPanel();
        jTextField1 = new javax.swing.JTextField();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton9 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Manager Dashboard");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        gradientPanel1.setColor1(new java.awt.Color(204, 0, 153));

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));

        jLabel4.setFont(new java.awt.Font("Tahoma", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Sales Page");

        jScrollPane1.setViewportView(jPanel2);

        jTextField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField1ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 0, 255));
        jButton8.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton8.setForeground(new java.awt.Color(255, 255, 255));
        jButton8.setText("Back");
        jButton8.setToolTipText("");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel1.setText("Total Price:");

        jButton9.setBackground(new java.awt.Color(255, 0, 255));
        jButton9.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jButton9.setForeground(new java.awt.Color(255, 255, 255));
        jButton9.setText("Next");
        jButton9.setToolTipText("");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Tahoma", 0, 14)); // NOI18N
        jLabel2.setText("Search field:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jButton8)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jButton9))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(258, 258, 258)
                        .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 266, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 277, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 80, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 200, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(15, 15, 15)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton8)
                    .addComponent(jButton9))
                .addContainerGap())
        );

        javax.swing.GroupLayout gradientPanel1Layout = new javax.swing.GroupLayout(gradientPanel1);
        gradientPanel1.setLayout(gradientPanel1Layout);
        gradientPanel1Layout.setHorizontalGroup(
            gradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, gradientPanel1Layout.createSequentialGroup()
                .addContainerGap(15, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(22, 22, 22))
        );
        gradientPanel1Layout.setVerticalGroup(
            gradientPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(gradientPanel1Layout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(gradientPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        autoSearchHelper();
    }//GEN-LAST:event_formWindowOpened

    private void jTextField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField1ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        SharedInformation.getFrame().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton8ActionPerformed
    private void createPurchasePrompt() {
        PurchasePrompt prompt = new PurchasePrompt(this, totalPrice, new PurchasePrompt.SalesListener() {
            @Override
            public void onFinish(Integer customer_id, String payment) {
                try {
                    int invoiceId = InvoicesDB.createInvoice(customer_id, UsersDB.getUserIdByUsername(SharedInformation.getUsername()), payment, cart);
                    
                    JOptionPane.showMessageDialog(SalesPage.this,
                            "Purchase Completed! Invoice ID: " + invoiceId);
                    emptyCart();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(SalesPage.this, "Error: " + ex.getMessage());
                }
            }
        });
        prompt.setVisible(true);
    }
    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        createPurchasePrompt();
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(SalesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(SalesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(SalesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(SalesPage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new SalesPage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private com.pharmacy.ui.GradientPanel gradientPanel1;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField jTextField1;
    // End of variables declaration//GEN-END:variables
}
