import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumnModel;
import java.awt.*;

public class ProgramsPanel {
    private JPanel programs = new JPanel(new BorderLayout());
    private JTable table;
    private DefaultTableModel tableModel;

    public ProgramsPanel() {
        tableModel = new DefaultTableModel(new Object[]{"Program", "Starttid", "Sluttid", "Undernamn", "Beskrivning", "Bild"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        //table.ed
        TableColumnModel columnModel = table.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(60);
        columnModel.getColumn(1).setPreferredWidth(20);
        columnModel.getColumn(2).setPreferredWidth(20);

        for (int i = 3; i < 6; i++) {
            columnModel.getColumn(i).setMinWidth(0);
            columnModel.getColumn(i).setMaxWidth(0);
            columnModel.getColumn(i).setPreferredWidth(0);
            columnModel.getColumn(i).setResizable(false);
        }

        JScrollPane scrollPane = new JScrollPane(table);
        programs.add(scrollPane, BorderLayout.CENTER);
    }

    public JPanel getPanel() {
        return programs;
    }

    public JTable getTable() {
        return table;
    }

    public void updatePrograms(List<Program> programs) {
        tableModel.setRowCount(0);
        for (Program p : programs) {
            tableModel.addRow(new Object[]{p.getProgramName(), p.getStartDate(), p.getEndDate(),
                                           p.getProgramSubName(), p.getProgramDescription(), p.getImageUrl()});
        }
    }
}
