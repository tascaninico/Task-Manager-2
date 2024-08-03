import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TasksForSP7.csv");

        System.out.println("------------------------------------------");
        fileBackedTasksManager.readTasksFromFile(); // Загружвем задачи из пустого файла
        // и выводим на экран все задачи прочитанные из файла(пример работы с пустым файлом)

        for (Task task : fileBackedTasksManager.getTaskList()) {
            System.out.println(task);
        }

        System.out.println("All tasks printed!");
        System.out.println();

        for (Task task : fileBackedTasksManager.getEpicList()) {
            System.out.println(task);
        }

        System.out.println("All epics printed!");
        System.out.println();


        for (Task task : fileBackedTasksManager.getSubtaskList()) {
            System.out.println(task);
        }

        System.out.println("All subtsasks printed!");
        System.out.println();

        Task task0 = new Task("Cat feeding", "To fill a cat's dish", StatusOfTask.NEW); //создаем Task
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh", StatusOfTask.NEW); //создаем Task

        fileBackedTasksManager.addNewTask(task0);// менеджер добавляет Task
        fileBackedTasksManager.addNewTask(task1);// менеджер добавляет Task

        Epic epic0 = new Epic("Moving on", "Changing residence"); //создаем Epic
        Epic epic1 = new Epic("Family building", "To build a large family");

        fileBackedTasksManager.addNewEpic(epic0);// менеджер добавляет Epic
        fileBackedTasksManager.addNewEpic(epic1);// менеджер добавляет Epic

        Subtask subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW, epic0.getId()); //создаем Subtask
        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW, epic0.getId()); //создаем Subtask
        Subtask subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW, epic0.getId()); //создаем Subtask

        fileBackedTasksManager.addNewSubtaskForEpic(subtask0); // добавляем эпику субзадачу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask1); // добавляем эпику субзадачу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask2); // добавляем эпику субзадачу

        Subtask subtask3 = new Subtask("Looking for a partner", "Need to look for an appropriate partner", StatusOfTask.NEW, epic1.getId());
        Subtask subtask4 = new Subtask("Making a proposal to the partner", "Need to get marry to create a family", StatusOfTask.NEW, epic1.getId());
        Subtask subtask5 = new Subtask("Children", "To give a birth to a few children", StatusOfTask.NEW, epic1.getId());

        fileBackedTasksManager.addNewSubtaskForEpic(subtask3); // добавляем эпику субзадачу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask4); // добавляем эпику субзадачу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask5); // добавляем эпику субзадачу

        fileBackedTasksManager.removeTaskByID(1); //удаляем task
        fileBackedTasksManager.removeSubtaskByID(9); //удаляем Subtask
        fileBackedTasksManager.removeEpicByID(3); //удаляем Epic

    }
}