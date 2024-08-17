import org.junit.jupiter.api.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class EpicTest {

    static InMemoryTaskManager manager;

    static Epic epic0;

    static LocalDateTime packingTime;

    static Duration packingDuration;

    static Subtask subtask0;

    static LocalDateTime transportingTime;

    static Duration transportingDuration;

    static Subtask subtask1;

    static LocalDateTime unpackingTime;

    static Duration unpackingDuration;

    static Subtask subtask2;
    @BeforeAll
    static void InitialisingManagerAndTasks() {
        manager = new InMemoryTaskManager();
        epic0 = new Epic("Moving on", "Changing residence"); // create Epic
        manager.addNewEpic(epic0);

        packingTime = LocalDateTime.of(2024, Month.JULY, 12, 7, 0);
        packingDuration = Duration.ofSeconds(60 * 150);
        subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW,
                epic0.getId(), packingTime, packingDuration); //create Subtask

        transportingTime = LocalDateTime.of(2024, Month.JULY, 12, 9, 30);
        transportingDuration = Duration.ofSeconds(60 * 210);
        subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic0.getId(), transportingTime, transportingDuration); //создаем Subtask


        unpackingTime = LocalDateTime.of(2024, Month.JULY, 12, 13, 0);
        unpackingDuration = Duration.ofSeconds(60 * 240);
        subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW,
                epic0.getId(), unpackingTime, unpackingDuration); //создаем Subtask


    }

    @Test
    @Order(1)
    public void StatusOfEpicWithAllSubtasksNEW(){
        manager.addNewSubtaskForEpic(subtask0);
        manager.addNewSubtaskForEpic(subtask1);
        manager.addNewSubtaskForEpic(subtask2);
        Assertions.assertEquals(StatusOfTask.NEW, epic0.getStatus());
    }

    @Test
    @Order(2)
    public void StatusOfEpicWithAllSubtasksDone(){
        manager.deleteAllSubtasks();

        subtask0.setStatus(StatusOfTask.DONE);
        manager.addNewSubtaskForEpic(subtask0);

        subtask1.setStatus(StatusOfTask.DONE);
        manager.addNewSubtaskForEpic(subtask1);

        subtask2.setStatus(StatusOfTask.DONE);
        manager.addNewSubtaskForEpic(subtask2);

        Assertions.assertEquals(StatusOfTask.DONE, epic0.getStatus());
    }

    @Test
    @Order(3)
    public void StatusOfEpicWithAllSubtasksNEWandDONE(){
        manager.deleteAllSubtasks();

        subtask0.setStatus(StatusOfTask.DONE);
        manager.addNewSubtaskForEpic(subtask0);

        subtask1.setStatus(StatusOfTask.DONE);
        manager.addNewSubtaskForEpic(subtask1);

        subtask2.setStatus(StatusOfTask.NEW);
        manager.addNewSubtaskForEpic(subtask2);

        Assertions.assertEquals(StatusOfTask.IN_PROGRESS, epic0.getStatus());
    }

    @Test
    @Order(4)
    public void StatusOfEpicWithAllSubtasksNEWandDONEandIN_Progress() {
        manager.deleteAllSubtasks();

        subtask0.setStatus(StatusOfTask.DONE);
        manager.addNewSubtaskForEpic(subtask0);

        subtask1.setStatus(StatusOfTask.IN_PROGRESS);
        manager.addNewSubtaskForEpic(subtask1);

        subtask2.setStatus(StatusOfTask.NEW);
        manager.addNewSubtaskForEpic(subtask2);

        Assertions.assertEquals(StatusOfTask.IN_PROGRESS, epic0.getStatus());
    }

    @Test
    @Order(5)
    public void StatusOfEpicWithAllSubtasksIN_PROGESS(){
        manager.deleteAllSubtasks();

        subtask0.setStatus(StatusOfTask.IN_PROGRESS);
        manager.addNewSubtaskForEpic(subtask0);

        subtask1.setStatus(StatusOfTask.IN_PROGRESS);
        manager.addNewSubtaskForEpic(subtask1);

        subtask2.setStatus(StatusOfTask.IN_PROGRESS);
        manager.addNewSubtaskForEpic(subtask2);
        Assertions.assertEquals(StatusOfTask.IN_PROGRESS, epic0.getStatus());
    }

}