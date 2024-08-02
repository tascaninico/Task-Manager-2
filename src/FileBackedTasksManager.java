import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager{
    private Path path;

    public FileBackedTasksManager(String fileLocation){
        this.path = Paths.get(fileLocation);
    }

    @Override
    public void addNewTask(Task task){
        super.addNewTask(task);
        save();
    }

    @Override
    public void addNewEpic(Epic task){
        super.addNewEpic(task);
        save();
    }

    @Override
    public void addNewSubtaskForEpic(Subtask task){
        super.addNewSubtaskForEpic(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) { //?????
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void removeTaskByID(Integer id) {
        super.removeTaskByID(id);
        save();
    }

    @Override
    public void removeEpicByID(Integer id) {
        super.removeEpicByID(id);
        save();
    }

    @Override
    public void removeSubtaskByID(Integer id) {
        super.removeSubtaskByID(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }

    public void save() {
        try (FileWriter fileWriter = new FileWriter(path.toString(), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("id,type,name,status,description,epic\n");

            List<Task> allTasks = this.getTaskList();
            allTasks.addAll(this.getEpicList());
            allTasks.addAll(this.getSubtaskList());

            ArrayList<Integer> all_ID = new ArrayList<>(0);

            for (int i = 0; i < allTasks.size(); i++){
                all_ID.add(i, allTasks.get(i).getId());
            }
            all_ID.trimToSize();

            Collections.sort(all_ID);

            for (Integer id: all_ID){
                for (Task task: allTasks){
                    if (id == task.getId()) {
                        bufferedWriter.write(toString(task));
                        bufferedWriter.newLine();
                    }
                }
            }

            } catch (IOException exception) {
            System.out.println(exception.getMessage());
            }
    }


    //    static String historyToString(HistoryManager manager){
//        List<Task> history = manager.getHistory();
//        StringBuilder stringBuilder = new StringBuilder();
//        for (Task task: history){
//            stringBuilder.append(String.valueOf(task.getId()));
//            stringBuilder.append(",");
//        }
//        stringBuilder.deleteCharAt(stringBuilder.length()-1);
//        return stringBuilder.toString();
//    }

//    void save(){
//        List<Task> allKindsOfTasksById = new ArrayList<>();
//        List<Task> allTasks = this.getTaskList();
//        allTasks.addAll(this.getEpicList());
//        allTasks.addAll(this.getSubtaskList());
//        for (Task task: allTasks){
//
//        }
//    }

//    static List<Integer> historyFromString(String value){
//
//
//        return null;
//    }

    public static String toString(Task task){ // tests passed!
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