public class Main {
    public static void main(String[] args) {

        FileBackedTasksManager manager0 = new FileBackedTasksManager("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TasksForSP7.csv");

        Task task0 = new Task("Cat feeding", "To fill a cat's dish", StatusOfTask.NEW); //создаем Task
        Task task1 = new Task("Borsh cooking", "To buy all essential goods to cook borsh", StatusOfTask.NEW); //создаем Task

        manager0.addNewTask(task0);// менеджер добавляет Task
        manager0.addNewTask(task1);// менеджер добавляет Task

        Epic epic0 = new Epic("Moving on", "Changing residence"); //создаем Epic
        Epic epic1 = new Epic("Family building", "To build a large family");//создаем Epic

        manager0.addNewEpic(epic0);// менеджер добавляет Epic
        manager0.addNewEpic(epic1);// менеджер добавляет Epic

        Subtask subtask0 = new Subtask("Packing", "Need to find and pack all things", StatusOfTask.NEW, epic0.getId()); //создаем Subtask
        Subtask subtask1 = new Subtask("Transporting", "To transport them to a different place", StatusOfTask.NEW, epic0.getId()); //создаем Subtask
        Subtask subtask2 = new Subtask("Unpacking", "To unpack all things", StatusOfTask.NEW, epic0.getId()); //создаем Subtask

        manager0.addNewSubtaskForEpic(subtask0); // добавляем эпику субзадачу
        manager0.addNewSubtaskForEpic(subtask1); // добавляем эпику субзадачу
        manager0.addNewSubtaskForEpic(subtask2); // добавляем эпику субзадачу

        Subtask subtask3 = new Subtask("Looking for a partner", "Need to look for an appropriate partner", StatusOfTask.NEW, epic1.getId());
        Subtask subtask4 = new Subtask("Making a proposal to the partner", "Need to get marry to create a family", StatusOfTask.NEW, epic1.getId());
        Subtask subtask5 = new Subtask("Children", "To give a birth to a few children", StatusOfTask.NEW, epic1.getId());

        manager0.addNewSubtaskForEpic(subtask3); // добавляем эпику субзадачу
        manager0.addNewSubtaskForEpic(subtask4); // добавляем эпику субзадачу
        manager0.addNewSubtaskForEpic(subtask5); // добавляем эпику субзадачу


        task0.setStatus(StatusOfTask.IN_PROGRESS);
        manager0.updateTask(task0);

        subtask2.setDescription("Things can be packed for a next few weeks");
        manager0.updateSubtask(subtask2);

        epic1.setName("Career building");
        manager0.updateEpic(epic1);

        manager0.removeTaskByID(1); //удаляем task
        manager0.removeSubtaskByID(9); //удаляем Subtask
        manager0.removeEpicByID(3); //удаляем Epic

        System.out.println("------------------------------------------");

        FileBackedTasksManager manager1 = FileBackedTasksManager.loadFromFile("C:\\Users\\tasca\\dev\\Sprint_6_FP\\java-kanban_FP6\\TasksForSP7.csv");

        for (Task task : manager1.getTaskList()) { // отображаем все Tasks
            System.out.println(task);
        }

        System.out.println("All tasks printed!");
        System.out.println();

        for (Task task : manager1.getEpicList()) { // отображаем все Epics
            System.out.println(task);
        }

        System.out.println("All epics printed!");
        System.out.println();


        for (Task task : manager1.getSubtaskList()) { // отображаем все Subtasks
            System.out.println(task);
        }

        System.out.println("All subtsasks printed!");
        System.out.println();

    }
}