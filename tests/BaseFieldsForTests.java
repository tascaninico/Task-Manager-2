import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import java.net.http.HttpClient;

abstract public class BaseFieldsForTests {
    protected TaskManager manager = new InMemoryTaskManager();

    protected HttpTaskServer httpTaskServer = new HttpTaskServer(manager);

    protected HttpClient client = HttpClient.newHttpClient();

    protected Gson gson = new Gson();
    @BeforeEach
    public void setUp(){
        manager.deleteAllTasks();
        manager.deleteAllSubtasks();
        manager.deleteAllEpics();
    }

    @AfterEach
    public void shutDown(){
        httpTaskServer.stop();
    }
}
