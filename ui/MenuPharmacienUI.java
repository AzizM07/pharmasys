package ui;

import javax.swing.*;

public class MenuPharmacienUI extends JFrame {

    private String login;

    public MenuPharmacienUI(String login) {

        this.login = login;

        setTitle("Menu Pharmacien");
        setSize(300, 300);
        setLayout(null);
        setLocationRelativeTo(null);

        JButton btnStock = new JButton("Gérer le stock");
        JButton btnVente = new JButton("Enregistrer vente");
        JButton btnConsulter = new JButton("Consulter ventes");
        JButton btnAnnuler = new JButton("Annuler vente");
        JButton btnProfil = new JButton("Mon profil");
        JButton btnRetour = new JButton("Retour");

        btnVente.setBounds(50, 30, 200, 30);
        btnConsulter.setBounds(50, 70, 200, 30);
        btnAnnuler.setBounds(50, 110, 200, 30);
        btnProfil.setBounds(50, 150, 200, 30);
        btnStock.setBounds(50, 190, 200, 30);
        btnRetour.setBounds(50, 240, 100, 30);

        add(btnVente);
        add(btnConsulter);
        add(btnAnnuler);
        add(btnProfil);
        add(btnStock);
        add(btnRetour);

        btnStock.addActionListener(e -> new StockUI());

        btnVente.addActionListener(e -> {
            dispose();
            new VenteUI(login);
        });

        btnConsulter.addActionListener(e -> {
    new ConsulterVentesUI(this);
});


        btnAnnuler.addActionListener(e -> {
            dispose();
            new AnnulerVenteUI(login);
        });

        btnProfil.addActionListener(e -> {
            dispose();
            new ProfilUI("PHARMACIEN", login);
        });

        btnRetour.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
