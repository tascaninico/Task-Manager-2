import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SubtasksHandler extends BaseHttpHandler{

    public SubtasksHandler(TaskManager manager){super(manager);}

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();

        String[] pathParts = path.split("/");

        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String jsonResponse = "There is no subtask with a given id";

        switch (method) {
            case "GET" -> {
                if (pathParts.length == 2) {
                    jsonResponse = gson.toJson(manager.getSubtaskList());
                    sendText(exchange, jsonResponse);
                }
                if (pathParts.length == 3) {
                    if (manager.getSubtaskList().stream().anyMatch(task -> task.getId() == Integer.parseInt(pathParts[2]))) {
                        jsonResponse = gson.toJson(manager.getSubtaskByID(Integer.parseInt(pathParts[2])));
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
                Subtask subtaskAdOrUp = gson.fromJson(objectFromBody.toString(), Subtask.class);
                if (pathParts.length == 2) {
                    if (manager instanceof InMemoryTaskManager && subtaskAdOrUp.getStartTime().isPresent()) {
                        if (!((InMemoryTaskManager) manager).getIntersection(subtaskAdOrUp)) {
                            manager.addNewSubtaskForEpic(subtaskAdOrUp);
                            sendApproval(exchange, "The subtask has been added successfully");
                        } else {
                            sendHasInteractions(exchange);
                        }
                    } else {
                        manager.addNewSubtaskForEpic(subtaskAdOrUp);
                        sendApproval(exchange, "The subtask has been added successfully");
                    }
                }
                if (pathParts.length == 3) {
                    if (manager.getSubtaskList().stream().anyMatch(subtask -> subtask.getId() == subtaskAdOrUp.getId())) {
                        manager.updateSubtask(gson.fromJson(objectFromBody.toString(), Subtask.class));
                        sendApproval(exchange, "The subtask has been added successfully");
                    } else {
                        sendNotFound(exchange, jsonResponse);
                    }
                }
            }

            case "DELETE" -> {
                if (manager.getSubtaskList().stream().anyMatch(subtask -> subtask.getId() == Integer.parseInt(pathParts[2]))) {
                    manager.removeSubtaskByID(Integer.valueOf(pathParts[2]));
                    sendApproval(exchange, "The task has been successfully removed");
                } else {
                    sendNotFound(exchange, jsonResponse);
                }
            }
        }
    }
}
