import java.util.HashMap;

public class Manager {


    private HashMap<Integer, Task> hashOfTasks = new HashMap<>();
    private HashMap<Integer, Epic> hashOfEpics = new HashMap<>();
    private HashMap<Integer, Subtask> hashOfSubtasks = new HashMap<>();
    private int idNum = 0;
    public HashMap<Integer, Task> getHashOfTasks() {
        return hashOfTasks;
    }

    public HashMap<Integer, Epic> getHashOfEpics() {
        return hashOfEpics;
    }

    public HashMap<Integer, Subtask> getHashOfSubtasks() {
        return hashOfSubtasks;
    }

    public void toAddNewTask(Task task){
        task.setId(toGenerateID());
        hashOfTasks.put(task.getId(), task);
    }


    public void toAddNewEpic(Epic epic){
        epic.setId(toGenerateID());
        hashOfEpics.put(epic.getId(),epic);
        for (Subtask subtask: epic.getSubtasks()){
            subtask.setId(toGenerateID());
            subtask.setIdOfHostEpic(epic.getId());
            hashOfSubtasks.put(subtask.getId(), subtask);
        }
    }

    public void toAddNewSubtaskForEpic(Epic epic, Subtask subtask){
        subtask.setIdOfHostEpic(epic.getId());
        subtask.setId(toGenerateID());
        epic.getSubtasks().add(subtask);
        epic.toDefineStatusOfEpic();
        hashOfSubtasks.put(subtask.getId(),subtask);
    }

    void toUpdateTask(Task task){
        hashOfTasks.put(task.getId(), task);
    }

    void toUpdateEpic(Epic epic){
        hashOfEpics.put(epic.getId(), epic);
        epic.setStatus(epic.toDefineStatusOfEpic());
    }

    void toUpdateSubtask(Subtask subtask){
        hashOfSubtasks.put(subtask.getId(), subtask);
        hashOfEpics.get(subtask.getIdOfHostEpic()).toDefineStatusOfEpic();
    }


    public Task toGetTasksByID(int id){
            return hashOfTasks.get(id);
    }

    public Epic toGetEpicByID(int id){
            return hashOfEpics.get(id);
    }

    public Subtask toGetSubtaskByID(int id){
        return hashOfSubtasks.get(id);
    }


    void toRemoveTaskByID(int id){
        if (hashOfTasks.containsKey(id)){
            hashOfTasks.remove(id, hashOfTasks.get(id));
            System.out.println("Task has been removed successfully");
            return;
        }
        if (hashOfEpics.containsKey(id)){
            for (Subtask subtask: hashOfEpics.get(id).getSubtasks()){
                hashOfSubtasks.remove(subtask);
                System.out.println("Subtask has been removed successfully");
            }
            hashOfEpics.remove(id, hashOfEpics.get(id));
            System.out.println("Epic has been removed successfully");
            return;
        }
        if (hashOfSubtasks.containsKey(id)){
            for (Epic epic: hashOfEpics.values()){
                for (Subtask subtask: epic.getSubtasks()){
                    if (subtask.getId() == id){
                        epic.getSubtasks().remove(id);
                        epic.toDefineStatusOfEpic();
                        break;
                    }
                }
            }
            hashOfSubtasks.remove(id,hashOfSubtasks.get(id));
            System.out.println("Epic has been removed successfully");
            return;
        }
    }

    void toDeleteAllTasks(){
        hashOfTasks.clear();
    }

    void toDeleteAllEpics(){
        hashOfSubtasks.clear();
        hashOfEpics.clear();
    }

    void toDeleteAllSubtasks(){
        for (Epic epic: hashOfEpics.values()){
            epic.getSubtasks().clear();
            epic.toDefineStatusOfEpic();
        }
        hashOfSubtasks.clear();
    }


    void toShowAllTasks(String typeOfTasks){// не тестировался
        if (typeOfTasks == "Task")
        {
            System.out.println("All tasks:");
            for (Integer num: hashOfTasks.keySet()){
                System.out.println(hashOfTasks.get(num));
            }
        }
        else if (typeOfTasks == "Epic"){
            System.out.println("All epics:");
            for (Integer num: hashOfEpics.keySet()){
                System.out.println(hashOfEpics.get(num));
            }
        }
        else if (typeOfTasks == "Subtask"){
            for (Integer num: hashOfEpics.keySet()){
                for (Subtask subtask : hashOfEpics.get(num).getSubtasks()){
                    System.out.println(subtask);
                }
            }
        } else{
            System.out.println("Тип с именем: " + typeOfTasks + "не найден");
        }
    }

    int toGenerateID(){
       return ++idNum;
    }

}
