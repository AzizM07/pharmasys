package ui;

import dao.MedicamentDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class StockUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private MedicamentDAO dao = new MedicamentDAO();

    public StockUI() {

        setTitle("Gestion du Stock");
        setSize(900, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        JButton btnAjouter = new JButton("Ajouter médicament");
        JButton btnModifier = new JButton("Mettre à jour stock");
        JButton btnActualiser = new JButton("Actualiser");

        btnAjouter.setBounds(20, 20, 180, 30);
        btnModifier.setBounds(20, 60, 180, 30);
        btnActualiser.setBounds(20, 100, 180, 30);

        add(btnAjouter);
        add(btnModifier);
        add(btnActualiser);

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
        btnAjouter.addActionListener(e -> ajouterMedicament());
        btnModifier.addActionListener(e -> modifierStock());

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
    }

    private void ajouterMedicament() {

        try {
            String nom = JOptionPane.showInputDialog(this, "Nom médicament");
            String dosage = JOptionPane.showInputDialog(this, "Dosage");
            int stock = Integer.parseInt(
                JOptionPane.showInputDialog(this, "Stock initial"));
            double prix = Double.parseDouble(
                JOptionPane.showInputDialog(this, "Prix unitaire"));

            dao.ajouterMedicament(nom, dosage, stock, prix);
            loadStock();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Données invalides",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void modifierStock() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Sélectionne un médicament",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idMed = (int) model.getValueAt(row, 0);

        try {
            int newStock = Integer.parseInt(
                JOptionPane.showInputDialog(this, "Nouveau stock"));

            dao.updateStock(idMed, newStock);
            loadStock();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Valeur invalide",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Pour test direct
    public static void main(String[] args) {
        new StockUI();
    }
}
