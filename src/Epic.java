import java.util.HashMap;
import java.util.ArrayList;

public class Epic extends Task{
//    private HashMap<Integer, Subtask> subtasks;
    private ArrayList<Subtask> subtasks = new ArrayList<>();
    public Epic(String name, String description,
                ArrayList<Subtask> subtasks) {
        super(name, description);
        this.subtasks = subtasks;
        this.setStatus(toDefineStatusOfEpic());
    }


    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }
    public String toDefineStatusOfEpic(){
        if (subtasks.isEmpty())
            return "NEW";

        int newCount = 0;
        int doneCount = 0;

        for (Subtask subtask: subtasks){
            if (subtask.getStatus().equals("NEW"))
                newCount++;
            else if (subtask.getStatus().equals("DONE"))
                doneCount++;
            if (newCount != 0 && doneCount != 0){
                return "IN_PROGRESS";
            }
        }
        if (newCount == 0)
            return "DONE";
        else
            return "NEW";
    }


    public String toString(){
        String result = "Epic{" +
                "name = " + "'" + super.getName() + "'"
                + ", description = " + "'" + super.getDescription() + "'"
                + ", id = " + "'" + super.getId() +"'"
                + ", status = " + "'" + super.getStatus() + "'" + "}\n"
                + "Subtasks of the epic:\n";

                for (Subtask num: subtasks){
                    result += num.toString() + "\n";
                }
        return result;
    }

}
