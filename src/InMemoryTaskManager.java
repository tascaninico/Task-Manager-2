import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager{
    private HashMap<Integer, Task> hashOfTasks = new HashMap<>();

    private HashMap<Integer, Epic> hashOfEpics = new HashMap<>();

    private HashMap<Integer, Subtask> hashOfSubtasks = new HashMap<>();

    private int idNum = 0;

    private HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public List<Task> getHistory(){
        return this.historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.addAll(hashOfTasks.values());
        return tasks;
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epics = new ArrayList<>();
        epics.addAll(hashOfEpics.values());
        return epics;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subtasks = new ArrayList<>();
        subtasks.addAll(hashOfSubtasks.values());
        return subtasks;
    }

    @Override
    public void addNewTask(Task task){
        task.setId(generateID());
        hashOfTasks.put(task.getId(), task);
    }

    @Override
    public void addNewEpic(Epic epic){
        epic.setId(generateID());
        defineStatusOfEpic(epic);
        hashOfEpics.put(epic.getId(),epic);
    }

    @Override
    public void addNewSubtaskForEpic(Subtask subtask){
        subtask.setId(generateID());
        hashOfEpics.get(subtask.getIdOfHostEpic()).getSubtasksID().add(subtask.getId());
        hashOfSubtasks.put(subtask.getId(), subtask);
        defineStatusOfEpic(hashOfEpics.get(subtask.getIdOfHostEpic()));
    }

    @Override
    public void updateTask(Task task){
        hashOfTasks.put(task.getId(), task);
    }

    @Override
    public void updateEpic(Epic epic){
        hashOfEpics.put(epic.getId(), epic);
        defineStatusOfEpic(epic);
    }

    @Override
    public void updateSubtask(Subtask subtask){
        hashOfSubtasks.put(subtask.getId(), subtask);
        defineStatusOfEpic(hashOfEpics.get(subtask.getIdOfHostEpic()));
    }

    @Override
    public Task getTaskByID(int id)
    {
        this.historyManager.addTask(hashOfTasks.get(id));
        return hashOfTasks.get(id);
    }

    @Override
    public Epic getEpicByID(int id){
        this.historyManager.addTask(hashOfEpics.get(id));
        return hashOfEpics.get(id);
    }

    @Override
    public Subtask getSubtaskByID(int id){
        this.historyManager.addTask(hashOfSubtasks.get(id));
        return hashOfSubtasks.get(id);
    }

    @Override
    public void removeTaskByID(Integer id){
        if (hashOfTasks.containsKey(id)){
            hashOfTasks.remove(id, hashOfTasks.get(id));
            historyManager.remove(id);
            System.out.println("Task has been removed successfully");
        } else {
            System.out.println("There is no task with such id");
        }
    }

    @Override
    public void removeEpicByID(Integer id) {
        if (hashOfEpics.containsKey(id)) {
            for (Integer number : hashOfEpics.get(id).getSubtasksID()) {
                hashOfSubtasks.remove(number);
                historyManager.remove(number);
                System.out.println("Subtask has been removed successfully");
            }
            hashOfEpics.remove(id, hashOfEpics.get(id));
            historyManager.remove(id);
            System.out.println("Epic has been removed successfully");
        } else {
            System.out.println("There is no epic with such id");
        }
    }

    @Override
    public void removeSubtaskByID(Integer id){
        if (hashOfSubtasks.containsKey(id)){
            Integer idOfEpic = hashOfSubtasks.get(id).getIdOfHostEpic();
            hashOfEpics.get(idOfEpic).getSubtasksID().remove(id);
            hashOfSubtasks.remove(id,hashOfSubtasks.get(id));
            defineStatusOfEpic(hashOfEpics.get(idOfEpic));
            System.out.println("Subtask has been removed successfully");
        } else {
            System.out.println("There is no subtask with such id");
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Integer epicID){
        ArrayList<Subtask> subtasksOfEpic = new ArrayList<>();
        for (Integer number: hashOfEpics.get(epicID).getSubtasksID()){
            subtasksOfEpic.add(hashOfSubtasks.get(number));
        }
        return subtasksOfEpic;
    }

    @Override
    public void deleteAllTasks(){
        hashOfTasks.clear();
    }

    @Override
    public void deleteAllEpics(){
        hashOfSubtasks.clear();
        hashOfEpics.clear();
    }

    @Override
    public void deleteAllSubtasks(){
        for (Epic epic: hashOfEpics.values()){
            epic.getSubtasksID().clear();
            defineStatusOfEpic(epic);
        }
        hashOfSubtasks.clear();
    }

    private int generateID(){
        return ++idNum;
    }

    private void defineStatusOfEpic(Epic epic){
        if (epic.getSubtasksID().isEmpty()){
            epic.setStatus(StatusOfTask.NEW);
            return;
        }

        int newCount = 0;
        int doneCount = 0;

        for (Integer id: epic.getSubtasksID()){
            if (hashOfSubtasks.get(id).getStatus().equals(StatusOfTask.NEW))
                newCount++;
            else if (hashOfSubtasks.get(id).getStatus().equals(StatusOfTask.DONE))
                doneCount++;
        }
        if (newCount == epic.getSubtasksID().size())
            epic.setStatus(StatusOfTask.NEW);
        else if (doneCount == epic.getSubtasksID().size()) {
            epic.setStatus(StatusOfTask.DONE);
        }
        else {
            epic.setStatus(StatusOfTask.IN_PROGRESS);
        }
    }
}
