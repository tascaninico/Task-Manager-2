import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;

public class Subtask extends Task {
    private int idOfHostEpic;

    public Subtask(String name, String description, StatusOfTask status, int idOfHostEpic, LocalDateTime startTime, Duration duration) {
        super(name, description, status, startTime, duration);
        this.idOfHostEpic = idOfHostEpic;
    }

    public int getIdOfHostEpic() {
        return idOfHostEpic;
    }

    public void setIdOfHostEpic(int idOfHostEpic) {
        this.idOfHostEpic = idOfHostEpic;
    }

    @Override
    public KindOfTask getTypeofTask(){
        return KindOfTask.SUBTASK;
    }

    @Override
    public String toString(){
        String stTime = getStartTime().map(localDateTime -> localDateTime.format(DateTimeDurationFormatter.dateTimeFormatter)).orElse("--:--");
        String enTime = getStartTime().map(localDateTime -> localDateTime.plus(getDuration()).format(DateTimeDurationFormatter.dateTimeFormatter)).orElse("--:--");

        return "Subtask{" +
                "name = " + "'" + super.getName() + "'"
                + ", description = " + "'" + super.getDescription()+ "'"
                + ", id = " + "'" + super.getId() + "'"
                + ", status = " + "'" + super.getStatus() + "'" +
                ", idOfHostEpic = " + "'" + idOfHostEpic + "'" +
                ", startTime = " + stTime + ", duration = " + "'" +
                DateTimeDurationFormatter.DurationFormatter(getDuration()) + "'" + ", endTime = " + "'" + enTime + "'}";
    }
}


