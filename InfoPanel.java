import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class InfoPanel {
    private JPanel panel = new JPanel(new GridLayout(2, 1));
    private JPanel channelPanel = new JPanel(new BorderLayout());
    private JPanel programPanel = new JPanel(new BorderLayout());
    private JTextField selectChannelPrompt = new JTextField("Välj en kanal för att se mer info");
    private JTextField selectProgramPrompt = new JTextField("Välj ett program för att se mer info");

    public InfoPanel() {
        channelPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Kanal"));
        programPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.GRAY), "Program"));
        selectChannelPrompt.setHorizontalAlignment(JTextField.CENTER);
        selectChannelPrompt.setEditable(false);
        selectProgramPrompt.setHorizontalAlignment(JTextField.CENTER);
        selectProgramPrompt.setEditable(false);
        channelPanel.add(selectChannelPrompt);
        programPanel.add(selectProgramPrompt);
        panel.add(channelPanel);
        panel.add(programPanel);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void updateChannel(Channel channel) {
        panel.removeAll();
        channelPanel.removeAll();
        programPanel.removeAll();
        programPanel.add(selectProgramPrompt);

        JTextArea channelName = setText(channel.getChannelName(), 26, false);

        JLabel logo = getImage(channel.getLogoUrl());

        JTextArea description = setText(channel.getChannelDescription(), 14, true);
        description.setBorder(new EmptyBorder(5, 10, 5, 10));

        JPanel north = new JPanel(new FlowLayout(FlowLayout.LEFT));
        north.add(logo);
        north.add(channelName);
        north.setBorder(new EmptyBorder(5, 5, 5, 5));

        JTextArea updateTime = setText("Senast uppdaterad: " + channel.getProgramUpdateDate(), 14, true);
        updateTime.setBorder(new EmptyBorder(5, 10, 5, 10));

        channelPanel.add(north, BorderLayout.NORTH);
        channelPanel.add(description, BorderLayout.CENTER);
        channelPanel.add(updateTime, BorderLayout.SOUTH);

        panel.add(channelPanel);
        panel.add(programPanel);
    }

    public void updateProgram(String[] info) {
        panel.remove(programPanel);
        programPanel.removeAll();

        JTextArea title = setText(info[0], 20, false);
        JTextArea date = setText(info[1] + " - " + info[2], 14, false);
        JTextArea underTitle = setText(info[3], 16, false);
        JTextArea description = setText(info[4], 14, true);
        JLabel logo = getImage(info[5]);

        JPanel north = new JPanel(new BorderLayout());
        north.add(logo, BorderLayout.WEST);

        JPanel texts = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(2, 5, 2, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;
        gbc.weightx = 1;

        gbc.gridx = 0;
        gbc.gridy = 0;
        texts.add(title, gbc);

        gbc.gridy = 1;
        texts.add(underTitle, gbc);

        north.add(texts, BorderLayout.CENTER);

        date.setBorder(new EmptyBorder(6, 0, 0, 0));
        north.add(date, BorderLayout.SOUTH);

        north.setBorder(new EmptyBorder(5, 10, 5, 10));

        programPanel.add(north, BorderLayout.NORTH);

        description.setBorder(new EmptyBorder(5, 10, 5, 10));

        programPanel.add(description, BorderLayout.CENTER);
        panel.add(programPanel);
    }

    private JTextArea setText(String content, int size, boolean wrap){
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("Arial", Font.PLAIN, size));
        textArea.setOpaque(false);
        textArea.setBackground(new Color(0, 0, 0, 0));
        textArea.setEditable(false);
        if (wrap) {
            textArea.setLineWrap(true);
            textArea.setWrapStyleWord(true);
        }
        return textArea;
    }

    private JLabel getImage(String urlString) {
        JLabel image = new JLabel();

        new SwingWorker<ImageIcon, Void>() {
            @Override
            protected ImageIcon doInBackground() {
                try {
                    URL url = new URL(urlString);
                    BufferedImage img = ImageIO.read(url);
                    Image scaledImage = img.getScaledInstance(60, 60, Image.SCALE_SMOOTH);
                    return new ImageIcon(scaledImage);
                }
                catch(IOException e) {
                    if (!urlString.isEmpty()) {
                        System.err.println("Failed to get channel or program logo.");
                    }
                    return null;
                }
            }

            @Override
            protected void done() {
                try {
                    ImageIcon icon = get();
                    if (icon != null) {
                        image.setIcon(icon);
                    }
                    else {
                        image.setText("Logga saknas");
                    }
                }
                catch (Exception e) {
                    System.err.println("Failed to show channel logo.");
                }
            }
        }.execute();
        return image;
    }
}
