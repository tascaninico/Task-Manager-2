import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Subtask> getSubtaskList();

    void addNewTask(Task task);

    void addNewEpic(Epic epic);

    void addNewSubtaskForEpic(Subtask subtask);

    List<Task> getHistory();

    void updateTask(Task task);

    void updateEpic(Epic epic);

    void updateSubtask(Subtask subtask);

    Task getTaskByID(int id);

    Epic getEpicByID(int id);

    Subtask getSubtaskByID(int id);

    void removeTaskByID(Integer id);

    void removeEpicByID(Integer id);

    void removeSubtaskByID(Integer id);

    ArrayList<Subtask> getSubtasksOfEpic(Integer epicID);

    void deleteAllTasks();

    void deleteAllEpics();

    void deleteAllSubtasks();


}
