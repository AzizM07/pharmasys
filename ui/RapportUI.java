package ui;

import dao.RapportDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class RapportUI extends JFrame {

    private RapportDAO dao = new RapportDAO();
    private DefaultTableModel model;
    private String login; //  login

    public RapportUI(String login) {

        this.login = login;

        setTitle("Rapports et Statistiques");
        setSize(800, 500);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblVentes = new JLabel("Total ventes : " + dao.getTotalVentes());
        JLabel lblQte = new JLabel("Quantité vendue : " + dao.getTotalQuantiteVendue());
        JLabel lblCA = new JLabel("Chiffre d'affaires : " + dao.getChiffreAffaires() + " DT");

        lblVentes.setBounds(20, 20, 300, 25);
        lblQte.setBounds(20, 50, 300, 25);
        lblCA.setBounds(20, 80, 300, 25);

        add(lblVentes);
        add(lblQte);
        add(lblCA);

        model = new DefaultTableModel(
            new String[]{"Médicament", "Quantité vendue", "Total (DT)"},
            0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        JTable table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 120, 740, 280);
        add(scroll);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setBounds(320, 420, 140, 30);
        add(btnRetour);

        btnRetour.addActionListener(e -> {
            dispose();
            new GestionnaireUI(login); 
        });

        loadTable();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadTable() {
        model.setRowCount(0);
        Vector<Vector<Object>> data = dao.ventesParMedicament();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }
}
