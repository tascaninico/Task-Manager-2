import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class HttpTaskServer {

    public static void main(String[] args) throws IOException {
//        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        TaskManager manager = Managers.getDefault();

        LocalDateTime catFeedingTime = LocalDateTime.of(2024, Month.SEPTEMBER, 3, 20, 0);
        Duration catFeedingDuration = Duration.ofSeconds(120);
        Task task0 = new Task("Cat feeding", "To fill a cat's dish",
                StatusOfTask.NEW, catFeedingTime, catFeedingDuration);
        manager.addNewTask(task0);


        LocalDateTime borshCookingTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 0);
        Duration borchCookingDuration = Duration.ofSeconds(60*60*2);
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh and cook a borsh",
                StatusOfTask.NEW, borshCookingTime, borchCookingDuration);
        manager.addNewTask(task1);


        Duration cleaningHouseDuration = Duration.ofSeconds(60*60 * 3);
        Task task2 = new Task("House cleaning", "To clean the house properly",
                StatusOfTask.NEW, cleaningHouseDuration);
        manager.addNewTask(task2);
        Epic epic0 = new Epic("Moving on", "Changing residence");
        manager.addNewEpic(epic0);

        LocalDateTime packingTime = LocalDateTime.of(2024, Month.JULY, 12, 7, 0);
        Duration packingDuration = Duration.ofSeconds(60 * 150);
        Subtask subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW,
                epic0.getId(), packingTime, packingDuration);
        manager.addNewSubtaskForEpic(subtask0);

        LocalDateTime transportingTime = LocalDateTime.of(2024, Month.JULY, 12, 9, 30);
        Duration transportingDuration = Duration.ofSeconds(60 * 210);
        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic0.getId(), transportingTime, transportingDuration);
//        manager.addNewSubtaskForEpic(subtask1);

        LocalDateTime unpackingTime = LocalDateTime.of(2024, Month.JULY, 12, 13, 0);
        Duration unpackingDuration = Duration.ofSeconds(60 * 240);
        Subtask subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW,
                epic0.getId(), unpackingTime, unpackingDuration);
        manager.addNewSubtaskForEpic(subtask2);

        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));

        httpServer.start();
        System.out.println("Server started on 8080 port");

//        LocalDateTime workingOutTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 30);
        Duration workingOutDuration = Duration.ofSeconds(60 * 90);
        Task task3 = new Task("Working out", "To do some exercises",
                StatusOfTask.NEW, workingOutDuration); //create Task
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String task3InJson = gson.toJson(task3);
        System.out.println(task3InJson);
        System.out.println();
//        String subtask1InJson = gson.toJson(subtask1);
//        System.out.println(subtask1InJson);


        Epic epic1 = new Epic("Plane building", "To build a plane");

        manager.addNewEpic(epic1);

        LocalDateTime investorsTime = LocalDateTime.of(2020, Month.DECEMBER, 1, 10, 0);
        Duration investorsDuration = Duration.ofMinutes(60 * 48);
        Subtask subtask3 = new Subtask("Investing", "Need to find investors who is ready to support the project", StatusOfTask.NEW,
                epic1.getId(), investorsTime, investorsDuration);
//        System.out.println(gson.toJson(subtask3));
        manager.addNewSubtaskForEpic(subtask3);

        LocalDateTime buildingTime = LocalDateTime.of(2020, Month.DECEMBER, 3, 12, 30);
        Duration buildingDuration = Duration.ofMinutes(60 * 24 * 10);
        Subtask subtask4 = new Subtask("Building", "Building of plane", StatusOfTask.NEW,
                epic1.getId(), buildingTime, buildingDuration);
//        System.out.println(gson.toJson(subtask4));
        manager.addNewSubtaskForEpic(subtask4);

        LocalDateTime testingTime = LocalDateTime.of(2020, Month.DECEMBER, 14, 7, 20);
        Duration testingDuration = Duration.ofMinutes(60 * 24 * 10);
        Subtask subtask5 = new Subtask("Testing", "To conduct a test flight", StatusOfTask.NEW,
                epic1.getId(), testingTime, testingDuration);

        LocalDateTime assignmentTime = LocalDateTime.of(2020, Month.DECEMBER, 5, 13, 0);
        Duration assignmentDuration = Duration.ofSeconds(60*60*10);
        Task task4 = new Task("Assigment time", "To pass assigment",
                StatusOfTask.NEW, assignmentTime, assignmentDuration);


        System.out.println(gson.toJson(task4));


//        System.out.println(gson.toJson(subtask5));
        manager.addNewSubtaskForEpic(subtask5);
//        System.out.println(gson.toJson(epic1));
        manager.getTaskList().forEach(System.out::println);
        manager.getEpicList().forEach(System.out::println);
        manager.getSubtaskList().forEach(System.out::println);


    }
}
