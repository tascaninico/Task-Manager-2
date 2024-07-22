import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {

    private HashMap<Integer, Node> mapTasks = new HashMap<>();

    private Node head = null;

    private Node tail = null;

    private int size = 0;

    @Override
    public List<Task> getHistory(){
        return this.getTasks();
    }

    @Override
    public void addTask(Task task){
        if (mapTasks.containsKey(task.getId()))
            this.remove(task.getId());
        mapTasks.put(task.getId(), this.linkLast(task));
    }

    @Override
    public void remove(int id){
        this.removeNode(mapTasks.get(id));
        mapTasks.remove(id);
    }

    private Node linkLast(Task typeOfData){
        if (this.head == null){
            this.head = new Node(typeOfData, null, null);
            this.tail = this.head;
        } else {
            this.tail.next = new Node(typeOfData, this.tail, null);
            this.tail = this.tail.next;
        }
        this.size++;
        return this.tail;
    }

    private void removeNode(Node removedNode){
        if (removedNode == this.head & removedNode == this.tail) {
            this.head = null;
            this.tail = null;
        } else  if (removedNode == this.head) {
            removedNode.next.prev = null;
            this.head = removedNode.next;
        } else if (removedNode == this.tail) {
            removedNode.prev.next = null;
            this.tail = removedNode.prev;
        } else {
            removedNode.prev.next = removedNode.next;
            removedNode.next.prev = removedNode.prev;
        }
    }

    private ArrayList<Task> getTasks(){
        ArrayList<Task> tasks = new ArrayList<Task>();
        if (this.head == null)
            return tasks;
        Node nodeForCicle = this.head;
        while (nodeForCicle.next != null){
            tasks.add(nodeForCicle.data);
            nodeForCicle = nodeForCicle.next;
        }
        tasks.add(this.tail.data);
        return tasks;
    }

    private class Node {

        Task data;

        Node next;

        Node prev;

        Node(Task data, Node prev, Node next){
            this.data = data;
            this.prev = prev;
            this.next = next;
        }
    }
}
