import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private Path path;

    public FileBackedTasksManager(String fileLocation){
        this.path = Paths.get(fileLocation);
    }

    static String historyToString(HistoryManager manager){
        List<Task> history = manager.getHistory();
        StringBuilder stringBuilder = new StringBuilder();
        for (Task task: history){
            stringBuilder.append(String.valueOf(task.getId()));
            stringBuilder.append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length()-1);
        return stringBuilder.toString();
    }

    void save(){
        List<Task> allKindsOfTasksById = new ArrayList<>();
        List<Task> allTasks = this.getTaskList();
        allTasks.addAll(this.getEpicList());
        allTasks.addAll(this.getSubtaskList());
        for (Task task: allTasks){
                

        }
    }

    static List<Integer> historyFromString(String value){


        return null;
    }


    public String toString(Task task){
        StringBuilder stringBuilder = new StringBuilder(task.getId() + "," + task.getTypeofTask().getString() + ","
                                                        + task.getName() + "," + task.getStatus() + ","
                                                        + task.getDescription());
        if (task instanceof Subtask){
            stringBuilder.append("," + ((Subtask)task).getIdOfHostEpic());

        }
        return stringBuilder.toString();
    }

    public Task fromString(String value){
        String[] taskFromFile = value.split(",");
        switch(taskFromFile[1]){
            case "TASK":
                return new Task(taskFromFile[2],taskFromFile[4],StatusOfTask.valueOf(taskFromFile[3]));
            case "EPIC":
                Epic epic = new Epic(taskFromFile[2], taskFromFile[4]);
                epic.setStatus(StatusOfTask.valueOf(taskFromFile[3]));
                return epic;
            case "SUBTASK":
                return new Subtask(taskFromFile[2],taskFromFile[4],StatusOfTask.valueOf(taskFromFile[3]), Integer.valueOf(taskFromFile[5]));
        }
        return null;
    }

}