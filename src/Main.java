public class Main {
    public static void main(String[] args) {

        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(); //создаем менеджера
        Task task0 = new Task("Покормить кота", "Насыпать в миску корма", StatusOfTask.NEW); //создаем Task
        Task task1 = new Task("Приготовить борщ", "Купить необходимые продукты и приготовить борщ", StatusOfTask.NEW); //создаем Task

        inMemoryTaskManager.addNewTask(task0);// менеджер добавляет задачу
        inMemoryTaskManager.addNewTask(task1);// менеджер добавляет задачу

        Epic epic0 = new Epic("Переезд", "Смена места жительства"); //создаем эпик
        Epic epic1 = new Epic("Привести себя в форму", "Необходимо сделать себя привлекательнее к лету"); //создаем эпик
        inMemoryTaskManager.addNewEpic(epic0);// менеджер добавляет epic
        inMemoryTaskManager.addNewEpic(epic1);// менеджер добавляет epic

        Subtask subtask = new Subtask("Упаковка вещей", "Необходимо найти и упаковать все вещи", StatusOfTask.NEW, epic0.getId()); //создаем Subtask
        Subtask subtask1 = new Subtask("Перевоз вещей", "Перевезти вещи на новое место жительство", StatusOfTask.NEW , epic0.getId()); //создаем Subtask
        Subtask subtask3 = new Subtask("Распаковка вещей", "Разложить вещи по местам в новом жилье", StatusOfTask.NEW, epic0.getId()); //создаем subtask

        System.out.println();

        inMemoryTaskManager.addNewSubtaskForEpic(subtask); // добавляем эпику субзадаччу
        inMemoryTaskManager.addNewSubtaskForEpic(subtask1); // добавляем эпику субзадачу
        inMemoryTaskManager.addNewSubtaskForEpic(subtask3);
        Subtask subtask2 = new Subtask("Записаться в зал", "Для более скорого преображения лучше тренить на тренажерах", StatusOfTask.NEW, epic1.getId());
        inMemoryTaskManager.addNewSubtaskForEpic(subtask2);

        System.out.println(inMemoryTaskManager.getTaskByID(1));
        System.out.println(inMemoryTaskManager.getTaskByID(2));
        System.out.println(inMemoryTaskManager.getEpicByID(3));
        System.out.println(inMemoryTaskManager.getEpicByID(4));
        System.out.println(inMemoryTaskManager.getSubtaskByID(5));
        System.out.println(inMemoryTaskManager.getSubtaskByID(6));
        System.out.println(inMemoryTaskManager.getSubtaskByID(7));
        System.out.println(inMemoryTaskManager.getSubtaskByID(8));
        System.out.println();

        for (Task task: inMemoryTaskManager.getHistory()){
            System.out.println(task);
        }
        System.out.println();
        inMemoryTaskManager.getTaskByID(1);
        inMemoryTaskManager.getEpicByID(4);
        inMemoryTaskManager.getSubtaskByID(6);
        System.out.println();
        for (Task task: inMemoryTaskManager.getHistory()){
            System.out.println(task);
        }

        inMemoryTaskManager.getTaskByID(1);
        inMemoryTaskManager.getTaskByID(2);
        System.out.println();
        for (Task task: inMemoryTaskManager.getHistory()){
            System.out.println(task);
        }

        System.out.println();

        inMemoryTaskManager.removeTaskByID(1);
        System.out.println();
        for (Task task: inMemoryTaskManager.getHistory()){
            System.out.println(task);
        }

        System.out.println();
        inMemoryTaskManager.removeEpicByID(3);
        System.out.println();

        for (Task task: inMemoryTaskManager.getHistory()){
            System.out.println(task);
        }
    }
}