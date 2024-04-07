import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {

        TaskManager taskManager = new TaskManager();
        Task task = new Task("Задача", "description", Status.NEW);
        taskManager.createTask(task);


        Task task2 = new Task("Задача 2", "Описание 2", Status.NEW);
        taskManager.createTask(task2);


        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.createEpic(epic);


        SubTask subTask = new SubTask("Подзадача", "Описание подзадачи", Status.NEW, epic.getId());
        taskManager.createSubTask(subTask);


        SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", Status.DONE, epic.getId());
        taskManager.createSubTask(subTask2);


        Epic epic2 = new Epic("Эпик2", "Описание 2");
        taskManager.createEpic(epic2);


        SubTask subTask3 = new SubTask("Подзадача", "Описание подзадачи", Status.DONE, epic2.getId());
        taskManager.createSubTask(subTask3);


        System.out.println(taskManager.getAllTasks());

        System.out.println(taskManager.getAllSubTasks());

        System.out.println(taskManager.getAllEpics());

        System.out.println(taskManager.getAllSubTasksEpic(epic2));
    }

}
