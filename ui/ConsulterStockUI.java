package ui;

import dao.MedicamentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class ConsulterStockUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private MedicamentDAO dao = new MedicamentDAO();

    private String role;
    private String login;

    public ConsulterStockUI(String role, String login) {

        this.role = role;
        this.login = login;

        setTitle("Consultation du Stock");
        setSize(900, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblTitre = new JLabel("Liste des médicaments en stock");
        lblTitre.setBounds(20, 10, 300, 25);
        add(lblTitre);

        JButton btnActualiser = new JButton("Actualiser");
        JButton btnRetour = new JButton("Retour");

        btnActualiser.setBounds(20, 50, 180, 30);
        btnRetour.setBounds(20, 90, 180, 30);

        add(btnActualiser);
        add(btnRetour);

        model = new DefaultTableModel(
            new String[]{
                "ID",
                "Nom",
                "Dosage",
                "Stock",
                "Prix (DT)"
            }, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(220, 20, 650, 420);
        add(scroll);

        btnActualiser.addActionListener(e -> loadStock());

        btnRetour.addActionListener(e -> {
            dispose();
            if ("PHARMACIEN".equalsIgnoreCase(role)) {
                new MenuPharmacienUI(login);
            } else {
                new GestionnaireUI(login);
            }
        });

        loadStock();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    private void loadStock() {

        model.setRowCount(0);
        Vector<Vector<Object>> data = dao.getAllMedicaments();

        for (Vector<Object> row : data) {
            model.addRow(row);
        }

        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucun médicament en stock",
                "Information",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
