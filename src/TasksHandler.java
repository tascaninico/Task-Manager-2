import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.google.gson.Gson;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.IOException;

public class TasksHandler extends BaseHttpHandler {

    public TasksHandler(TaskManager manager){
        super(manager);
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        String[] pathParts = path.split("/");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonResponse = "There is no task with a given id";

        switch (method) {
            case "GET" -> {
                if (pathParts.length == 2) {
                    jsonResponse = gson.toJson(manager.getTaskList());
                    sendText(exchange, jsonResponse);
                }
                if (pathParts.length == 3) {
                    if (manager.getTaskList().stream().anyMatch(task -> task.getId() == Integer.parseInt(pathParts[2]))) {
                        jsonResponse = gson.toJson(manager.getTaskByID(Integer.parseInt(pathParts[2])));
                        sendText(exchange, jsonResponse);
                    } else {
                        sendNotFound(exchange, jsonResponse);
                    }
                }
            }

            case "POST" -> {
                StringBuilder objectFromBody = new StringBuilder();
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody(), "utf-8"))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        objectFromBody.append(line);
                    }
                }
                Task taskAdOrUp = gson.fromJson(objectFromBody.toString(), Task.class);
                if (pathParts.length == 2) { // можно ли как-то оптимизировать кучу if????
                    if (manager instanceof InMemoryTaskManager && taskAdOrUp.getStartTime().isPresent()) {
                        if (!((InMemoryTaskManager) manager).getIntersection(taskAdOrUp)) {
                            manager.addNewTask(taskAdOrUp);
                            sendApproval(exchange, "The task has been added successfully");
                        } else {
                            sendHasInteractions(exchange);
                        }
                    } else {
                        manager.addNewTask(taskAdOrUp);
                        sendApproval(exchange, "The task has been added successfully");
                    }
                }
                if (pathParts.length == 3) {
                    if (manager.getTaskList().stream().anyMatch(task -> task.getId() == taskAdOrUp.getId())) {
                        manager.updateTask(taskAdOrUp);
                        sendApproval(exchange, "The task has been updated successfully");
                    } else {
                        sendNotFound(exchange, jsonResponse);
                    }
                }
            }

            case "DELETE" -> {
                if (manager.getTaskList().stream().anyMatch(task -> task.getId() == Integer.parseInt(pathParts[2]))) {
                    manager.removeTaskByID(Integer.valueOf(pathParts[2]));
                    sendApproval(exchange, "The task has been successfully removed");
                } else {
                    sendNotFound(exchange, jsonResponse);
                }
            }
        }
    }
}
