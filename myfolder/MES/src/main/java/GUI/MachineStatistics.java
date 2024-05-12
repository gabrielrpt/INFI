/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import org.OPC_UA.OPCUAClient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.time.Duration;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author gabri
 */
public class MachineStatistics extends javax.swing.JFrame {
     OPCUAClient client;
    String machine1Time;
    String machine2Time;
    String machine3Time;
    String machine4Time;
    String machine5Time;
    String machine6Time;
    String machine7Time;
    String machine8Time;
    String machine9Time;
    String machine10Time;
    String machine11Time;
    String machine12Time;

    ScheduledExecutorService executorService;


    public MachineStatistics() {
        // Create an instance of OPCUAClient
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
        executorService.scheduleAtFixedRate(this::updateMachineTimes, 0, 1, TimeUnit.SECONDS);
    }

    private void updateMachineTimes() {
        Duration machine1Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M11.timer", 4);
        Duration machine2Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M21.timer", 4);
        Duration machine3Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M12.timer", 4);
        Duration machine4Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M22.timer", 4);
        Duration machine5Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M13.timer", 4);
        Duration machine6Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M23.timer", 4);
        Duration machine7Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M14.timer", 4);
        Duration machine8Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M24.timer", 4);
        Duration machine9Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M15.timer", 4);
        Duration machine10Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M25.timer", 4);
        Duration machine11Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M16.timer", 4);
        Duration machine12Timer = client.readTime("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M26.timer", 4);

         machine1Time = formatDuration(machine1Timer);
         machine2Time = formatDuration(machine2Timer);
         machine3Time = formatDuration(machine3Timer);
         machine4Time = formatDuration(machine4Timer);
         machine5Time = formatDuration(machine5Timer);
         machine6Time = formatDuration(machine6Timer);
         machine7Time = formatDuration(machine7Timer);
         machine8Time = formatDuration(machine8Timer);
         machine9Time = formatDuration(machine9Timer);
         machine10Time = formatDuration(machine10Timer);
         machine11Time = formatDuration(machine11Timer);
         machine12Time = formatDuration(machine12Timer);
         updateTable1();
         updateTable2();
         updateTable3();
         updateTable4();
         updateTable5();
         updateTable7();
    }


    public void updateTable1() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine11Time, i, 1);
                    break;
                case "4":
                    model.setValueAt(machine12Time, i, 1);
                    break;

            }
        }
    }
    public void updateTable2() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable2.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "1":
                    model.setValueAt(machine1Time, i, 1);
                    break;
                case "2":
                    model.setValueAt(machine2Time, i, 1);
                    break;

            }
        }
    }

    public void updateTable3() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable3.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "1":
                    model.setValueAt(machine5Time, i, 1);
                    break;
                case "2":
                    model.setValueAt(machine6Time, i, 1);
                    break;
            }
        }
    }

    public void updateTable4() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable4.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "1":
                    model.setValueAt(machine3Time, i, 1);
                    break;
                case "2":
                    model.setValueAt(machine4Time, i, 1);
                    break;
            }
        }
    }

    public void updateTable5() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable5.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine7Time, i, 1);
                    break;
                case "4":
                    model.setValueAt(machine8Time, i, 1);
                    break;
            }
        }
    }

    public void updateTable7() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable7.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine9Time, i, 1);
                    break;
                case "4":
                    model.setValueAt(machine10Time, i, 1);
                    break;
            }
        }
    }




    public String formatDuration(Duration duration) {
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }


