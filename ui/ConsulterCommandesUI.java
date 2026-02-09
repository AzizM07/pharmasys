package ui;

import dao.CommandeDAO;
import java.awt.Component;
import java.awt.LayoutManager;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

public class ConsulterCommandesUI extends JFrame {

    private JTable tableCommandes;
    private DefaultTableModel tableModel;
    private CommandeDAO dao = new CommandeDAO();

    public ConsulterCommandesUI() {
        this.setTitle("Consultation des Commandes");
        this.setSize(800, 500);
        this.setLayout((LayoutManager)null);
        this.setLocationRelativeTo((Component)null);

        
        JButton btnActualiser = new JButton("Actualiser");
        btnActualiser.setBounds(20, 20, 120, 30);
        this.add(btnActualiser);

        
        this.tableModel = new DefaultTableModel(
            new String[]{"ID Commande", "ID Gestionnaire", "ID Médicament", "Quantité", "Date"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; 
            }
        };

        this.tableCommandes = new JTable(this.tableModel);
        JScrollPane scrollPane = new JScrollPane(this.tableCommandes);
        scrollPane.setBounds(160, 20, 600, 400);
        this.add(scrollPane);

        
        btnActualiser.addActionListener(e -> loadCommandes());

        
        loadCommandes();

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);;
        this.setVisible(true);
    }

    private void loadCommandes() {
        tableModel.setRowCount(0); 
        Vector<Vector<Object>> commandes = dao.getAllCommandes();
        for (Vector<Object> row : commandes) {
            tableModel.addRow(row);
        }
    }

    public static void main(String[] args) {
        new ConsulterCommandesUI();
    }
}
