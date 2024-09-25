import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {

    private HttpServer httpServer ;

    public HttpTaskServer(TaskManager manager) {
        try {
            this.httpServer = HttpServer.create(new InetSocketAddress(8080), 0);
        } catch (Exception exception){
            System.out.println("Exception in HttpTaskServer constructor occurred");
        }
        httpServer.createContext("/tasks", new TasksHandler(manager));
        httpServer.createContext("/subtasks", new SubtasksHandler(manager));
        httpServer.createContext("/epics", new EpicHandler(manager));
        httpServer.createContext("/history", new HistoryHandler(manager));
        httpServer.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void start(){
        httpServer.start();
    }

    public void stop(){
        httpServer.stop(0);
    }

    public static void main(String[] args) throws IOException {

    }
}
