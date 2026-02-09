package ui;

import javax.swing.*;

public class GestionnaireUI extends JFrame {

    private String login; 

    public GestionnaireUI(String login) {

        this.login = login;

        setTitle("Menu Gestionnaire");
        setSize(350, 400);
        setLayout(null);
        setLocationRelativeTo(null);

        JButton btnStock = new JButton("Gérer le stock");
        JButton btnCommande = new JButton("Gérer commandes");
        JButton btnconsCommande = new JButton("Consulter commandes");
        JButton btnVentes = new JButton("Consulter ventes");
        JButton btnCritique = new JButton("Stock critique");
        JButton btnHistorique = new JButton("Stock historique");
        JButton btnRapport = new JButton("Rapports");
        JButton btnNotif = new JButton("Notifications");
        JButton btnProfil = new JButton("Mon profil");
        JButton btnRetour = new JButton("Retour");

        JButton[] btns = {
            btnconsCommande, btnStock, btnCommande, btnVentes,
            btnCritique, btnHistorique, btnRapport,
            btnNotif, btnProfil, btnRetour
        };

        int y = 20;
        for (JButton b : btns) {
            b.setBounds(50, y, 250, 30);
            add(b);
            y += 35;
        }

        btnStock.addActionListener(e -> new StockUI());
        btnCommande.addActionListener(e -> new CommandeUI());
        btnconsCommande.addActionListener(e -> new ConsulterCommandesUI());
        btnVentes.addActionListener(e -> new ConsulterVentesUI(this));
        btnCritique.addActionListener(e -> new StockCritiqueUI());
        btnStock.addActionListener(e -> {dispose();
            new ConsulterStockUI("GESTIONNAIRE", login);});

        btnRapport.addActionListener(e -> new RapportUI(login));
        btnNotif.addActionListener(e -> new NotifUI());
        btnHistorique.addActionListener(e -> new StockHistoriqueUI());
        btnProfil.addActionListener(e -> {
            dispose();
            new ProfilUI("GESTIONNAIRE", login); 
        });

        btnRetour.addActionListener(e -> {
            dispose();
            new LoginUI();
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }
}
