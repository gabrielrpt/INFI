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
public class Sub_MachineStats extends javax.swing.JFrame {
    OPCUAClient client;

    ScheduledExecutorService executorService;
    int machine1Type3, machine1Type4, machine1Type6, machine1Type7, machine1Type8,
            machine2Type3, machine2Type4, machine2Type6, machine2Type7, machine2Type8,
            machine3Type3, machine3Type4, machine3Type6, machine3Type7, machine3Type8,
            machine4Type3, machine4Type4, machine4Type6, machine4Type7, machine4Type8,
            machine5Type3, machine5Type4, machine5Type6, machine5Type7, machine5Type8,
            machine6Type3, machine6Type4, machine6Type6, machine6Type7, machine6Type8;
    int machine7Type3, machine7Type5, machine7Type7, machine7Type8,  machine7Type9,
            machine8Type3, machine8Type5, machine8Type7, machine8Type8, machine8Type9,
            machine9Type3, machine9Type5, machine9Type7, machine9Type8, machine9Type9,
            machine10Type3, machine10Type5, machine10Type7, machine10Type8, machine10Type9,
            machine11Type3, machine11Type5, machine11Type7, machine11Type8, machine11Type9,
            machine12Type3, machine12Type5, machine12Type7, machine12Type8, machine12Type9;

    /**
     * Creates new form Sub_MachineStats
     */
    public Sub_MachineStats() {
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
        executorService.scheduleAtFixedRate(this::updateMachinePerPieceTotals, 0, 1, TimeUnit.SECONDS);
    }

    private void updateMachinePerPieceTotals(){
        machine1Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M11.n_peca3", 4);
        machine1Type4 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M11.n_peca4", 4);
        machine1Type6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M11.n_peca6", 4);
        machine1Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M11.n_peca7", 4);
        machine1Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M11.n_peca8", 4);
        machine2Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M21.n_peca3", 4);
        machine2Type4 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M21.n_peca4", 4);
        machine2Type6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M21.n_peca6", 4);
        machine2Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M21.n_peca7", 4);
        machine2Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M21.n_peca8", 4);
        machine3Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M12.n_peca3", 4);
        machine3Type4 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M12.n_peca4", 4);
        machine3Type6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M12.n_peca6", 4);
        machine3Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M12.n_peca7", 4);
        machine3Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M12.n_peca8", 4);
        machine4Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M22.n_peca3", 4);
        machine4Type4 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M22.n_peca4", 4);
        machine4Type6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M22.n_peca6", 4);
        machine4Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M22.n_peca7", 4);
        machine4Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M22.n_peca8", 4);
        machine5Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M13.n_peca3", 4);
        machine5Type4 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M13.n_peca4", 4);
        machine5Type6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M13.n_peca6", 4);
        machine5Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M13.n_peca7", 4);
        machine5Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M13.n_peca8", 4);
        machine6Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M23.n_peca3", 4);
        machine6Type4 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M23.n_peca4", 4);
        machine6Type6 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M23.n_peca6", 4);
        machine6Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M23.n_peca7", 4);
        machine6Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M23.n_peca8", 4);
        machine7Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M14.n_peca3", 4);
        machine7Type5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M14.n_peca5", 4);
        machine7Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M14.n_peca7", 4);
        machine7Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M14.n_peca8", 4);
        machine7Type9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M14.n_peca9", 4);
        machine8Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M24.n_peca3", 4);
        machine8Type5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M24.n_peca5", 4);
        machine8Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M24.n_peca7", 4);
        machine8Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M24.n_peca8", 4);
        machine8Type9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M24.n_peca9", 4);
        machine9Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M15.n_peca3", 4);
        machine9Type5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M15.n_peca5", 4);
        machine9Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M15.n_peca7", 4);
        machine9Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M15.n_peca8", 4);
        machine9Type9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M15.n_peca9", 4);
        machine10Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M25.n_peca3", 4);
        machine10Type5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M25.n_peca5", 4);
        machine10Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M25.n_peca7", 4);
        machine10Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M25.n_peca8", 4);
        machine10Type9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M25.n_peca9", 4);
        machine11Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M16.n_peca3", 4);
        machine11Type5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M16.n_peca5", 4);
        machine11Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M16.n_peca7", 4);
        machine11Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M23.n_peca8", 4);
        machine11Type9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M16.n_peca9", 4);
        machine12Type3 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M26.n_peca3", 4);
        machine12Type5 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M26.n_peca5", 4);
        machine12Type7 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M26.n_peca7", 4);
        machine12Type8 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M26.n_peca8", 4);
        machine12Type9 = client.readInt16("|var|CODESYS Control Win V3 x64.Application.PLC_PRG.M26.n_peca9", 4);
        updateTableC6();
        updateTableC5();
        updateTableC4();
        updateTableC3();
        updateTableC2();
        updateTableC1();
    }

