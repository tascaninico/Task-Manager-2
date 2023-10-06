import java.util.ArrayList;
import java.util.HashMap;

public class Main {
    public static void main(String[] args) {
        Manager manager = new Manager(); //создаем менеджера
        Task task0 = new Task("Покормить кота", "Насыпать в миску корма", "NEW"); //создаем Task
        Task task1 = new Task("Приготовить борщ", "Купить необходимые продукты и приготовить борщ", "NEW"); //создаем Task

        Subtask subtask = new Subtask("Упаковка вещей", "Необходимо найти и упаковать все вещи", "NEW"); //создаем Subtask
        Subtask subtask1 = new Subtask("Перевоз вещей", "Перевезти вещи на новое место жительство", "NEW"); //создаем Subtask
        ArrayList<Subtask> subtaskArrayList0 = new ArrayList<>();
        subtaskArrayList0.add(subtask);
        subtaskArrayList0.add(subtask1); // добавляем subtask в лист, чтобы передать Эпику
        Epic epic0 = new Epic("Переезд", "Смена места жительства", subtaskArrayList0); //создаем эпик


        Subtask subtask2 = new Subtask("Записаться в зал", "Для более скорого преображения лучше тренить на тренажерах", "NEW");
        ArrayList<Subtask> subtaskArrayList1 = new ArrayList<>();
        subtaskArrayList1.add(subtask2);// добавляем subtask в лист, чтобы передать Эпику
        Epic epic1 = new Epic("Привести себя в форму", "Необходимо сделать себя привлекательнее к лету", subtaskArrayList1); //создаем эпик

        manager.toAddNewTask(task0);// менеджер добавляет задачу
        manager.toAddNewTask(task1);// менеджер добавляет задачу

        for (Task task: manager.getHashOfTasks().values()){ //выводим на экран таски в менеджере
            System.out.println(task);
        }

        manager.toAddNewEpic(epic0);// менеджер добавляет epic
        manager.toAddNewEpic(epic1);// менеджер добавляет epic
        for (Epic epic: manager.getHashOfEpics().values()){ // выводим на экран эпики в менеджере
            System.out.println(epic);
        }

        for (Subtask subtask3: manager.getHashOfSubtasks().values() ){ // выводим на экран субтаски в менеджере
            System.out.println(subtask3);
        }


        Task task2 = manager.toGetTasksByID(1); //получаем Task
        task2.setStatus("DONE"); // меняем поле Task
        manager.toUpdateTask(task2); // Обновляем Task
        System.out.println();
        System.out.println(manager.toGetTasksByID(1));// cмотрим результат

        Epic epic = manager.toGetEpicByID(3); // получение Epic
        Subtask subtask3 = epic.getSubtasks().get(0); // получение Subtask Эпика
        subtask3.setStatus("DONE"); // меняем статус Subtask
        manager.toUpdateEpic(epic); // обновляем эпик
        System.out.println();
        System.out.println(manager.toGetEpicByID(3)); // смотрим, что статус эпика ихменился


        manager.toRemoveTaskByID(1); // удаление Task;
        for (Task task: manager.getHashOfTasks().values()){//смотрим результат
            System.out.println(task);
        }

        System.out.println();

        manager.toRemoveTaskByID(3); // удаление Epic;
        for (Epic epic2: manager.getHashOfEpics().values()){ //смотрим результат
            System.out.println(epic);
        }

    }
}
