import org.junit.jupiter.api.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends InMemoryTaskManagerTest {

    static FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TestsForSP8.csv");;

    static Subtask subtask3;

    @Test
    void fileBackedTasksManagerSpecialMethodsTest() { // Passing this test means that
                                                     // such methods as like loadFromFile() and private methods
                                                    // such as save(), readTasksFromFile() and toString(Task) work properly
        fileBackedTasksManager.addNewTask(task0);
        fileBackedTasksManager.addNewEpic(epic2);
        subtask3 = new Subtask("Investment", "To find money for plane construction",
                StatusOfTask.NEW, epic2.getId(), LocalDateTime.of(2022, Month.NOVEMBER, 20, 7,0), Duration.ofSeconds(60*60*48));
        fileBackedTasksManager.addNewSubtaskForEpic(subtask3);

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TestsForSP8.csv");
        List<Task> allTasks = new ArrayList<>();
        allTasks.addAll(fileBackedTasksManager1.getTaskList());
        allTasks.addAll(fileBackedTasksManager1.getEpicList());
        allTasks.addAll(fileBackedTasksManager1.getSubtaskList());

        List<Task> taskList = new ArrayList<>(List.of(task0, epic2, subtask3));

        assertEquals(allTasks.get(0).getName(), taskList.get(0).getName());
        assertEquals(allTasks.get(1).getName(), taskList.get(1).getName());
        assertEquals(allTasks.get(2).getName(), taskList.get(2).getName());
    }
}