    public void updateTableC6() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable13.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine11Type3, i, 1);
                    model.setValueAt(machine12Type3, i, 2);
                    break;
                case "5":
                    model.setValueAt(machine11Type5, i, 1);
                    model.setValueAt(machine12Type5, i, 2);
                    break;
                case "7":
                    model.setValueAt(machine11Type7, i, 1);
                    model.setValueAt(machine12Type7, i, 2);
                    break;
                case "8":
                    model.setValueAt(machine11Type8, i, 1);
                    model.setValueAt(machine12Type8, i, 2);
                    break;
                case "9":
                    model.setValueAt(machine11Type9, i, 1);
                    model.setValueAt(machine12Type9, i, 2);
                    break;
            }

        }
    }
    public void updateTableC5() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable16.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine9Type3, i, 1);
                    model.setValueAt(machine10Type3, i, 2);
                    break;
                case "5":
                    model.setValueAt(machine9Type5, i, 1);
                    model.setValueAt(machine10Type5, i, 2);
                    break;
                case "7":
                    model.setValueAt(machine9Type7, i, 1);
                    model.setValueAt(machine10Type7, i, 2);
                    break;
                case "8":
                    model.setValueAt(machine9Type8, i, 1);
                    model.setValueAt(machine10Type8, i, 2);
                    break;
                case "9":
                    model.setValueAt(machine9Type9, i, 1);
                    model.setValueAt(machine10Type9, i, 2);
                    break;
            }

        }
    }
    public void updateTableC4() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable9.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine7Type3, i, 1);
                    model.setValueAt(machine8Type3, i, 2);
                    break;
                case "5":
                    model.setValueAt(machine7Type5, i, 1);
                    model.setValueAt(machine8Type5, i, 2);
                    break;
                case "7":
                    model.setValueAt(machine7Type7, i, 1);
                    model.setValueAt(machine8Type7, i, 2);
                    break;
                case "8":
                    model.setValueAt(machine7Type8, i, 1);
                    model.setValueAt(machine8Type8, i, 2);
                    break;
                case "9":
                    model.setValueAt(machine7Type9, i, 1);
                    model.setValueAt(machine8Type9, i, 2);
                    break;
            }
        }
    }
    public void updateTableC3() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable15.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine5Type3, i, 1);
                    model.setValueAt(machine6Type3, i, 2);
                    break;
                case "4":
                    model.setValueAt(machine5Type4, i, 1);
                    model.setValueAt(machine6Type4, i, 2);
                    break;
                case "6":
                    model.setValueAt(machine5Type6, i, 1);
                    model.setValueAt(machine6Type6, i, 2);
                    break;
                case "7":
                    model.setValueAt(machine5Type7, i, 1);
                    model.setValueAt(machine6Type7, i, 2);
                    break;
                case "8":
                    model.setValueAt(machine5Type8, i, 1);
                    model.setValueAt(machine6Type8, i, 2);
                    break;
            }
        }
    }
    public void updateTableC2() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable8.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine3Type3, i, 1);
                    model.setValueAt(machine4Type3, i, 2);
                    break;
                case "4":
                    model.setValueAt(machine3Type4, i, 1);
                    model.setValueAt(machine4Type4, i, 2);
                    break;
                case "6":
                    model.setValueAt(machine3Type6, i, 1);
                    model.setValueAt(machine4Type6, i, 2);
                    break;
                case "7":
                    model.setValueAt(machine3Type7, i, 1);
                    model.setValueAt(machine4Type7, i, 2);
                    break;
                case "8":
                    model.setValueAt(machine3Type8, i, 1);
                    model.setValueAt(machine4Type8, i, 2);
                    break;
            }
        }
    }
    public void updateTableC1() {
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) jTable14.getModel();
        for (int i = 0; i < model.getRowCount(); i++) {
            String machineType = (String) model.getValueAt(i, 0);
            switch (machineType) {
                case "3":
                    model.setValueAt(machine1Type3, i, 1);
                    model.setValueAt(machine2Type3, i, 2);
                    break;
                case "4":
                    model.setValueAt(machine1Type4, i, 1);
                    model.setValueAt(machine2Type4, i, 2);
                    break;
                case "6":
                    model.setValueAt(machine1Type6, i, 1);
                    model.setValueAt(machine2Type6, i, 2);
                    break;
                case "7":
                    model.setValueAt(machine1Type7, i, 1);
                    model.setValueAt(machine2Type7, i, 2);
                    break;
                case "8":
                    model.setValueAt(machine1Type8, i, 1);
                    model.setValueAt(machine2Type8, i, 2);
                    break;
            }
        }
    }
