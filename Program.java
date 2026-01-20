import com.google.gson.JsonObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class Program {

    private String programName;
    private String programSubName;
    private String programDescription;
    private long startTime;
    private long endTime;
    private String imageUrl;

    public Program(JsonObject programObject) {
        programName = programObject.get("title").getAsString();
        if (programObject.get("subtitle") != null) {
            programSubName = programObject.get("subtitle").getAsString();
        }
        else {
            programSubName = "";
        }
        programDescription = programObject.get("description").getAsString();
        startTime = Long.parseLong(programObject.get("starttimeutc").getAsString().replaceAll("\\D+", ""));
        endTime = Long.parseLong(programObject.get("endtimeutc").getAsString().replaceAll("\\D+", ""));
        if (programObject.get("imageurl") != null) {
            imageUrl = programObject.get("imageurl").getAsString();
        }
        else {
            imageUrl = null;
        }

    }

    public String getProgramName() {
        return programName;
    }

    public String getProgramSubName() {
        return programSubName;
    }

    public String getProgramDescription() {
        return programDescription;
    }

    public String getStartDate() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(startTime), ZoneId.of("Europe/Stockholm"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public String getEndDate() {
        LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(endTime), ZoneId.of("Europe/Stockholm"));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return dateTime.format(formatter);
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
