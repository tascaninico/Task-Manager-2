import java.time.Duration;
import java.time.format.DateTimeFormatter;

public class DateTimeDurationFormatter {
    public static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH-mm");

    public static String DurationFormatter(Duration duration){
        long hours = duration.toHoursPart();
        long minutes = duration.toMinutesPart();

        return String.format("%02d:%02d", hours, minutes);
    }

    public static Duration fromStringToDuration(String str){
        long hours = Long.parseLong(str.substring(0,2));
        long minutes = Long.parseLong(str.substring(3,5));
        return Duration.ofMinutes(hours * 60 + minutes);
    }
}
