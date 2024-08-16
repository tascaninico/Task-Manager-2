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
        Duration borchCookingDuration = Duration.ofSeconds(60*60*2);
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh and cook a borsh",
                StatusOfTask.NEW, borshCookingTime, borchCookingDuration); //create Task
        fileBackedTasksManager.addNewTask(task1);// time intersection: false


        Duration cleaningHouseDuration = Duration.ofSeconds(60*60 * 3);
        Task task2 = new Task("House cleaning", "To clean the house properly", StatusOfTask.NEW, cleaningHouseDuration); //create Task
        fileBackedTasksManager.addNewTask(task2); // without starting time


        LocalDateTime workingOutTime = LocalDateTime.of(2024, Month.AUGUST, 1, 13, 30);
        Duration workingOutDuration = Duration.ofSeconds(60*90);
        Task task3 = new Task("Working out", "To do some exercises", StatusOfTask.NEW, workingOutDuration); //create Task
        fileBackedTasksManager.addNewTask(task3); // time intersection: true


        Epic epic0 = new Epic("Moving on", "Changing residence"); // create Epic
        fileBackedTasksManager.addNewEpic(epic0);


        LocalDateTime packingTime = LocalDateTime.of(2024, Month.JULY, 12, 7, 0);
        Duration packingDuration = Duration.ofSeconds(60*150);
        Subtask subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW,
                epic0.getId(), packingTime, packingDuration); //create Subtask
        fileBackedTasksManager.addNewSubtaskForEpic(subtask0); // time intersection: false


        LocalDateTime transportingTime = LocalDateTime.of(2024, Month.JULY, 12, 9, 30);
        Duration transportingDuration = Duration.ofSeconds(60*210);
        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW,
                epic0.getId(), transportingTime, transportingDuration); //создаем Subtask
        fileBackedTasksManager.addNewSubtaskForEpic(subtask1); // time intersection: true


        LocalDateTime unpackingTime = LocalDateTime.of(2024, Month.JULY, 12, 13, 0);
        Duration unpackingDuration = Duration.ofSeconds(60*240);
        Subtask subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW,
                epic0.getId(), unpackingTime, unpackingDuration); //создаем Subtask
        fileBackedTasksManager.addNewSubtaskForEpic(subtask2);// time intersection: false

        System.out.println("-----------------------------------------------------");

        FileBackedTasksManager fileBackedTasksManager1 = FileBackedTasksManager.loadFromFile("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TasksForSP8.csv");

        for (Task task : fileBackedTasksManager1.getTaskList()) { // отображаем все Tasks
            System.out.println(task);
        }

        System.out.println("All tasks printed!");
        System.out.println();

        for (Task task : fileBackedTasksManager1.getEpicList()) { // отображаем все Epics
            System.out.println(task);
        }

        System.out.println("All epics printed!");
        System.out.println();

        for (Task task : fileBackedTasksManager1.getSubtaskList()) { // отображаем все Subtasks
            System.out.println(task);
        }

        System.out.println("All subtsasks printed!");
        System.out.println();

        System.out.println("----------------PrioritizedTasks---------------------");
        for (Task task : fileBackedTasksManager1.getPrioritizedTasks()){
            System.out.println(task);
        }



//        manager0.addNewTask(task0);// менеджер добавляет Task
//        manager0.addNewTask(task1);// менеджер добавляет Task


//        Epic epic1 = new Epic("Family building", "To build a large family");//создаем Epic

//        manager0.addNewEpic(epic0);// менеджер добавляет Epic
//        manager0.addNewEpic(epic1);// менеджер добавляет Epic

//
//        manager0.addNewSubtaskForEpic(subtask0); // добавляем эпику субзадачу
//        manager0.addNewSubtaskForEpic(subtask1); // добавляем эпику субзадачу
//        manager0.addNewSubtaskForEpic(subtask2); // добавляем эпику субзадачу

//        Subtask subtask3 = new Subtask("Looking for a partner", "Need to look for an appropriate partner", StatusOfTask.NEW, epic1.getId());
//        Subtask subtask4 = new Subtask("Making a proposal to the partner", "Need to get marry to create a family", StatusOfTask.NEW, epic1.getId());
//        Subtask subtask5 = new Subtask("Children", "To give a birth to a few children", StatusOfTask.NEW, epic1.getId());
//
//        manager0.addNewSubtaskForEpic(subtask3); // добавляем эпику субзадачу
//        manager0.addNewSubtaskForEpic(subtask4); // добавляем эпику субзадачу
//        manager0.addNewSubtaskForEpic(subtask5); // добавляем эпику субзадачу

//        task0.setStatus(StatusOfTask.IN_PROGRESS);
//        manager0.updateTask(task0);
//
//        subtask2.setDescription("Things can be packed for a next few weeks");
//        manager0.updateSubtask(subtask2);

//        epic1.setName("Career building");
//        manager0.updateEpic(epic1);
//
//        manager0.removeTaskByID(1); //удаляем task
//        manager0.removeSubtaskByID(9); //удаляем Subtask
//        manager0.removeEpicByID(3); //удаляем Epic
//
//        System.out.println("------------------------------------------");
//
//        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TasksForSP7.csv");
//
//        for (Task task : manager1.getTaskList()) { // отображаем все Tasks
//            System.out.println(task);
//        }
//
//        System.out.println("All tasks printed!");
//        System.out.println();
//
//        for (Task task : manager1.getEpicList()) { // отображаем все Epics
//            System.out.println(task);
//        }
//
//        System.out.println("All epics printed!");
//        System.out.println();
//
//
//        for (Task task : manager1.getSubtaskList()) { // отображаем все Subtasks
//            System.out.println(task);
//        }
//
//        System.out.println("All subtsasks printed!");
//        System.out.println();

    }
}