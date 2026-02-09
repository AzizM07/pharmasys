package ui;

import dao.CommandeDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class CommandeUI extends JFrame {

    private JTable tableCommandes;
    private DefaultTableModel tableModel;
    private CommandeDAO dao = new CommandeDAO();

    public CommandeUI() {

        setTitle("Gestion des Commandes");
        setSize(800, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        JButton btnAjouter = new JButton("Ajouter");
        JButton btnSupprimer = new JButton("Supprimer");
        JButton btnActualiser = new JButton("Actualiser");

        btnAjouter.setBounds(20, 20, 120, 30);
        btnSupprimer.setBounds(20, 60, 120, 30);
        btnActualiser.setBounds(20, 100, 120, 30);

        add(btnAjouter);
        add(btnSupprimer);
        add(btnActualiser);

        tableModel = new DefaultTableModel(
            new String[]{
                "ID Commande",
                "ID Gestionnaire",
                "ID Médicament",
                "Quantité",
                "Date"
            }, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        tableCommandes = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tableCommandes);
        scrollPane.setBounds(160, 20, 600, 400);
        add(scrollPane);

       
        btnActualiser.addActionListener(e -> loadCommandes());

        btnAjouter.addActionListener(e -> ajouterCommande());

        btnSupprimer.addActionListener(e -> supprimerCommande());

        loadCommandes();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadCommandes() {

        tableModel.setRowCount(0);
        Vector<Vector<Object>> data = dao.getAllCommandes();

        for (Vector<Object> row : data) {
            tableModel.addRow(row);
        }
    }
    private void ajouterCommande() {

        try {
            int idGest = Integer.parseInt(
                JOptionPane.showInputDialog(this, "ID Gestionnaire")
            );
            int idMed = Integer.parseInt(
                JOptionPane.showInputDialog(this, "ID Médicament")
            );
            int qte = Integer.parseInt(
                JOptionPane.showInputDialog(this, "Quantité")
            );

            dao.ajouterCommande(idGest, idMed, qte);
            loadCommandes();

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,
                "Données invalides",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    private void supprimerCommande() {

        int row = tableCommandes.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Sélectionne une commande",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        int idCommande = (int) tableModel.getValueAt(row, 0);
        dao.supprimerCommande(idCommande);
        loadCommandes();
    }
    public static void main(String[] args) {
        new CommandeUI();
    }
}
