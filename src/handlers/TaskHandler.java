package handlers;

import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.Task;
import server.HttpMethods;
import service.TaskManager;

import java.nio.charset.StandardCharsets;


public class TaskHandler extends BaseHttpHandler {


    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    public void handle(HttpExchange exchange) {
        System.out.println("Началась обработка Задачи");
        try (exchange) {

            String path = exchange.getRequestURI().getPath();
            String method = exchange.getRequestMethod();
            String[] pathParts = path.split("/");
            try {
                switch (method) {
                    case HttpMethods.GET:
                        if (pathParts.length == 2) {

                            writeResponse(exchange, getGson().toJson(taskManager.getAllTasks()), 200);

                        } else if (pathParts.length == 3) {

                            writeResponse(exchange, getGson().toJson(taskManager.getIdTask(getIdFromPath(pathParts[2]))), 200);
                        }
                        break;
                    case HttpMethods.POST:

                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        Task task = getGson().fromJson(body, Task.class);
                        try {
                            taskManager.getIdTask(task.getId());
                            taskManager.updateTask(task);
                            writeResponse(exchange, "Задача обновлена", 201);
                        } catch (NotFoundException e) {
                            taskManager.createTask(task);
                            writeResponse(exchange, "Задача создана", 201);
                        }
                        break;
                    case HttpMethods.DELETE:
                        int id = getIdFromPath(pathParts[2]);

                        if (id == -1) {
                            writeResponse(exchange, "Нет данных с таким номером", 404);
                        } else {
                            taskManager.removeIdTask(id);
                            sendResponseCode(exchange);
                        }
                        break;
                }
            } catch (Exception exception) {
                exception(exchange, exception);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}