package ui;

import dao.VenteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class VenteUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;
    private VenteDAO dao = new VenteDAO();

    private String login;  

    public VenteUI(String login) {   

        this.login = login;

        setTitle("Gestion des Ventes");
        setSize(900, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblTitre = new JLabel("Historique des ventes");
        lblTitre.setBounds(20, 10, 300, 25);
        add(lblTitre);

        JButton btnNouvelleVente = new JButton("Nouvelle vente");
        JButton btnAnnulerVente = new JButton("Annuler vente");
        JButton btnActualiser = new JButton("Actualiser");
        JButton btnRetour = new JButton("Retour");

        btnNouvelleVente.setBounds(20, 50, 180, 30);
        btnAnnulerVente.setBounds(20, 90, 180, 30);
        btnActualiser.setBounds(20, 130, 180, 30);
        btnRetour.setBounds(20, 170, 180, 30);

        add(btnNouvelleVente);
        add(btnAnnulerVente);
        add(btnActualiser);
        add(btnRetour);

        model = new DefaultTableModel(
            new String[]{
                "ID Vente",
                "ID Client",
                "ID Médicament",
                "Quantité",
                "Date"
            }, 0
        ) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(220, 40, 650, 400);
        add(scroll);

        btnActualiser.addActionListener(e -> loadVentes());

        btnNouvelleVente.addActionListener(e -> {
            dispose();
            new EnregistrerVenteUI(login);   
        });

        btnAnnulerVente.addActionListener(e -> {
            dispose();
            new AnnulerVenteUI(login);     
        });

        btnRetour.addActionListener(e -> {
            dispose();
            new MenuPharmacienUI(login);    
        });

        loadVentes();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadVentes() {

        model.setRowCount(0);
        Vector<Vector<Object>> data = dao.getAllVentes();

        for (Vector<Object> row : data) {
            model.addRow(row);
        }

        if (data.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Aucune vente enregistrée",
                "Info",
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
