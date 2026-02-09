package ui;

import dao.VenteDAO;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.Vector;

public class ConsulterVentesUI extends JDialog {

    private JTable table;
    private VenteDAO dao = new VenteDAO();

    public ConsulterVentesUI(JFrame parent) {

        super(parent, "Liste des ventes", true);
        setSize(600, 400);
        setLayout(null);
        setLocationRelativeTo(parent);

        
        Vector<String> columns = new Vector<>();
        columns.add("ID Vente");
        columns.add("Médicament");
        columns.add("Quantité");
        columns.add("Date");

        
        Vector<Vector<Object>> data = dao.getAllVentes();

        DefaultTableModel model = new DefaultTableModel(data, columns) {
            @Override
            public boolean isCellEditable(int r, int c) {
                return false;
            }
        };

        table = new JTable(model);
        table.setRowHeight(22);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBounds(20, 20, 550, 330);
        add(scroll);

        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setVisible(true);
    }
}
