package handlers;

import com.sun.net.httpserver.HttpExchange;
import exception.NotFoundException;
import model.SubTask;
import server.HttpMethods;
import service.TaskManager;

import java.nio.charset.StandardCharsets;

public class SubTaskHandler extends BaseHttpHandler {

    public SubTaskHandler(TaskManager taskManager) {
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
                            writeResponse(exchange, getGson().toJson(taskManager.getAllSubTasks()), 200);
                        } else if (pathParts.length == 3) {
                            writeResponse(exchange, getGson().toJson(taskManager.getIdSubTasks(getIdFromPath(pathParts[2]))), 200);
                        }
                        break;
                    case HttpMethods.POST:
                        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                        SubTask subTask = getGson().fromJson(body, SubTask.class);

                        try {
                            taskManager.getIdSubTasks(subTask.getId());
                            taskManager.updateSubTask(subTask);
                            writeResponse(exchange, "Подзадача обновлена", 201);
                        } catch (NotFoundException e) {
                            taskManager.createSubTask(subTask);
                            writeResponse(exchange, "Подзадача создана", 201);
                        }
                        break;
                    case HttpMethods.DELETE:
                        int id = getIdFromPath(pathParts[2]);
                        if (id == -1) {
                            writeResponse(exchange, "Нет данных с таким номером", 404);
                        } else {
                            taskManager.removeIdSubTasks(id);
                            sendResponseCode(exchange);
                        }
                        break;
                }
            } catch (Exception e) {
                exception(exchange, e);
            }
        } catch (Exception e) {
            e.printStackTrace(System.out);
        }
    }
}
