import com.google.gson.JsonObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Channel {

    private int channelId;
    private String channelName;
    private String channelDescription;
    private String logoUrl;
    private String channelType;
    private List<Program> programs = new ArrayList<>();
    private long programUpdateTime = 0;

    public Channel(JsonObject channelObject) {
        channelId = channelObject.get("id").getAsInt();
        channelName = channelObject.get("name").getAsString();
        channelDescription = channelObject.get("tagline").getAsString();
        logoUrl = channelObject.get("image").getAsString();
        channelType = channelObject.get("channeltype").getAsString();
    }

    public int getChannelId() {
        return channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getChannelDescription() {
        return channelDescription;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public String getChannelType() {
        return channelType;
    }

    public synchronized List<Program> getPrograms() {
        return programs;
    }

    public synchronized void setPrograms(List<Program> programList) {
        programs = programList;
    }

    public synchronized long getProgramUpdateTime() {
        return programUpdateTime;
    }

    public synchronized String getProgramUpdateDate() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(programUpdateTime), ZoneId.of("Europe/Stockholm"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public synchronized void setProgramUpdateTime(long time) {
        programUpdateTime = time;
    }
}
