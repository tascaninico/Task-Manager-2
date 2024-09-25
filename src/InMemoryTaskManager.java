import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager{

    private final HashMap<Integer, Task> hashOfTasks = new HashMap<>();

    private final HashMap<Integer, Epic> hashOfEpics = new HashMap<>();

    private final Map<Integer, Subtask> hashOfSubtasks = new HashMap<>();

    private TreeSet<Task> sortedSetOfTasks =
            new TreeSet<>(Comparator.comparing((Task task0) -> task0.getStartTime().get()));

    public boolean getIntersection(Task task0){
        return getPrioritizedTasks().stream()
                .anyMatch((task -> {
                    LocalDateTime maxStartTime = task.getStartTime().get().isAfter(task0.getStartTime().get()) ?
                        task.getStartTime().get() : task0.getStartTime().get();
                    LocalDateTime minEndTime = task.getEndTime().get().isBefore(task0.getEndTime().get()) ?
                            task.getEndTime().get() : task0.getEndTime().get();
                    return maxStartTime.isBefore(minEndTime) || maxStartTime.isEqual(minEndTime);
                }));
    }

    private int idNum = 0;

    private final HistoryManager historyManager = Managers.getDefaultHistory();

    public List<Task> getPrioritizedTasks(){
        return new ArrayList<>(sortedSetOfTasks);
    }

    @Override
    public List<Task> getHistory(){
        return this.historyManager.getHistory();
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(hashOfTasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(hashOfEpics.values());
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        return new ArrayList<>(hashOfSubtasks.values());
    }

    @Override
    public void addNewTask(Task task){
        task.setId(generateID());
        hashOfTasks.put(task.getId(), task);
        if (task.getStartTime().isPresent()) {
            if (!getIntersection(task))
                sortedSetOfTasks.add(task);
            else
                System.out.println("Задача пересекается по времени с другой задачей");
        }
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
        Epic hostEpic;
        try {
            hostEpic = hashOfEpics.get(subtask.getIdOfHostEpic());
            hostEpic.getSubtasksID().add(subtask.getId());
        } catch (NullPointerException exception){
            System.out.println("Query to get Epic that does not exist!");
            return;
        }

        if (subtask.getStartTime().isPresent()){  // оптимизировать, так как появился отдельный метод
            if (hostEpic.getSubtasksID().size() == 1){
                hostEpic.setStartTime(subtask.getStartTime().get());
                hostEpic.setEndTime(subtask.getStartTime().get().plus(subtask.getDuration()));
                hostEpic.setDuration(subtask.getDuration());
            } else {
                if (subtask.getStartTime().get().isBefore(hostEpic.getStartTime().get()))
                    hostEpic.setStartTime(subtask.getStartTime().get());

                if (subtask.getEndTime().get().isAfter(hostEpic.getEndTime().get()))
                    hostEpic.setEndTime(subtask.getEndTime().get());
                hostEpic.setDuration(hostEpic.getDuration().plus(subtask.getDuration()));
            }
        }
        hashOfSubtasks.put(subtask.getId(), subtask);
        defineStatusOfEpic(hashOfEpics.get(subtask.getIdOfHostEpic()));
        if (subtask.getStartTime().isPresent()) {
            if (!getIntersection(subtask))
                sortedSetOfTasks.add(subtask);
            else
                System.out.println("Задача пересекается по времени с другой задачей");
        }
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
    public Task getTaskByID(int id) {
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
            sortedSetOfTasks = sortedSetOfTasks.stream().filter(task -> task.getId() == id)
                    .collect(Collectors.toCollection(()-> new TreeSet<>(Comparator.comparing((Task task0) -> task0.getStartTime().get()))));
            System.out.println("Task has been removed successfully");
        } else {
            System.out.println("There is no task with such id");
        }
    }

    @Override
    public void removeEpicByID(Integer id) {
        if (hashOfEpics.containsKey(id)) {

            List<Integer> listOfSubId = hashOfEpics.get(id).getSubtasksID();
            sortedSetOfTasks = sortedSetOfTasks.stream()
                    .filter(task -> !(listOfSubId.contains(task.getId())))
                            .collect(Collectors.toCollection(() -> new TreeSet<>(Comparator.comparing((Task task0) -> task0.getStartTime().get()))));

            hashOfEpics.get(id).getSubtasksID().forEach(hashOfSubtasks::remove);
            hashOfEpics.get(id).getSubtasksID().forEach(historyManager::remove);
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
            defineEpicTimeAndDuration(hashOfEpics.get(idOfEpic));
            System.out.println("Subtask has been removed successfully");
        } else {
            System.out.println("There is no subtask with such id");
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasksOfEpic(Integer epicID){
        return hashOfEpics.get(epicID).getSubtasksID().stream()
                .map(hashOfSubtasks::get)
                .collect(Collectors.toCollection(ArrayList::new));
    }

    @Override
    public void deleteAllTasks(){
        hashOfTasks.clear();
        sortedSetOfTasks = sortedSetOfTasks.stream().filter(task -> !(task.getClass().equals(Task.class)))
                .collect(Collectors.toCollection(()-> new TreeSet<>(Comparator.comparing((Task task0) -> task0.getStartTime().get()))));
    }

    @Override
    public void deleteAllEpics(){
        hashOfSubtasks.clear();
        sortedSetOfTasks = sortedSetOfTasks.stream().filter(task -> !(task.getClass().equals(Subtask.class)))
                .collect(Collectors.toCollection(()-> new TreeSet<>(Comparator.comparing((Task task0) -> task0.getStartTime().get()))));
        hashOfEpics.clear();
    }

    @Override
    public void deleteAllSubtasks(){
        hashOfEpics.forEach((key, value) -> value.getSubtasksID().clear());
        sortedSetOfTasks = sortedSetOfTasks.stream().filter(task -> !(task.getClass().equals(Subtask.class)))
                .collect(Collectors.toCollection(()-> new TreeSet<>(Comparator.comparing((Task task0) -> task0.getStartTime().get()))));
        hashOfSubtasks.clear();
    }

    private int generateID(){
        return ++idNum;
    }

    private void defineEpicTimeAndDuration(Epic epic){
        if (!epic.getSubtasksID().isEmpty() && epic.getSubtasksID().size() != 1) {
            List<LocalDateTime> startDateTimeList = epic.getSubtasksID().stream()
                    .map(idNum -> this.getSubtaskByID(idNum).getStartTime())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            epic.setStartTime(startDateTimeList.stream().min(Comparator.naturalOrder()).get());

            List<LocalDateTime> endDateTimeList = epic.getSubtasksID().stream()
                    .map(idNum -> this.getSubtaskByID(idNum).getEndTime())
                    .filter(Optional::isPresent)
                    .map(Optional::get)
                    .collect(Collectors.toList());

            epic.setEndTime(endDateTimeList.stream().max(Comparator.naturalOrder()).get());
            epic.setDuration(Duration.between(epic.getStartTime().get(), epic.getEndTime().get()));

        } else if (epic.getSubtasksID().size() == 1){
            Subtask onlySubOfEpic = this.getSubtaskByID(epic.getSubtasksID().get(0));
            if (onlySubOfEpic.getStartTime().isPresent()) {
                epic.setStartTime(onlySubOfEpic.getStartTime().get());
                epic.setDuration(onlySubOfEpic.getDuration());
                epic.setEndTime(onlySubOfEpic.getEndTime().get());
            }
        } else {
            epic.setStartTime(null);
            epic.setEndTime(null);
            epic.setDuration(null);
        }
    }

    private void defineStatusOfEpic(Epic epic){
        if (epic.getSubtasksID().isEmpty()){
            epic.setStatus(StatusOfTask.NEW);
            return;
        }
        long newCount = epic.getSubtasksID().stream().map(hashOfSubtasks::get)
                .filter(task -> task.getStatus().equals(StatusOfTask.NEW)).count();
        long doneCount = epic.getSubtasksID().stream().map(hashOfSubtasks::get)
                .filter(task -> task.getStatus().equals(StatusOfTask.DONE)).count();
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
