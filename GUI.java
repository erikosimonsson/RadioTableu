import javax.swing.*;
import java.awt.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;

public class GUI {

    private Channel currentChannel = null;
    private JFrame window;
    private JTextField loadingText;
    private InfoPanel channelInfo = new InfoPanel();
    private ProgramsPanel programInfo = new ProgramsPanel();

    public GUI() {
        window = new JFrame("RadioInfo");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setMinimumSize(new Dimension(800, 600));
        window.setLayout(new BorderLayout());

        loadingText = new JTextField("Programmet laddar...");
        loadingText.setEditable(false);
        loadingText.setHorizontalAlignment(JTextField.CENTER);
        window.add(loadingText);
        window.pack();
        window.setVisible(true);
    }

    public HashMap loadChannelMenu(List<Channel> channels, List<String> channelTypes) {
        HashMap<JMenuItem, Channel> menuItems = new HashMap();
        JMenuBar bar = new JMenuBar();
        for (String ct : channelTypes) {
            JMenu channelType = new JMenu(ct);
            for (Channel c : channels) {
                if (c.getChannelType().equals(ct)) {
                    JMenuItem channel = new JMenuItem(c.getChannelName());
                    channelType.add(channel);
                    menuItems.put(channel, c);
                }
            }
            bar.add(channelType);
        }
        window.setJMenuBar(bar);
        return menuItems;
    }

    public JButton loadGUI() {
        window.remove(loadingText);
        JPanel dashboard = new JPanel(new GridLayout(1, 2));
        dashboard.add(channelInfo.getPanel());
        dashboard.add(programInfo.getPanel());
        window.add(dashboard, BorderLayout.CENTER);
        JButton reload = new JButton("Uppdatera data");
        window.add(reload, BorderLayout.SOUTH);
        window.revalidate();
        window.repaint();
        return reload;
    }

    public void loadChannel(Channel channel) {
        channelInfo.updateChannel(channel);
        programInfo.updatePrograms(channel.getPrograms());
        addTableListeners();
        window.revalidate();
        window.repaint();
        currentChannel = channel;
    }

    public void reload() {
        channelInfo.updateChannel(currentChannel);
        programInfo.updatePrograms(currentChannel.getPrograms());
        addTableListeners();
        window.revalidate();
        window.repaint();
    }

    private void addTableListeners() {
        programInfo.getTable().addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JTable table = programInfo.getTable();
                int selectedRow = table.getSelectedRow();

                if (selectedRow != -1) {
                    int columnCount = table.getColumnCount();
                    String[] rowData = new String[columnCount];

                    for (int i = 0; i < columnCount; i++) {
                        Object value = table.getValueAt(selectedRow, i);
                        rowData[i] = (value != null) ? value.toString() : "";
                    }

                    channelInfo.updateProgram(rowData);
                    window.revalidate();
                    window.repaint();
                }
            }
        });
    }
}
