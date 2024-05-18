/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import org.OPC_UA.OPCUAClient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author gabri
 */
public class UnloadingDocks extends javax.swing.JFrame {
    OPCUAClient client;
    int unl1P5, unl1P6, unl1P7, unl1P9, P5Total,
    unl2P5, unl2P6, unl2P7, unl2P9, P6Total,
    unl3P5, unl3P6, unl3P7, unl3P9, P7Total,
    unl4P5, unl4P6, unl4P7, unl4P9, P9Total;
    ScheduledExecutorService executorService;
    /**
     * Creates new form UnloadingDocks
     */
    public UnloadingDocks() {
        client = new OPCUAClient();

        try {
            client.connect("opc.tcp://localhost:4840");
        } catch (Exception e) {
            e.printStackTrace();
            // handle the exception
        }
        initComponents();
       Toolkit toolkit = getToolkit();
        Dimension size= toolkit.getScreenSize();
        setLocation(size.width/2 - getWidth()/2,size.height/2-getHeight()/2);
        currentDate();
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(this::updateStatistic, 0, 1, TimeUnit.SECONDS);
    }
public void currentDate(){
        Calendar cal= new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        int year= cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
       
       jLabel3.setText(day+"/"+(month+1)+"/" + year);
    }
    private void updateStatistic(){
        unl1P5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C17.npeca5", 4);
        unl1P6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C17.npeca6", 4);
        unl1P7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C17.npeca7", 4);
        unl1P9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C17.npeca9", 4);
        P5Total = client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.UPieceTotal[0]",4);
        unl2P5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C18.npeca5", 4);
        unl2P6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C18.npeca6", 4);
        unl2P7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C18.npeca7", 4);
        unl2P9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C18.npeca9", 4);
        P6Total = client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.UPieceTotal[1]",4);
        unl3P5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C19.npeca5", 4);
        unl3P6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C19.npeca6", 4);
        unl3P7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C19.npeca7", 4);
        unl3P9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C19.npeca9", 4);
        P7Total = client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.UPieceTotal[2]",4);
        unl4P5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C20.npeca5", 4);
        unl4P6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C20.npeca6", 4);
        unl4P7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C20.npeca7", 4);
        unl4P9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.C20.npeca9", 4);
        P9Total = client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.UPieceTotal[3]",4);
        updateTable();
    }

    public void updateTable(){
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "5":
                    model.setValueAt(unl1P5, i, 1);
                    model.setValueAt(unl2P5, i, 2);
                    model.setValueAt(unl3P5, i, 3);
                    model.setValueAt(unl4P5, i, 4);
                    model.setValueAt(P5Total, i, 5);
                    break;
                case "6":
                    model.setValueAt(unl1P6, i, 1);
                    model.setValueAt(unl2P6, i, 2);
                    model.setValueAt(unl3P6, i, 3);
                    model.setValueAt(unl4P6, i, 4);
                    model.setValueAt(P6Total, i, 5);
                    break;
                case "7":
                    model.setValueAt(unl1P7, i, 1);
                    model.setValueAt(unl2P7, i, 2);
                    model.setValueAt(unl3P7, i, 3);
                    model.setValueAt(unl4P7, i, 4);
                    model.setValueAt(P7Total, i, 5);
                    break;
                case "9":
                    model.setValueAt(unl1P9, i, 1);
                    model.setValueAt(unl2P9, i, 2);
                    model.setValueAt(unl3P9, i, 3);
                    model.setValueAt(unl4P9, i, 4);
                    model.setValueAt(P9Total, i, 5);
                    break;
            }
        }
    }
    
    
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jButton3 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        jButton2.setText("Return to Main Menu");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(850, 300));
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 24)); // NOI18N
        jLabel1.setText("Unloading Docks Status");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(290, 10, 323, 29);

        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/25694.png"))); // NOI18N
        jButton3.setText("Return to Main Menu");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(250, 190, 350, 40);

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"5", null, null, null, null, null},
                {"6", null, null, null, null, null},
                {"7", null, null, null, null, null},
                {"9", null, null, null, null, null}
            },
            new String [] {
                "Piece Type", "Unloading Dock 1", "Unloading Dock 2", "Unloading Dock 3", "Unloading Dock 4", "Total "
            }
        ));
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(60, 70, 710, 90);

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel3.setText("jLabel3");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(730, 10, 130, 16);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/colorkit2.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 880, 270);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        MainMenu mainmenu = new MainMenu();
        mainmenu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        MainMenu mainmenu = new MainMenu();
        mainmenu.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

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
            java.util.logging.Logger.getLogger(UnloadingDocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(UnloadingDocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(UnloadingDocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(UnloadingDocks.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new UnloadingDocks().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;
    // End of variables declaration//GEN-END:variables
}
