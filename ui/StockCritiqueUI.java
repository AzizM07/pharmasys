package ui;

import dao.MedicamentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class StockCritiqueUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private MedicamentDAO dao = new MedicamentDAO();

    public StockCritiqueUI() {

        setTitle("Stock Critique");
        setSize(800, 450);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lbl = new JLabel("Médicaments en stock critique (≤ 10)");
        lbl.setBounds(20, 10, 300, 25);
        add(lbl);

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
        scroll.setBounds(20, 40, 740, 320);
        add(scroll);

        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setBounds(320, 370, 140, 30);
        add(btnActualiser);

        btnActualiser.addActionListener(e -> loadStockCritique());

        loadStockCritique();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }


    private void loadStockCritique() {

        model.setRowCount(0);

        Vector<Vector<Object>> data = dao.getStockCritique(10);

        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucun stock critique ",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
        }

        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }

    
    public static void main(String[] args) {
        new StockCritiqueUI();
    }
}
