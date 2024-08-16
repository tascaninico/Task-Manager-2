import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.time.Duration;
import java.time.LocalDateTime;

public class Task {

    private String name;

    private String description;

    private int id = 0;

    private StatusOfTask status;

    private Optional<LocalDateTime> startTime;

    private Duration duration;

    public Optional<LocalDateTime> getEndTime(){
        return startTime.map(localDateTime -> localDateTime.plus(duration));
    }

    public Task(String name, String description, StatusOfTask status, LocalDateTime startTime, Duration duration){
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = Optional.ofNullable(startTime);
        this.duration = duration;
    }

    public Task(String name, String description, StatusOfTask status, Duration duration){
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = Optional.empty();
        this.duration = duration;
    }

    protected Task(String name, String description){
        this.name = name;
        this.description = description;
        this.startTime = Optional.empty();
        this.duration = Duration.ofSeconds(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Optional<LocalDateTime> getStartTime(){
        return startTime;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setStartTime(LocalDateTime startTime){
        this.startTime = Optional.ofNullable(startTime);
    }

    public StatusOfTask getStatus() {
        return status;
    }

    public void setStatus(StatusOfTask status) {
        this.status = status;
    }

    public KindOfTask getTypeofTask(){
        return KindOfTask.TASK;
    }

    @Override
    public String toString(){
        String stTime = startTime.map(localDateTime -> localDateTime.format(DateTimeDurationFormatter.dateTimeFormatter)).orElse("--:--");
        String enTime = startTime.map(localDateTime -> localDateTime.plus(duration).format(DateTimeDurationFormatter.dateTimeFormatter)).orElse("--:--");

        return "Task{" +
                    "name = " + "'" + name + "'"
                    + ", description = " + "'" + description + "'"
                    + ", id = " + "'" + id +"'"
                    + ", status = " + "'" + status + "'" + ", startTime = " + "'"
                    + stTime + "'" + ", duration = " + "'" + DateTimeDurationFormatter.DurationFormatter(duration) + "'" +
                    ", endTime = " + enTime + "'}";
    }
}
