import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.util.List;

public class HistoryHandler extends BaseHttpHandler{

    protected HistoryHandler(TaskManager manager) {
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        String[] pathParts = path.split("/");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonResponse = "The history is empty";

        if (method.equals("GET")) {
            if (!manager.getHistory().isEmpty()) {
                List<Task> listOfTasks = manager.getHistory();
                jsonResponse = gson.toJson(listOfTasks);
                sendText(exchange, jsonResponse);
            } else {
                sendNotFound(exchange, jsonResponse);
            }
        }

    }
}
