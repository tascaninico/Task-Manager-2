import java.util.ArrayList;
public class Epic extends Task{
    private ArrayList<Integer> subtasksID = new ArrayList<>();
    public Epic(String name, String description) {
        super(name, description);
    }
    public ArrayList<Integer> getSubtasksID() {
        return subtasksID;
    }

    @Override
    public KindOfTask getTypeofTask() {
        return KindOfTask.EPIC;
    }

    public String toString(){
        String result = "Epic{" +
                "name = " + "'" + super.getName() + "'"
                + ", description = " + "'" + super.getDescription() + "'"
                + ", id = " + "'" + super.getId() +"'"
                + ", status = " + "'" + super.getStatus() + "'" + "}";

        return result;
    }
}
