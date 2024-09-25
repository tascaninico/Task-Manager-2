import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

public class Epic extends Task{

    private ArrayList<Integer> subtasksID = new ArrayList<>();

    private Optional<LocalDateTime> endTime;

    public Epic(String name, String description) {
        super(name, description);

    }

    public ArrayList<Integer> getSubtasksID() {
        return subtasksID;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = Optional.ofNullable(endTime);
    }

    public Optional<LocalDateTime> getEndTime(){
        return endTime;
    }

    @Override
    public KindOfTask getTypeofTask() {
        return KindOfTask.EPIC;
    }

    public String toString(){
        String stTime = getStartTime().map(localDateTime -> localDateTime.format(DateTimeDurationFormatter.dateTimeFormatter)).orElse("--:--");
        String enTime = endTime.map(localDateTime -> localDateTime.format(DateTimeDurationFormatter.dateTimeFormatter)).orElse("--:--");
        String dur = getDuration() != null ? DateTimeDurationFormatter.DurationFormatter(getDuration()) : "--:--";

        return "Epic{" +
                "name = " + "'" + super.getName() + "'"
                + ", description = " + "'" + super.getDescription() + "'"
                + ", id = " + "'" + super.getId() +"'"
                + ", status = " + "'" + super.getStatus() + "'"
                + ", startTime = " + "'" + stTime + "'" +  ", duration = " + "'"
                + dur + "'" +", endTime = " + "'" + enTime + "'}";
    }
}
