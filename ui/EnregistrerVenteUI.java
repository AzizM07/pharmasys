package ui;

import dao.MedicamentDAO;
import dao.VenteDAO;
import dao.ClientDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class EnregistrerVenteUI extends JFrame {

    private JTable table;
    private DefaultTableModel model;

    private JTextField txtNom = new JTextField();
    private JTextField txtPrenom = new JTextField();
    private JTextField txtQuantite = new JTextField();

    private MedicamentDAO medDAO = new MedicamentDAO();
    private VenteDAO venteDAO = new VenteDAO();
    private ClientDAO clientDAO = new ClientDAO();

    private String login;   

    
    public EnregistrerVenteUI(String login) {

        this.login = login;

        setTitle("Enregistrer une vente");
        setSize(800, 450);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblNom = new JLabel("Nom client :");
        JLabel lblPrenom = new JLabel("Prénom client :");
        JLabel lblQte = new JLabel("Quantité :");

        lblNom.setBounds(20, 20, 100, 25);
        txtNom.setBounds(120, 20, 120, 25);

        lblPrenom.setBounds(260, 20, 120, 25);
        txtPrenom.setBounds(380, 20, 120, 25);

        lblQte.setBounds(520, 20, 80, 25);
        txtQuantite.setBounds(600, 20, 80, 25);

        add(lblNom);
        add(txtNom);
        add(lblPrenom);
        add(txtPrenom);
        add(lblQte);
        add(txtQuantite);

        JButton btnValider = new JButton("Valider vente");
        JButton btnRetour = new JButton("Retour");

        btnValider.setBounds(200, 60, 180, 30);
        btnRetour.setBounds(420, 60, 180, 30);

        add(btnValider);
        add(btnRetour);

        model = new DefaultTableModel(
            new String[]{"ID", "Médicament", "Stock"}, 0
        ) {
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 110, 740, 280);
        add(scroll);

        btnValider.addActionListener(e -> enregistrerVente());

        
        btnRetour.addActionListener(e -> {
            dispose();
            new VenteUI(login);
        });

        loadMedicaments();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    private void loadMedicaments() {
        model.setRowCount(0);
        Vector<Vector<Object>> data = medDAO.getMedicamentsForVente();
        for (Vector<Object> row : data) {
            model.addRow(row);
        }
    }

    private void enregistrerVente() {

        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this,
                "Sélectionne un médicament",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (txtNom.getText().isEmpty() || txtPrenom.getText().isEmpty() || txtQuantite.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Remplis toutes les informations",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int idMed = (int) model.getValueAt(row, 0);
            int qte = Integer.parseInt(txtQuantite.getText());

            
            int idClient = clientDAO.getOrCreateClient(
                txtNom.getText().trim(),
                txtPrenom.getText().trim()
            );

            
            boolean ok = venteDAO.enregistrerVente(
                1,
                idClient,
                idMed,
                qte
            );

            if (ok) {
                JOptionPane.showMessageDialog(this, "Vente enregistrée avec succès");
                loadMedicaments();
                txtQuantite.setText("");
            } else {
                JOptionPane.showMessageDialog(this,
                    "Stock insuffisant ou erreur",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this,
                "Quantité invalide",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }
    }
}
