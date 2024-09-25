import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

public class EpicHandler extends BaseHttpHandler {

    protected EpicHandler(TaskManager manager) {
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
                    jsonResponse = gson.toJson(manager.getEpicList());
                    sendText(exchange, jsonResponse);
                }
                if (pathParts.length == 3) {
                    if (manager.getEpicList().stream().anyMatch(epic -> epic.getId() == Integer.parseInt(pathParts[2]))) {
                        jsonResponse = gson.toJson(manager.getEpicByID(Integer.parseInt(pathParts[2])));
                        sendText(exchange, jsonResponse);
                    } else {
                        sendNotFound(exchange, jsonResponse);
                    }
                }
                if (pathParts.length == 4) {
                    if (manager.getEpicList().stream().anyMatch(epic -> epic.getId() == Integer.parseInt(pathParts[2]))) {
                        List<Subtask> listOfSubtasks = manager.getEpicByID(Integer.parseInt(pathParts[2]))
                                .getSubtasksID().stream()
                                .map(idNum -> manager.getSubtaskByID(idNum)).collect(Collectors.toList());
                        jsonResponse = gson.toJson(listOfSubtasks);
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
                Epic epicAdOrUp = gson.fromJson(objectFromBody.toString(), Epic.class);
                if (pathParts.length == 2) {
                    manager.addNewEpic(epicAdOrUp);
                    sendApproval(exchange, "The task has been added successfully");
                }
                if (pathParts.length == 3) {
                    if (manager.getEpicList().stream().anyMatch(task -> task.getId() == epicAdOrUp.getId())) {
                        manager.updateEpic(epicAdOrUp);
                        sendApproval(exchange, "The task has been updated successfully");
                    } else {
                        sendNotFound(exchange, jsonResponse);
                    }
                }
            }

            case "DELETE" -> {
                if (manager.getEpicList().stream().anyMatch(epic -> epic.getId() == Integer.parseInt(pathParts[2]))) {
                    manager.removeEpicByID(Integer.valueOf(pathParts[2]));
                    sendApproval(exchange, "The task has been successfully removed");
                } else {
                    sendNotFound(exchange, jsonResponse);
                }
            }
        }
    }
}