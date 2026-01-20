import javax.swing.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class RadioController {

    private GUI view;
    private RadioModel model;
    private JSONParser parser;
    private JButton refetch;

    public RadioController() {
        view = new GUI();
        model = new RadioModel();
        parser = new JSONParser();

        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            private List<Channel> channels;
            private List<String> channelTypes;
            @Override
            protected Void doInBackground() {
                try {
                    channels = parser.parseChannels(model.getChannels());
                    channelTypes = parser.getChannelTypes();
                }
                catch (IOException e) {
                    SwingUtilities.invokeLater(() -> {
                        JOptionPane.showMessageDialog(null,
                                "Could not fetch list of channels:\n" + e.getMessage()
                                        + "\nCheck your internet connection.\nProgram needs rebooting.",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
                        System.err.println("Unable to fetch list of channels.\nPlease check your internet connection.");
                        System.exit(1);
                    });
                }
                return null;
            }

            @Override
            protected void done() {
                if (channels == null || channelTypes == null) {
                    return;
                }
                addMenuListeners(view.loadChannelMenu(channels, channelTypes));
                refetch = view.loadGUI();
                refetch.addActionListener(e -> updateChannelData(channels));
            }
        };
        worker.execute();
    }

    private void updateChannelData(List<Channel> channels) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                try {
                    for (Channel c : channels) {
                        if (!c.getPrograms().isEmpty()) {
                            c.setProgramUpdateTime(System.currentTimeMillis());
                            c.setPrograms(parser.parsePrograms(model.getSchedule(c.getChannelId())));
                        }
                    }
                }
                catch (IOException e) {
                    SwingUtilities.invokeLater(() ->
                    JOptionPane.showMessageDialog(null,
                            "Failed to get list of programs:\n" + e.getMessage(),
                            "Error",
                            JOptionPane.ERROR_MESSAGE)
                    );
                    System.err.println("Failed to get list of programs for channel.");
                }
                return null;
            }

            @Override
            protected void done() {
                view.reload();
            }
        };
        worker.execute();
    }

    private void addMenuListeners(HashMap<JMenuItem, Channel> menuItems) {
        menuItems.forEach((menuItem, channel) -> {
            menuItem.addActionListener(e -> {
                SwingWorker<Void, Void> worker = new SwingWorker<>() {
                    @Override
                    protected Void doInBackground() {
                        try {
                            if (channel.getProgramUpdateTime() < (System.currentTimeMillis() - 3600000L)) {
                                channel.setProgramUpdateTime(System.currentTimeMillis());
                                channel.setPrograms(parser.parsePrograms(model.getSchedule(channel.getChannelId())));
                            }
                        }
                        catch (IOException e) {
                            SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null,
                                    "Channel " + channel.getChannelName() + " disabled:\n" + e.getMessage(),
                                    "Error",
                                    JOptionPane.ERROR_MESSAGE)
                            );
                            System.err.println("Failed to add listener to channel: " + channel.getChannelName());
                        }
                        return null;
                    }

                    @Override
                    protected void done() {
                        view.loadChannel(channel);
                    }
                };
                worker.execute();
            });
        });
    }
}
