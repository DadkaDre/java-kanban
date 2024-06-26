package server;

import com.sun.net.httpserver.HttpServer;
import handlers.EpicHandler;
import handlers.HistoryHandler;
import handlers.PrioritizedHandler;
import handlers.SubTaskHandler;
import handlers.TaskHandler;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.LocalDateTime;


public class HttpTaskServer {
    private static final int PORT_DEFAULT = 8088;
    private final int port;
    private HttpServer server;
    private final TaskManager taskManager;

    public HttpTaskServer() {
        this(PORT_DEFAULT);
    }

    public HttpTaskServer(int port) {
        this(Managers.getDefault(), port);
    }

    public HttpTaskServer(TaskManager taskManager) {
        this(taskManager, PORT_DEFAULT);
    }

    public HttpTaskServer(TaskManager taskManager, int port) {
        this.taskManager = taskManager;
        this.port = port;
    }

    public static void main(String[] args) {


        TaskManager taskManager1 = Managers.getDefault();

        Task task = new Task("Задача", "Описание", Status.NEW);
        taskManager1.createTask(task);

        HttpTaskServer server = new HttpTaskServer(taskManager1);
        server.start();


        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager1.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 7, 13, 18, 38, 20), 15);
        taskManager1.createSubTask(subTask);

    }

    public void start() {
        try {
            server = HttpServer.create(new InetSocketAddress("localHost", 8088), 0);
            server.start();
            System.out.println("Сервер запущен. Порт: " + port);
            this.server.createContext("/tasks", new TaskHandler(taskManager));
            this.server.createContext("/epics", new EpicHandler(taskManager));
            this.server.createContext("/subtasks", new SubTaskHandler(taskManager));
            this.server.createContext("/history", new HistoryHandler(taskManager));
            this.server.createContext("/prioritized", new PrioritizedHandler(taskManager));

        } catch (IOException e) {
            e.printStackTrace(System.out);
        }

    }

    public void stopServer() {
        server.stop(0);
        System.out.println("Сервер остановлен. Порт: " + port);
    }


}

