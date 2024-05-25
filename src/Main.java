import model.Epic;
import model.Status;
import model.SubTask;
import service.FileBackedTaskManager;
import service.Managers;
import service.TaskManager;

import java.nio.file.Paths;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager1 = Managers.getDefault();

        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager1.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        taskManager1.createSubTask(subTask);

        TaskManager taskManager = FileBackedTaskManager.loadFromFile(Paths.get("resources/task.CSV"));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getSubTasks());


    }
}
