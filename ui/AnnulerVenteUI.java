package ui;

import dao.VenteDAO;
import javax.swing.*;

public class AnnulerVenteUI extends JFrame {

    private JTextField txtIdVente = new JTextField();
    private JButton btnAnnuler = new JButton("Annuler la vente");
    private JButton btnRetour = new JButton("Retour");
    private String login; 

    public AnnulerVenteUI(String login) { 
        this.login = login;

        setTitle("Annuler une vente");
        setSize(350, 250); 
        setLayout(null);
        setLocationRelativeTo(null); 

        JLabel lbl = new JLabel("ID Vente :");
        lbl.setBounds(30, 40, 100, 25);
        txtIdVente.setBounds(130, 40, 150, 25);

        btnAnnuler.setBounds(90, 90, 170, 35);
        btnRetour.setBounds(90, 150, 170, 35); 

        add(lbl);
        add(txtIdVente);
        add(btnAnnuler);
        add(btnRetour);

        
        btnAnnuler.addActionListener(e -> {
            try {
                int idVente = Integer.parseInt(txtIdVente.getText());

                VenteDAO dao = new VenteDAO();
                boolean ok = dao.annulerVente(idVente);

                if (ok) {
                    JOptionPane.showMessageDialog(this,
                        "Vente annulée et stock rétabli");
                } else {
                    JOptionPane.showMessageDialog(this,
                        "Vente introuvable",
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }

            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this,
                    "ID invalide",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        
        btnRetour.addActionListener(e -> {
            dispose(); 
            new MenuPharmacienUI(login);
        });

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }

    
    public static void main(String[] args) {
        new AnnulerVenteUI("monLogin"); 
    }
}
