public class Main {
    public static void main(String[] args) {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TaskManagerInfo.csv");
        InMemoryTaskManager inMemoryTaskManager = new InMemoryTaskManager(); //создаем менеджера
        Task task0 = new Task("Покормить кота", "Насыпать в миску корма", StatusOfTask.NEW); //создаем Task
        Task task1 = new Task("Приготовить борщ", "Купить необходимые продукты и приготовить борщ", StatusOfTask.NEW); //создаем Task

        fileBackedTasksManager.addNewTask(task0);// менеджер добавляет задачу
        fileBackedTasksManager.addNewTask(task1);// менеджер добавляет задачу

        Epic epic0 = new Epic("Переезд", "Смена места жительства"); //создаем эпик
        Epic epic1 = new Epic("Привести себя в форму", "Необходимо сделать себя привлекательнее к лету"); //создаем эпик
        fileBackedTasksManager.addNewEpic(epic0);// менеджер добавляет epic
        fileBackedTasksManager.addNewEpic(epic1);// менеджер добавляет epic
//        System.out.println(fileBackedTasksManager.toString(epic0));


        Subtask subtask = new Subtask("Упаковка вещей", "Необходимо найти и упаковать все вещи", StatusOfTask.NEW, epic0.getId()); //создаем Subtask
        Subtask subtask1 = new Subtask("Перевоз вещей", "Перевезти вещи на новое место жительство", StatusOfTask.NEW , epic0.getId()); //создаем Subtask
        Subtask subtask3 = new Subtask("Распаковка вещей", "Разложить вещи по местам в новом жилье", StatusOfTask.NEW, epic0.getId()); //создаем subtask

//        System.out.println(fileBackedTasksManager.toString(subtask1));

        System.out.println();

        fileBackedTasksManager.addNewSubtaskForEpic(subtask); // добавляем эпику субзадаччу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask1); // добавляем эпику субзадачу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask3);
        Subtask subtask2 = new Subtask("Записаться в зал", "Для более скорого преображения лучше тренить на тренажерах", StatusOfTask.NEW, epic1.getId());
        fileBackedTasksManager.addNewSubtaskForEpic(subtask2);

        Task task3 = new Task("Come to the weekend", "Найти недорогие билеты и отель", StatusOfTask.NEW); //создаем Task
        fileBackedTasksManager.addNewTask(task3);

        Epic epic3 = new Epic("Отправиться на луну", "Планируем полет на луну"); //создаем эпик
        fileBackedTasksManager.addNewEpic(epic3);

        Subtask subtask4 = new Subtask("Пройти физ.подготовку", "Надо быть в форме", StatusOfTask.NEW, epic3.getId()); //создаем Subtask
        Subtask subtask5 = new Subtask("Собрать ракету", "Нужен транспорт", StatusOfTask.NEW , epic3.getId()); //создаем Subtask
        Subtask subtask6 = new Subtask("Взять большой запас еды и воды", "Надо распределить ресурсы на весь полет", StatusOfTask.NEW, epic3.getId()); //создаем subtask

        fileBackedTasksManager.addNewSubtaskForEpic(subtask4); // добавляем эпику субзадаччу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask5); // добавляем эпику субзадачу
        fileBackedTasksManager.addNewSubtaskForEpic(subtask6); // добавляем эпику субзадачу

//        fileBackedTasksManager.updateTask(new Task("Приготовить борщ", "Купить необходимые продукты и приготовить борщ", StatusOfTask.IN_PROGRESS));

//        fileBackedTasksManager.updateSubtask(new Subtask("Пройти физ.подготовку", "Надо быть в форме", StatusOfTask.DONE, epic3.getId()));

//          fileBackedTasksManager.deleteAllEpics();
//          fileBackedTasksManager.deleteAllTasks();
//          fileBackedTasksManager.deleteAllSubtasks();
//          fileBackedTasksManager.removeTaskByID(9);
//          fileBackedTasksManager.removeTaskByID(2);

//            fileBackedTasksManager.removeEpicByID(10);
            fileBackedTasksManager.removeTaskByID(1);
            fileBackedTasksManager.removeSubtaskByID(6);
            fileBackedTasksManager.removeSubtaskByID(12);


//        System.out.println(inMemoryTaskManager.getTaskByID(1));
//        System.out.println(inMemoryTaskManager.getTaskByID(2));
//        System.out.println(inMemoryTaskManager.getEpicByID(3));
//        System.out.println(inMemoryTaskManager.getEpicByID(4));
//        System.out.println(inMemoryTaskManager.getSubtaskByID(5));
//        System.out.println(inMemoryTaskManager.getSubtaskByID(6));
//        System.out.println(inMemoryTaskManager.getSubtaskByID(7));
//        System.out.println(inMemoryTaskManager.getSubtaskByID(8));
//        System.out.println();
//
//        for (Task task: inMemoryTaskManager.getHistory()){
//            System.out.println(task);
//        }
//        System.out.println();
//        inMemoryTaskManager.getTaskByID(1);
//        inMemoryTaskManager.getEpicByID(4);
//        inMemoryTaskManager.getSubtaskByID(6);
//        System.out.println();
//        for (Task task: inMemoryTaskManager.getHistory()){
//            System.out.println(task);
//        }
//
//        inMemoryTaskManager.getTaskByID(1);
//        inMemoryTaskManager.getTaskByID(2);
//        System.out.println();
//        for (Task task: inMemoryTaskManager.getHistory()){
//            System.out.println(task);
//        }
//
//        System.out.println();
//
//        inMemoryTaskManager.removeTaskByID(1);
//        System.out.println();
//        for (Task task: inMemoryTaskManager.getHistory()){
//            System.out.println(task);
//        }
//
//        System.out.println();
//        inMemoryTaskManager.removeEpicByID(3);
//        System.out.println();
//
//        for (Task task: inMemoryTaskManager.getHistory()){
//            System.out.println(task);
//        }
//        System.out.println();
//        System.out.println();
//
//        System.out.println(FileBackedTasksManager.toString(task0));
//        System.out.println(FileBackedTasksManager.toString(epic0));
//        System.out.println(FileBackedTasksManager.toString(subtask));
//        System.out.println(FileBackedTasksManager.toString(subtask1));
//        System.out.println(FileBackedTasksManager.toString(subtask2));
//        System.out.println(FileBackedTasksManager.toString(epic1));

    }
}