package ui;

import dao.StockHistoriqueDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class StockHistoriqueUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private StockHistoriqueDAO dao = new StockHistoriqueDAO();

    public StockHistoriqueUI() {

        setTitle("Historique du Stock");
        setSize(800, 450);
        setLayout(null);
        setLocationRelativeTo(null);

        model = new DefaultTableModel(
            new String[]{"ID Médicament", "Médicament", "Quantité", "Date"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 740, 350);
        add(scroll);

        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setBounds(320, 380, 140, 30);
        add(btnActualiser);

        btnActualiser.addActionListener(e -> loadHistorique());

        loadHistorique();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadHistorique() {

        model.setRowCount(0);

        Vector<Vector<Object>> data = dao.getHistorique();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }

    public static void main(String[] args) {
        new StockHistoriqueUI();
    }
}
