package ui;

import dao.ProfilDAO;
import javax.swing.*;

public class ProfilUI extends JFrame {

    public ProfilUI(String role, String login) {

        setTitle("Mon Profil");
        setSize(350, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel lblRole = new JLabel("Rôle :");
        JLabel lblNom = new JLabel("Nom :");
        JLabel lblPrenom = new JLabel("Prénom :");
        JLabel lblLogin = new JLabel("Login :");

        JLabel txtRole = new JLabel(role);
        JLabel txtNom = new JLabel();
        JLabel txtPrenom = new JLabel();
        JLabel txtLogin = new JLabel(login);

        lblRole.setBounds(40, 30, 100, 25);
        lblNom.setBounds(40, 70, 100, 25);
        lblPrenom.setBounds(40, 110, 100, 25);
        lblLogin.setBounds(40, 150, 100, 25);

        txtRole.setBounds(150, 30, 150, 25);
        txtNom.setBounds(150, 70, 150, 25);
        txtPrenom.setBounds(150, 110, 150, 25);
        txtLogin.setBounds(150, 150, 150, 25);

        add(lblRole); add(txtRole);
        add(lblNom); add(txtNom);
        add(lblPrenom); add(txtPrenom);
        add(lblLogin); add(txtLogin);

        JButton btnRetour = new JButton("Retour");
        btnRetour.setBounds(110, 200, 120, 30);
        add(btnRetour);

        btnRetour.addActionListener(e -> {
            dispose();
            if (role.equals("PHARMACIEN"))
                new MenuPharmacienUI(login);
            else
                new GestionnaireUI(login);
        });

        ProfilDAO dao = new ProfilDAO();
        ProfilDAO.Profil profil = dao.getProfil(role, login);

        if (profil != null) {
            txtNom.setText(profil.nom);
            txtPrenom.setText(profil.prenom);
        } else {
            JOptionPane.showMessageDialog(this,
                "Profil introuvable",
                "Erreur",
                JOptionPane.ERROR_MESSAGE);
        }

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
