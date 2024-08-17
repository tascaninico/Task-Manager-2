import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLOutput;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FileBackedTasksManager extends InMemoryTaskManager{

    private Path path;

    public FileBackedTasksManager(String fileLocation){
        this.path = Paths.get(fileLocation);
    }

    public static FileBackedTasksManager loadFromFile(String name){
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(name);
        fileBackedTasksManager.readTasksFromFile();
        return fileBackedTasksManager;
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

    private void readTasksFromFile(){

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
                    LocalDateTime startTimeForTask = taskFromFile[6].equals("--:--") ? null :
                            LocalDateTime.parse(taskFromFile[6]); //, DateTimeDurationFormatter.dateTimeFormatter
                    Duration durationForTask = Duration.parse(taskFromFile[7]);

                    switch (taskFromFile[1]) {
                        case "TASK":
                            super.addNewTask(new Task(taskFromFile[2],taskFromFile[4],StatusOfTask.valueOf(taskFromFile[3]),
                                    startTimeForTask, durationForTask));
                            continue; //"id,type,name,status,description,epic, start time, duration, end time
                        case "EPIC":
                            mapOfEpicsID.put(Integer.parseInt(taskFromFile[0]), i);
                            super.addNewEpic(new Epic(taskFromFile[2], taskFromFile[4]));
                            continue;
                        case "SUBTASK":
                            super.addNewSubtaskForEpic(new Subtask(taskFromFile[2],taskFromFile[4],
                                    StatusOfTask.valueOf(taskFromFile[3]),mapOfEpicsID.get(Integer.parseInt(taskFromFile[5])),
                                    startTimeForTask, durationForTask));
                    }
                }
            }
        } catch (IOException exception){
            throw new ManagerSaveException(exception.getMessage());
        }
    }

    private void save() {
        try (FileWriter fileWriter = new FileWriter(path.toString(), StandardCharsets.UTF_8);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter)) {
            bufferedWriter.write("id,type,name,status,description,epic, start time, duration, end time\n");

            List<Task> allTasks = this.getTaskList();
            allTasks.addAll(this.getEpicList());
            allTasks.addAll(this.getSubtaskList());

            allTasks.sort(Comparator.comparingInt(Task::getId));

            allTasks.forEach(task -> {
                try {
                    bufferedWriter.write(toString(task));
                    bufferedWriter.newLine();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });

        } catch (IOException exception) {
            System.out.println(exception.getMessage());
        }
    }

    private static String toString(Task task){
        StringBuilder stringBuilder = new StringBuilder(task.getId() + "," + task.getTypeofTask().getString() + ","
                                                        + task.getName() + "," + task.getStatus() + ","
                                                        + task.getDescription());

        String startTimeForTask = task.getStartTime().isPresent() ? task.getStartTime().get().toString() : "--:--";
        String endTimeForTask = task.getStartTime().isPresent() ? task.getEndTime().get().toString(): "--:--";

        if (task instanceof Subtask){
            stringBuilder.append(",").append(((Subtask) task).getIdOfHostEpic()).append(",");

        } else {
            stringBuilder.append(",").append(" ").append(",");
        }
        stringBuilder.append(startTimeForTask).append(",").append(task.getDuration().toString()).append(",").append(endTimeForTask);

        return stringBuilder.toString();
    }
}