import org.junit.jupiter.api.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class InMemoryTaskManagerTest {

    static Task task0;

    static Task task1;

    static Task task2;

    static InMemoryTaskManager manager;

    static Epic epic0;

    static Subtask subtask0;

    static Subtask subtask1;

    static Subtask subtask2;

    static Epic epic1;

    static Epic epic2;
    @BeforeAll
    static void InitialisingManagerAndTasks() {
        manager = new InMemoryTaskManager();

        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration); //create Task
        manager.addNewTask(task0); // time intersection: false


        LocalDateTime borshCookingTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 0);
        Duration borchCookingDuration = Duration.ofSeconds(60*60*2);
        task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh and cook a borsh",
                StatusOfTask.NEW, borshCookingTime, borchCookingDuration); //create Task
        manager.addNewTask(task1);// time intersection: false


        Duration cleaningHouseDuration = Duration.ofSeconds(60*60 * 3);
        task2 = new Task("House cleaning", "To clean the house properly",
                StatusOfTask.NEW, cleaningHouseDuration); //create Task
        manager.addNewTask(task2); // without starting time
        epic0 = new Epic("Moving on", "Changing residence"); // create Epic
        manager.addNewEpic(epic0);

        LocalDateTime packingTime = LocalDateTime.of(2024, Month.JULY, 12, 7, 0);
        Duration packingDuration = Duration.ofSeconds(60 * 150);
        subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW,
                epic0.getId(), packingTime, packingDuration); //create Subtask
        manager.addNewSubtaskForEpic(subtask0);// time intersection: false

        LocalDateTime transportingTime = LocalDateTime.of(2024, Month.JULY, 12, 9, 30);
        Duration transportingDuration = Duration.ofSeconds(60 * 210);
        subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic0.getId(), transportingTime, transportingDuration); //создаем Subtask
        manager.addNewSubtaskForEpic(subtask1);// time intersection: true

        LocalDateTime unpackingTime = LocalDateTime.of(2024, Month.JULY, 12, 13, 0);
        Duration unpackingDuration = Duration.ofSeconds(60 * 240);
        subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW,
                epic0.getId(), unpackingTime, unpackingDuration); //создаем Subtask
        manager.addNewSubtaskForEpic(subtask2);// time intersection: false

        epic1 = new Epic("House building", "To build a huge house");
        epic2 = new Epic("Plane building", "To build a plane");

    }
    @Test
    @Order(1)
    void getIntersection() {
        assertTrue(manager.getIntersection(new Task("Working out", "To do some exercises",
                StatusOfTask.NEW, LocalDateTime.of(2024, Month.AUGUST, 1, 13, 30), Duration.ofSeconds(60 * 90))));
        assertFalse(manager.getIntersection(new Task("Chilling out", "To have some rest",
                StatusOfTask.NEW, LocalDateTime.of(2022, Month.DECEMBER, 5, 10, 15), Duration.ofSeconds(60 * 120))));
        assertTrue(manager.getIntersection(new Task("Date", "To go out with girlfriend",
                StatusOfTask.NEW, LocalDateTime.of(2024, Month.AUGUST, 1, 10, 0), Duration.ofSeconds(60 * 180))));
    }

    @Test
    @Order(2)
    void getPrioritizedTasks() {
        List<Task> tasksAndSubtasksWithNoIntersection = List.of(subtask0, subtask2, task1, task0);
        Assertions.assertEquals(tasksAndSubtasksWithNoIntersection, manager.getPrioritizedTasks());
    }

    @Test
    @Order(3)
    void getTaskList() {
        ArrayList<Task> taskList = new ArrayList<>(List.of(task0, task1, task2));
        Assertions.assertEquals(taskList, manager.getTaskList());
        Assertions.assertEquals(3, manager.getTaskList().size());
    }

    @Test
    @Order(4)
    void getEpicList() {
        ArrayList<Epic> taskList = new ArrayList<>(List.of(epic0));
        Assertions.assertEquals(taskList, manager.getEpicList());
        Assertions.assertEquals(1, manager.getEpicList().size());
    }

    @Test
    @Order(5)
    void getSubtaskList() {
        ArrayList<Subtask> subtasksList = new ArrayList<>(List.of(subtask0, subtask1, subtask2));
        Assertions.assertEquals(subtasksList, manager.getSubtaskList());
        Assertions.assertEquals(3, manager.getSubtaskList().size());
    }

    @Test
    @Order(6)
    void addNewTask() {
        Task task3 = new Task("Eating out", "To visit lovely restorant",
                StatusOfTask.NEW, LocalDateTime.of(2024, Month.JULY, 20, 10, 15), Duration.ofSeconds(60 * 60));
        manager.addNewTask(task3);
        Assertions.assertTrue(manager.getTaskList().contains(task3));
        Assertions.assertEquals(4, manager.getTaskList().size());
    }

    @Test
    @Order(7)
    void addNewEpic() {
        manager.addNewEpic(epic1);
        Assertions.assertTrue(manager.getEpicList().contains(epic1));
        Assertions.assertEquals(2, manager.getEpicList().size());
    }

    @Test
    @Order(8)
    void addNewSubtaskForEpic() {
        Subtask subtask4 = new Subtask("Buying tools and materials", "It's essential to buy instruments and materials to build a house",
                StatusOfTask.NEW, epic1.getId(), LocalDateTime.of(2022, Month.SEPTEMBER, 3, 10, 0), Duration.ofSeconds(60 * 90));
        manager.addNewSubtaskForEpic(subtask4);
        Assertions.assertEquals(1, epic1.getSubtasksID().size());
        Assertions.assertTrue(epic1.getSubtasksID().contains(subtask4.getId()));
    }

    @Test
    @Order(9)
    void updateTask() {
        task0.setStatus(StatusOfTask.IN_PROGRESS);
        manager.updateTask(task0);
        Assertions.assertEquals(StatusOfTask.IN_PROGRESS, manager.getTaskByID(task0.getId()).getStatus());
    }

    @Test
    @Order(10)
    void updateEpic() {
        epic1.setName("Career building");
        manager.updateEpic(epic1);
        Assertions.assertEquals("Career building", manager.getEpicByID(epic1.getId()).getName());
    }

    @Test
    @Order(11)
    void updateSubtask() {
        subtask2.setDescription("Things can be packed for a next few weeks");
        manager.updateSubtask(subtask2);
        Assertions.assertEquals("Things can be packed for a next few weeks",
                manager.getSubtaskByID(subtask2.getId()).getDescription());
    }

    @Test
    @Order(12)
    void getTaskByID() {
        Assertions.assertEquals(task1, manager.getTaskByID(task1.getId()));
    }

    @Test
    @Order(13)
    void getEpicByID() {
        Assertions.assertEquals(epic0, manager.getEpicByID(epic0.getId()));
    }

    @Test
    @Order(14)
    void getSubtaskByID() {
        Assertions.assertEquals(subtask2, manager.getSubtaskByID(subtask2.getId()));
    }

    @Test
    @Order(15)
    void removeTaskByID() {
        manager.removeTaskByID(task0.getId());
        Assertions.assertEquals(3, manager.getTaskList().size());
        assertFalse(manager.getTaskList().contains(task0));
    }

    @Test
    @Order(16)
    void removeEpicByID() {
        InMemoryTaskManager manager = new InMemoryTaskManager();

        Epic epic4 = new Epic("House building", "To build a huge house");
        manager.addNewEpic(epic4);

        Subtask subtask4 = new Subtask("Buying tools and materials", "It's essential to buy instruments and materials to build a house",
                StatusOfTask.NEW, epic4.getId(), LocalDateTime.of(2022, Month.SEPTEMBER, 3, 10, 0), Duration.ofSeconds(60 * 90));
        manager.addNewSubtaskForEpic(subtask4);

        manager.removeEpicByID(epic4.getId());

        Assertions.assertTrue(manager.getEpicList().isEmpty());
        Assertions.assertFalse(manager.getEpicList().contains(epic4));
        Assertions.assertFalse(manager.getSubtaskList().contains(subtask4));
    }

    @Test
    @Order(17)
    void removeSubtaskByID() {
        manager.removeSubtaskByID(subtask1.getId());
        Assertions.assertEquals(2, manager.getSubtasksOfEpic(epic0.getId()).size());
        assertFalse(manager.getSubtaskList().contains(subtask1));
    }

    @Test
    @Order(18)
    void getSubtasksOfEpic() {
        ArrayList<Subtask> arrayList = new ArrayList<>();
        arrayList.add(subtask0);
        arrayList.add(subtask2);
        Assertions.assertEquals(arrayList, manager.getSubtasksOfEpic(epic0.getId()));
    }

    @Test
    @Order(19)
    void deleteAllTasks() {
        manager.deleteAllTasks();
        Assertions.assertTrue(manager.getTaskList().isEmpty());
    }

    @Test
    @Order(20)
    void deleteAllEpics() {
        manager.deleteAllEpics();
        Assertions.assertTrue(manager.getEpicList().isEmpty());

    }

    @Test
    @Order(21)
    void deleteAllSubtasks() {
        Epic epic2 = new Epic("Playing soccer", "To meet some friends and play soccer");
        Subtask subtask4 = new Subtask("Making appointment", "To call friends and to choose a date and time for playing",
                StatusOfTask.NEW, epic2.getId(), LocalDateTime.of(2023, Month.APRIL, 5, 13, 0), Duration.ofSeconds(60*120));
        manager.addNewEpic(epic2);
        manager.addNewSubtaskForEpic(subtask4);
        manager.deleteAllSubtasks();
        Assertions.assertTrue(manager.getSubtaskList().isEmpty());

    }
}