public void currentDate(){
        Calendar cal= new GregorianCalendar();
        int month = cal.get(Calendar.MONTH);
        int year= cal.get(Calendar.YEAR);
        int day = cal.get(Calendar.DAY_OF_MONTH);
       
        jLabel9.setText(day+"/"+(month+1)+"/" + year);
    }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jScrollPane13 = new javax.swing.JScrollPane();
        jTable13 = new javax.swing.JTable();
        jScrollPane8 = new javax.swing.JScrollPane();
        jTable8 = new javax.swing.JTable();
        jScrollPane9 = new javax.swing.JScrollPane();
        jTable9 = new javax.swing.JTable();
        jScrollPane14 = new javax.swing.JScrollPane();
        jTable14 = new javax.swing.JTable();
        jScrollPane15 = new javax.swing.JScrollPane();
        jTable15 = new javax.swing.JTable();
        jScrollPane16 = new javax.swing.JScrollPane();
        jTable16 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(1350, 605));
        getContentPane().setLayout(null);

        jLabel1.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 30)); // NOI18N
        jLabel1.setText("Work Pieces Produced By Type");
        getContentPane().add(jLabel1);
        jLabel1.setBounds(500, 10, 520, 36);

        jTable13.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                    {"3", null, null},//C6
                    {"5", null, null},
                    {"7", null, null},
                    {"8", null, null},
                    {"9", null, null}
            },
            new String [] {
                "Piece Type", "Quantity Produced by Machine Type 1", "Quantity Produced by Machine Type 2"
            }
        ));
        jTable13.setCellSelectionEnabled(true);
        jTable13.setGridColor(new java.awt.Color(0, 0, 0));
        jTable13.setShowGrid(true);
        jScrollPane13.setViewportView(jTable13);

        getContentPane().add(jScrollPane13);
        jScrollPane13.setBounds(690, 390, 640, 90);

        jTable8.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", null, null},//C2
                {"4", null, null},
                {"6", null, null},
                {"7", null, null},
                {"8", null, null}
            },
            new String [] {
                "Piece Type", "Quantity Produced by Machine Type 1", "Quantity Produced by Machine Type 2"
            }
        ));
        jTable8.setCellSelectionEnabled(true);
        jTable8.setGridColor(new java.awt.Color(0, 0, 0));
        jTable8.setShowGrid(true);
        jScrollPane8.setViewportView(jTable8);

        getContentPane().add(jScrollPane8);
        jScrollPane8.setBounds(20, 220, 640, 110);

        jTable9.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", null, null},//C4
                {"5", null, null},
                {"7", null, null},
                    {"8", null, null},
                {"9", null, null}
            },
            new String [] {
                "Piece Type", "Quantity Produced by Machine Type 3", "Quantity Produced by Machine Type 4"
            }
        ));
        jTable9.setCellSelectionEnabled(true);
        jTable9.setGridColor(new java.awt.Color(0, 0, 0));
        jTable9.setShowGrid(true);
        jScrollPane9.setViewportView(jTable9);

        getContentPane().add(jScrollPane9);
        jScrollPane9.setBounds(690, 90, 640, 110);

        jTable14.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", null, null},//C1
                {"4", null, null},
                {"6", null, null},
                {"7", null, null},
                {"8", null, null}
            },
            new String [] {
                "Piece Type", "Quantity Produced by Machine Type 1", "Quantity Produced by Machine Type 2"
            }
        ));
        jTable14.setCellSelectionEnabled(true);
        jTable14.setGridColor(new java.awt.Color(0, 0, 0));
        jTable14.setShowHorizontalLines(true);
        jTable14.setShowVerticalLines(true);
        jScrollPane14.setViewportView(jTable14);

        getContentPane().add(jScrollPane14);
        jScrollPane14.setBounds(20, 70, 640, 110);

        jTable15.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", null, null},//C3
                {"4", null, null},
                {"6", null, null},
                {"7", null, null},
                {"8", null, null}
            },
            new String [] {
                "Piece Type", "Quantity Produced by Machine Type 1", "Quantity Produced by Machine Type 2"
            }
        ));
        jTable15.setCellSelectionEnabled(true);
        jTable15.setGridColor(new java.awt.Color(0, 0, 0));
        jTable15.setShowGrid(true);
        jScrollPane15.setViewportView(jTable15);

        getContentPane().add(jScrollPane15);
        jScrollPane15.setBounds(20, 370, 640, 110);

        jTable16.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"3", null, null},//C5
                {"5", null, null},
                {"7", null, null},
                    {"8", null, null},
                {"9", null, null}
            },
            new String [] {
                "Piece Type", "Quantity Produced by Machine Type 1", "Quantity Produced by Machine Type 2"
            }
        ));
        jTable16.setCellSelectionEnabled(true);
        jTable16.setGridColor(new java.awt.Color(0, 0, 0));
        jTable16.setShowHorizontalLines(true);
        jTable16.setShowVerticalLines(true);
        jScrollPane16.setViewportView(jTable16);

        getContentPane().add(jScrollPane16);
        jScrollPane16.setBounds(690, 240, 640, 90);

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/340.png"))); // NOI18N
        jButton1.setText("Back to Machine Statistics");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(800, 520, 400, 40);

        jLabel3.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel3.setText("C1");
        getContentPane().add(jLabel3);
        jLabel3.setBounds(300, 40, 37, 40);

        jLabel4.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel4.setText("C2");
        getContentPane().add(jLabel4);
        jLabel4.setBounds(290, 190, 40, 40);

        jLabel5.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel5.setText("C3");
        getContentPane().add(jLabel5);
        jLabel5.setBounds(290, 340, 40, 40);

        jLabel6.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel6.setText("C4");
        getContentPane().add(jLabel6);
        jLabel6.setBounds(1000, 70, 40, 21);

        jLabel7.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel7.setText("C5");
        getContentPane().add(jLabel7);
        jLabel7.setBounds(1000, 180, 60, 100);

        jLabel8.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 18)); // NOI18N
        jLabel8.setText("C6");
        getContentPane().add(jLabel8);
        jLabel8.setBounds(1000, 370, 50, 20);

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/25694.png"))); // NOI18N
        jButton2.setText("Return to Main Menu");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(90, 520, 400, 40);

        jLabel9.setFont(new java.awt.Font("Arial Rounded MT Bold", 0, 14)); // NOI18N
        jLabel9.setText("jLabel9");
        getContentPane().add(jLabel9);
        jLabel9.setBounds(1260, 20, 140, 16);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/images2/colorkit2.png"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 1380, 570);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        this.dispose();
        
        
        
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
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
            java.util.logging.Logger.getLogger(Sub_MachineStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Sub_MachineStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Sub_MachineStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Sub_MachineStats.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Sub_MachineStats().setVisible(true);
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
    private javax.swing.JScrollPane jScrollPane13;
    private javax.swing.JScrollPane jScrollPane14;
    private javax.swing.JScrollPane jScrollPane15;
    private javax.swing.JScrollPane jScrollPane16;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JTable jTable13;
    private javax.swing.JTable jTable14;
    private javax.swing.JTable jTable15;
    private javax.swing.JTable jTable16;
    private javax.swing.JTable jTable8;
    private javax.swing.JTable jTable9;
    // End of variables declaration//GEN-END:variables
}
