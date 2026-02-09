package ui;

import dao.MedicamentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class NotifUI extends JFrame {

    private MedicamentDAO dao = new MedicamentDAO();
    private DefaultTableModel model;

    public NotifUI() {

        setTitle("Notifications - Stock Critique");
        setSize(700, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel(" Médicaments en stock critique");
        lbl.setBounds(20, 10, 300, 25);
        add(lbl);

        model = new DefaultTableModel(
            new String[]{"ID", "Nom", "Dosage", "Stock", "Prix"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 50, 650, 250);
        add(scroll);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setBounds(280, 320, 120, 30);
        add(btnRetour);

        btnRetour.addActionListener(e -> {
            dispose();
        });

        loadNotifications();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadNotifications() {

        model.setRowCount(0);

        Vector<Vector<Object>> data = dao.getStockCritique(20);

        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucune alerte de stock critique ");
        } else {
            for (Vector<Object> row : data) {
                model.addRow(row);
            }
        }
    }

   
    public static void main(String[] args) {
        new NotifUI();
    }
}
