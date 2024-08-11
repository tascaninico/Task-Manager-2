import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private Path path;

    public FileBackedTasksManager(String fileLocation){
        this.path = Paths.get(fileLocation);
    }

//    public static FileBackedTasksManager loadFromFile(String name){
//        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(name);
//        fileBackedTasksManager.readTasksFromFile();
//        return fileBackedTasksManager;
//    }
    public static FileBackedTasksManager loadFromFile(String name){
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(name);
        fileBackedTasksManager.readTasksFromFile_1();
        return fileBackedTasksManager;
    }

//    private void readTasksFromFile(){
//
//        List<String> listOfTasks = new ArrayList<>();
//
//        try (FileReader fileReader = new FileReader(path.toString(), StandardCharsets.UTF_8);
//            BufferedReader bufferedReader = new BufferedReader(fileReader)){
//
//            while (bufferedReader.ready()){
//                listOfTasks.add(bufferedReader.readLine());
//            }
//            if (!listOfTasks.isEmpty()) {
//                listOfTasks.remove(0);
//                for (String str_task : listOfTasks) {
//                    Task task = fromString(str_task);
//                    if (task instanceof Epic) {
//                        super.addNewEpic((Epic) task);
//                    } else if (task instanceof Subtask) {
//                        super.addNewSubtaskForEpic((Subtask) task);
//                    } else {
//                        super.addNewTask(task);
//                    }
//                }
//            }
//        } catch (IOException exception){
//            throw new ManagerSaveException(exception.getMessage());
//        }
//
//    }

    private void readTasksFromFile_1(){

        List<String> listOfTasks = new ArrayList<>();

        try (FileReader fileReader = new FileReader(path.toString(), StandardCharsets.UTF_8);
             BufferedReader bufferedReader = new BufferedReader(fileReader)){

            while (bufferedReader.ready()){
                listOfTasks.add(bufferedReader.readLine());
            }
            if (!listOfTasks.isEmpty()) {
                listOfTasks.remove(0);
                Map<Integer, Integer> mapOfEpicsID = new HashMap<>();
                for (int i = 1;  i < listOfTasks.size()+1; ++i){
                    String[] taskFromFile = listOfTasks.get(i-1).split(",");
                    switch (taskFromFile[1]) {
                        case "TASK":
                            super.addNewTask(new Task(taskFromFile[2],taskFromFile[4],StatusOfTask.valueOf(taskFromFile[3])));
                            continue;
                        case "EPIC":
                            mapOfEpicsID.put(Integer.parseInt(taskFromFile[0]), i);
                            super.addNewEpic(new Epic(taskFromFile[2], taskFromFile[4]));
                            continue;
                        case "SUBTASK":
                            super.addNewSubtaskForEpic(new Subtask(taskFromFile[2],taskFromFile[4],
                                StatusOfTask.valueOf(taskFromFile[3]),mapOfEpicsID.get(Integer.parseInt(taskFromFile[5]))));

                    }
                }
            }
        } catch (IOException exception){
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    private Task fromString(String value){
        String[] taskFromFile = value.split(",");
        switch(taskFromFile[1]){
            case "TASK":
                return new Task(taskFromFile[2],taskFromFile[4],StatusOfTask.valueOf(taskFromFile[3]));
            case "EPIC":
                Epic epic = new Epic(taskFromFile[2], taskFromFile[4]);
                epic.setStatus(StatusOfTask.valueOf(taskFromFile[3]));
                return epic;
            case "SUBTASK":
                return new Subtask(taskFromFile[2],taskFromFile[4],StatusOfTask.valueOf(taskFromFile[3]), Integer.parseInt(taskFromFile[5]));
        }
        return null;
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
    public void updateSubtask(Subtask subtask) {
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

    private void save() {
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

    private static String toString(Task task){ // tests passed!
        StringBuilder stringBuilder = new StringBuilder(task.getId() + "," + task.getTypeofTask().getString() + ","
                                                        + task.getName() + "," + task.getStatus() + ","
                                                        + task.getDescription());
        if (task instanceof Subtask){
            stringBuilder.append("," + ((Subtask)task).getIdOfHostEpic());

        }
        return stringBuilder.toString();
    }

}