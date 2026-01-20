import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {

    private List<Channel> channels = new ArrayList<>();
    private List<String> channelTypes = new ArrayList<>();

    public JSONParser() {

    }

    public List<Channel> parseChannels(String json) {
        try {
            JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
            JsonArray channelsArray = jsonObject.getAsJsonArray("channels");

            for (int i = 0; i < channelsArray.size(); i++) {
                JsonObject channelObject = channelsArray.get(i).getAsJsonObject();
                channels.add(new Channel(channelObject));

                String channelType = channelObject.get("channeltype").getAsString();
                if (!channelTypes.contains(channelType)) {
                    channelTypes.add(channelType);
                }
            }
        }
        catch (Exception e) {
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                    "Failed to read channel data:\n" + e.getMessage() + "\nProgram needs rebooting.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
                System.err.println("Program failed to read channels data.");
                System.exit(1);
            });
        }
        return channels;
    }

    public List<String> getChannelTypes() {
        return channelTypes;
    }

    public List<Program> parsePrograms(String json) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray programsArray = jsonObject.getAsJsonArray("schedule");
        List<Program> programs = new ArrayList<>();

        try {
            for (int i = 0; i < programsArray.size(); i++) {
                JsonObject programObject = programsArray.get(i).getAsJsonObject();
                if (checkTime(programObject)) {
                    programs.add(new Program(programObject));
                }

            }
        }
        catch (Exception e) {
            JOptionPane.showMessageDialog(null,
                    "Failed to read program data:\n" + e.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            System.err.println("Could not parse information about a program: " + e.getMessage());
        }
        return programs;
    }

    private boolean checkTime(JsonObject programObject) {
        final long twelveHrsMs = 12 * 60 * 60 * 1000; //12 hours in milliseconds
        long currentTime = System.currentTimeMillis();
        long startTime = Long.parseLong(programObject.get("starttimeutc").getAsString().replaceAll("\\D+", ""));
        long endTime = Long.parseLong(programObject.get("endtimeutc").getAsString().replaceAll("\\D+", ""));
        return startTime < (currentTime + twelveHrsMs) && endTime > (currentTime - twelveHrsMs);
    }
}
