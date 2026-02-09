package ui;

import dao.UserDAO;
import javax.swing.*;

public class LoginUI extends JFrame {

    JTextField txtLogin = new JTextField();
    JPasswordField txtPwd = new JPasswordField();
    JButton btnLogin = new JButton("Connexion");

    public LoginUI() {

        setTitle("Login");
        setSize(300, 200);
        setLayout(null);
        setLocationRelativeTo(null);

        JLabel l1 = new JLabel("Login");
        JLabel l2 = new JLabel("Mot de passe");

        l1.setBounds(30, 30, 100, 25);
        l2.setBounds(30, 70, 100, 25);
        txtLogin.setBounds(130, 30, 120, 25);
        txtPwd.setBounds(130, 70, 120, 25);
        btnLogin.setBounds(90, 120, 120, 30);

        add(l1); add(txtLogin);
        add(l2); add(txtPwd);
        add(btnLogin);

        btnLogin.addActionListener(e -> {

            String login = txtLogin.getText();
            String pwd = new String(txtPwd.getPassword());

            UserDAO dao = new UserDAO();
            String role = dao.login(login, pwd);

            if ("PHARMACIEN".equals(role)) {
                dispose();
                new MenuPharmacienUI(login);

            } else if ("GESTIONNAIRE".equals(role)) {
                dispose();
                new GestionnaireUI(login);

            } else {
                JOptionPane.showMessageDialog(this,
                    "Informations incorrectes",
                    "Erreur",
                    JOptionPane.ERROR_MESSAGE);
            }
        });

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
    }

    public static void main(String[] args) {
        new LoginUI();
    }
}
