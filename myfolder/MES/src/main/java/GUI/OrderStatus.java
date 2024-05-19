/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package GUI;

import Logic.Orders;
import database.javaDatabase;
import org.OPC_UA.OPCUAClient;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author gabri
 */
public class OrderStatus extends javax.swing.JFrame{
    OPCUAClient client;
    /**
     * Creates new form OrderRequests
     */
    public OrderStatus() {
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
        AtomicInteger prodDay = new AtomicInteger(0);
        List<Orders> orderList = new ArrayList<>();
        Thread orderUpdatingThread = new Thread(() -> {
            while (true) {
                orderCompletedUpdates();
                getOrdersByProdDay(prodDay.intValue(), orderList);
                try {
                    Thread.sleep(60000); // Sleep for 60 seconds
                    prodDay.getAndIncrement(); // Increment the production day
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        orderUpdatingThread.start();
    }
    public void getOrdersByProdDay(int prodDay, List<Orders> orderList) {
        try {
            boolean flag = true;
            System.out.println(prodDay);
            ResultSet rs = javaDatabase.getOrdersByProdDay(prodDay);
            while (rs.next()) {
                String orderNumber = rs.getString("ordernumber");
                String workPiece = rs.getString("workpiece");
                int quantity = rs.getInt("quantity");
                int dueDate = rs.getInt("duedate");
                double latePenalty = rs.getDouble("latepen");
                double earlyPenalty = rs.getDouble("earlypen");
                int productionDay = rs.getInt("productionday");

                ResultSet prs = javaDatabase.getPieceByOrderNumber(orderNumber);
                prs.next();
                String rawPiece = prs.getString("rawpiece");

                //print the orders
                System.out.println("Order Number: " + orderNumber + " Work Piece: " + workPiece + " Quantity: " + quantity + " Due Date: " + dueDate + " Late Penalty: " + latePenalty + " Early Penalty: " + earlyPenalty + " Production Day: " + productionDay);


                //iterate over the orderList and check if the order is already in the list
                for (Orders order : orderList) {
                    if (order.getOrderNumber().equals(orderNumber)) {
                        flag = false;
                        break;
                    }
                }
                //Add the order stats to the GUI and to the Codesys
                if (flag) {
                    Orders order = new Orders(orderNumber, workPiece, rawPiece, quantity, dueDate, latePenalty, earlyPenalty, productionDay);
                    orderList.add(order);
                    for (int i=0; i<11; i++){
                        if(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId["+i+"]", 4)==0){
                            client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId["+i+"]", 4, order.getOrderNumber());
                            client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.PendingPieces["+i+"]", 4, String.valueOf(order.getQuantity()));
                            break;
                        }
                    }
                    createOrderStatus(order);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Organiza as ordens na GUI e no Codesys após completar a ordem de maior prioridade
    public void orderCompletedUpdates (){
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        if (client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.Completed", 4)==1){ //algum sinal para avisar que a ordem já foi entregue
            for(int i=0; i<(model.getRowCount()-1); i++){
                model.setValueAt(model.getValueAt(i+1, 0), i, 0);
                model.setValueAt(model.getValueAt(i+1, 1), i, 1);
                model.setValueAt(model.getValueAt(i+1, 2), i, 2);
                model.setValueAt(model.getValueAt(i+1, 3), i, 3);
                model.setValueAt(model.getValueAt(i+1, 4), i, 4);

            }
            model.setValueAt(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId["+4+"]", 4), 3, 0);
            model.setValueAt(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.PendingPieces["+4+"]", 4), 3, 1);
            model.setValueAt(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.CompletedPieces["+4+"]", 4), 3, 2);
            model.setValueAt(client.readTime("|var|CODESYS Control Win V3 x64.Application.GVL.OrderDuration["+4+"]", 4), 3, 3);

            model.setValueAt("In Progress", 3, 4);
            client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.Completed", 4, String.valueOf(0));
            for(int i=0; i<10; i++){
                client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId["+i+"]", 4,
                        String.valueOf(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.OrderId["+(i+1)+"]", 4)));
                client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.PendingPieces["+i+"]", 4,
                        String.valueOf(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.PendingPieces["+(i+1)+"]", 4)));
                client.writeInt16("|var|CODESYS Control Win V3 x64.Application.GVL.CompletedPieces["+i+"]", 4,
                        String.valueOf(client.readInt16("|var|CODESYS Control Win V3 x64.Application.GVL.CompletedPieces["+(i+1)+"]", 4)));
                //Fazer para o timer também
            }
        }
    }

    public void createOrderStatus(Orders order){
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        for(int i=0; i<model.getRowCount(); i++){
            if (model.getValueAt(i, 0)== null ){
                model.setValueAt(order.getOrderNumber(), i, 0);
                model.setValueAt(0, i, 1);
                model.setValueAt(order.getQuantity(), i, 2);
                model.setValueAt(0, i, 3);
                model.setValueAt("In Progress", i, 4);
                break;
            }
        }
    }


    public void currentDate(){
        Calendar cal= new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        int year= cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
       
        jLabel2.setText(day+"/"+(month+1)+"/" + year);
    }
    public void updatePiecesAndTime(Orders order){
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable1.getModel();
        for(int i=0; i<model.getRowCount(); i++){
            if (model.getValueAt(i, 0)== order.getOrderNumber()){

            }
        }
    }
    public void updateStatus(Orders order){

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1000, 300));
        setPreferredSize(new java.awt.Dimension(1000, 300));

        jPanel1.setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 30)); // NOI18N
        jLabel1.setText("Order Status");
        jPanel1.add(jLabel1);
        jLabel1.setBounds(400, 20, 210, 29);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/25694.png"))); // NOI18N
        jButton1.setText("Return to Main Menu");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel1.add(jButton1);
        jButton1.setBounds(390, 220, 229, 32);

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Order ID", "Completed Pieces", "Pending Pieces", "Order Duration", "Order Status"
            }
        ));
        jTable1.setGridColor(new java.awt.Color(0, 0, 0));
        jTable1.setRowHeight(25);
        jTable1.setShowGrid(true);
        jTable1.setSurrendersFocusOnKeystroke(true);
        jScrollPane1.setViewportView(jTable1);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(140, 70, 700, 130);

        jLabel2.setBackground(new java.awt.Color(0, 0, 0));
        jLabel2.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel2.setText("jLabel2");
        jPanel1.add(jLabel2);
        jLabel2.setBounds(870, 10, 130, 16);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/priority_2-512 (2).png"))); // NOI18N
        jPanel1.add(jLabel5);
        jLabel5.setBounds(820, 150, 100, 50);

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 12)); // NOI18N
        jLabel4.setText("Priority");
        jPanel1.add(jLabel4);
        jLabel4.setBounds(890, 165, 70, 20);

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/colorkit2.png"))); // NOI18N
        jLabel3.setText("jLabel3");
        jPanel1.add(jLabel3);
        jLabel3.setBounds(-70, 0, 1050, 330);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1080, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(81, 81, 81))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 309, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(332, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed

        MainMenu mainmenu = new MainMenu();
        mainmenu.setVisible(true);
        //this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

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
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(OrderStatus.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new OrderStatus().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable jTable1;


    // End of variables declaration//GEN-END:variables
}
