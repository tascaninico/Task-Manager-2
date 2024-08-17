import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TasksForSP8.csv");

        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        Task task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration); //create Task
        fileBackedTasksManager.addNewTask(task0); // time intersection: false


        LocalDateTime borshCookingTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 0);
        Duration borchCookingDuration = Duration.ofSeconds(60 * 60 * 2);
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh and cook a borsh",
                StatusOfTask.NEW, borshCookingTime, borchCookingDuration); //create Task
        fileBackedTasksManager.addNewTask(task1);// time intersection: false


        Duration cleaningHouseDuration = Duration.ofSeconds(60 * 60 * 3);
        Task task2 = new Task("House cleaning", "To clean the house properly",
                StatusOfTask.NEW, cleaningHouseDuration); //create Task
        fileBackedTasksManager.addNewTask(task2); // without starting time


        LocalDateTime workingOutTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 30);
        Duration workingOutDuration = Duration.ofSeconds(60 * 90);
        Task task3 = new Task("Working out", "To do some exercises",
                StatusOfTask.NEW, workingOutDuration); //create Task
        fileBackedTasksManager.addNewTask(task3); // time intersection: true


        Epic epic0 = new Epic("Moving on", "Changing residence"); // create Epic
        fileBackedTasksManager.addNewEpic(epic0);


        LocalDateTime packingTime = LocalDateTime.of(2024, Month.JULY, 12, 7, 0);
        Duration packingDuration = Duration.ofSeconds(60 * 150);
        Subtask subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW,
                epic0.getId(), packingTime, packingDuration); //create Subtask
        fileBackedTasksManager.addNewSubtaskForEpic(subtask0); // time intersection: false


        LocalDateTime transportingTime = LocalDateTime.of(2024, Month.JULY, 12, 9, 30);
        Duration transportingDuration = Duration.ofSeconds(60 * 210);
        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic0.getId(), transportingTime, transportingDuration); //создаем Subtask
        fileBackedTasksManager.addNewSubtaskForEpic(subtask1); // time intersection: true


        LocalDateTime unpackingTime = LocalDateTime.of(2024, Month.JULY, 12, 13, 0);
        Duration unpackingDuration = Duration.ofSeconds(60 * 240);
        Subtask subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW,
                epic0.getId(), unpackingTime, unpackingDuration); //создаем Subtask
        fileBackedTasksManager.addNewSubtaskForEpic(subtask2);// time intersection: false

        System.out.println("-----------------------------------------------------");

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TasksForSP8.csv");


        fileBackedTasksManager1.getTaskList().forEach(System.out::println);  // отображаем все Tasks

        System.out.println("All tasks printed!");
        System.out.println();

        fileBackedTasksManager1.getEpicList().forEach(System.out::println);  // отображаем все Epics

        System.out.println("All epics printed!");
        System.out.println();

        fileBackedTasksManager1.getSubtaskList().forEach(System.out::println); // отображаем все Subtasks

        System.out.println("All subtsasks printed!");
        System.out.println();


        System.out.println("----------------PrioritizedTasks---------------------");
        fileBackedTasksManager1.getPrioritizedTasks().forEach(System.out::println);

    }
}