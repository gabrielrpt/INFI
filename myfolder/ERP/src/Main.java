
import GUI.MainMenu;

import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
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
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
        ServerERP_MES server = new ServerERP_MES();
        OrderManagement orderManagement = new OrderManagement();
        orderManagement.server = server;
        // Start the server in a separate thread
        Thread serverThread = new Thread(server::connection);
        serverThread.start();
        // Start the order receiver in a separate thread
        Thread orderManagementThread = new Thread(orderManagement::orderManagement);
        orderManagementThread.start();
        // Start the order completion checker in a separate thread
        Thread orderCompletionThread = new Thread(() -> {
                try {
                    orderManagement.checkOrderCompletion();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
        });
        orderCompletionThread.start();
    }
}
