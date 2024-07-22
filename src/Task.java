public class Task {

    private String name;

    private String description;

    private int id;

    private StatusOfTask status;

    public Task(String name, String description, StatusOfTask status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    protected Task(String name, String description){
        this.name = name;
        this.description = description;
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
        return "Task{" +
                    "name = " + "'" + name + "'"
                    + ", description = " + "'" + description + "'"
                    + ", id = " + "'" + id +"'"
                    + ", status = " + "'" + status + "'" + "}";
    }

}
