import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class RadioModel {

    private String channelAPI = "http://api.sr.se/api/v2/channels?format=json&pagination=false";
    private String tableuAPI = "http://api.sr.se/api/v2/scheduledepisodes?channelid=";
    private String toJson = "&format=json&pagination=false";

    public RadioModel() {

    }

    public String getChannels() throws IOException {
        return fetchFromAPI(channelAPI);
    }

    public String getSchedule(int channelId) throws IOException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date = LocalDate.now(ZoneId.of("Europe/Stockholm"));

        LocalTime currentTime = LocalTime.now(ZoneId.of("Europe/Stockholm"));

        String fromDate = "&fromdate=", toDate = "&todate=";

        if (currentTime.isBefore(LocalTime.NOON)) {
            fromDate += date.minusDays(1).format(formatter);
            toDate += date.format(formatter);
        }
        else {
            fromDate += date.format(formatter);
            toDate += date.plusDays(1).format(formatter);
        }
        return fetchFromAPI(tableuAPI + channelId + toJson + fromDate + toDate);
    }

    private String fetchFromAPI(String apiUrl) throws IOException {
        try (InputStream is = new URL(apiUrl).openStream()) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder json = new StringBuilder();
            int c;
            while ((c = reader.read()) != -1) {
                json.append((char) c);
            }
            return json.toString();
        }
        catch (MalformedURLException e) {
            throw new MalformedURLException("Invalid URL: " + apiUrl);
        }
        catch (IOException e) {
            throw new IOException("Failed to fetch data from API: " + e.getMessage());
        }
    }
}