public void currentDate(){
        Calendar cal= new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        int year= cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
       
        jLabel9.setText(day+"/"+(month+1)+"/" + year);
    }

    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        jTable4 = new javax.swing.JTable();
        jScrollPane5 = new javax.swing.JScrollPane();
        jTable5 = new javax.swing.JTable();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        jTable7 = new javax.swing.JTable();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1200, 500));
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 30)); // NOI18N
        jLabel1.setText("Machine Statistics");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(440, 20, 400, 36);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/25694.png"))); // NOI18N
        jButton1.setText("Return to Main Menu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(80, 380, 400, 50);

        jTable1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", machine10Time, null},
                {"4", machine11Time, null}
            },
            new String [] {
                "Machine Type ", "Total Operating Time", "Number of WP Produced (Total)"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jTable1.setShowGrid(true);
        jScrollPane1.setViewportView(jTable1);

        getContentPane().add(jScrollPane1);
        jScrollPane1.setBounds(580, 270, 550, 60);

        jTable2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", machine3Time, null},
                {"2", machine4Time, null}
            },
            new String [] {
                "Machine Type ", "Total Operating Time", "Number of WP Produced (Total)"
            }
        ));
        jTable2.setGridColor(new java.awt.Color(51, 51, 51));
        jTable2.setShowGrid(true);
        jScrollPane2.setViewportView(jTable2);

        getContentPane().add(jScrollPane2);
        jScrollPane2.setBounds(20, 90, 550, 60);

        jTable3.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", machine5Time, null},
                {"2", machine6Time, null}
            },
            new String [] {
                "Machine Type ", "Total Operating Time", "Number of WP Produced (Total)"
            }
        ));
        jTable3.setGridColor(new java.awt.Color(0, 0, 0));
        jTable3.setShowGrid(true);
        jScrollPane3.setViewportView(jTable3);

        getContentPane().add(jScrollPane3);
        jScrollPane3.setBounds(20, 270, 550, 60);

        jTable4.setBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 0, 0), 1, true));
        jTable4.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"1", machine3Time, null},
                {"2", machine4Time, null}
            },
            new String [] {
                "Machine Type ", "Total Operating Time", "Number of WP Produced (Total)"
            }
        ));
        jTable4.setGridColor(new java.awt.Color(0, 0, 0));
        jTable4.setShowGrid(true);
        jScrollPane4.setViewportView(jTable4);

        getContentPane().add(jScrollPane4);
        jScrollPane4.setBounds(20, 180, 550, 60);

        jTable5.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable5.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", machine7Time, null},
                {"4", machine8Time, null}
            },
            new String [] {
                "Machine Type ", "Total Operating Time", "Number of WP Produced (Total)"
            }
        ));
        jTable5.setGridColor(new java.awt.Color(0, 0, 0));
        jTable5.setInheritsPopupMenu(true);
        jTable5.setShowGrid(true);
        jScrollPane5.setViewportView(jTable5);

        getContentPane().add(jScrollPane5);
        jScrollPane5.setBounds(580, 90, 550, 60);

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel3.setText("Machining Cell 1");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(230, 60, 170, 40);

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel4.setText("Machining Cell 2");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(230, 160, 190, 20);

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel5.setText("Machining Cell 3");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(230, 240, 170, 40);

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel6.setText("Machining Cell 4");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(780, 70, 200, 21);

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel7.setText("Machining Cell 6");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(780, 250, 180, 20);

        jTable7.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));
        jTable7.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", machine9Time, null},
                {"4", machine10Time, null}
            },
            new String [] {
                "Machine Type ", "Total Operating Time", "Number of WP Produced (Total)"
            }
        ));
        jTable7.setGridColor(new java.awt.Color(0, 0, 0));
        jTable7.setShowGrid(true);
        jScrollPane7.setViewportView(jTable7);

        getContentPane().add(jScrollPane7);
        jScrollPane7.setBounds(580, 180, 550, 60);

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel8.setText("Machining Cell 5");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(780, 160, 200, 21);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/65000.png"))); // NOI18N
        jButton2.setText("See Work Pieces Produced By Type");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(690, 380, 400, 50);

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel9.setText("jLabel9");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(1090, 20, 110, 17);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/colorkit2.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 1200, 520);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        MainMenu mainmenu = new MainMenu();
        mainmenu.setVisible(true);
       // this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        Sub_MachineStats submachinestats = new Sub_MachineStats();
        submachinestats.setVisible(true);
        this.dispose();    }//GEN-LAST:event_jButton2ActionPerformed

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
            java.util.logging.Logger.getLogger(MachineStatistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MachineStatistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MachineStatistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MachineStatistics.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MachineStatistics().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTable jTable4;
    private javax.swing.JTable jTable5;
    private javax.swing.JTable jTable7;
    // End of variables declaration//GEN-END:variables
}
