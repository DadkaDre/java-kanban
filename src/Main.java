import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.Managers;
import service.TaskManager;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager =  Managers.getDefault();
        Task task = new Task("Задача", "description", Status.NEW);
        taskManager.createTask(task);


        Task task1 = new Task("Задача2", "description2", Status.NEW);
        taskManager.createTask(task1);


        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        taskManager.createSubTask(subTask);

        SubTask subTask1 = new SubTask("Подзадача1", "Описание1", Status.NEW, epic.getId());
        taskManager.createSubTask(subTask1);

        SubTask subTask2 = new SubTask("Подзадача1", "Описание1", Status.NEW, epic.getId());
        taskManager.createSubTask(subTask2);

        Epic epic1 = new Epic("Эпик", "Описание эпика");
        taskManager.createEpic(epic1);

        taskManager.getIdTask(task.getId());
        taskManager.getIdTask(task1.getId());
        taskManager.getIdTask(task.getId());

        System.out.println(taskManager.getHistory());

        taskManager.removeIdTask(task1.getId());

        System.out.println(taskManager.getHistory());

        taskManager.getIdEpics(epic.getId());

        taskManager.getIdSubTasks(subTask.getId());
        taskManager.getIdSubTasks(subTask1.getId());
        taskManager.getIdSubTasks(subTask2.getId());

        System.out.println(taskManager.getHistory());

        taskManager.removeIdEpics(epic.getId());

        System.out.println(taskManager.getHistory());
    }
}
