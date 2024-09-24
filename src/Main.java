
public class Main {

    public static void main(String[] args) {

        TaskManager inMemoryTaskManager = new InMemoryTaskManager();

        HttpTaskServer httpTaskServer = new HttpTaskServer(inMemoryTaskManager);

        httpTaskServer.start();

        httpTaskServer.stop();

    }
}