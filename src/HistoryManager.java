import java.util.ArrayList;

public interface HistoryManager {
    void addTask(Task task);
    void remove(int id);
    ArrayList<Task> getHistory();
}
