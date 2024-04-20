import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import service.HistoryManager;
import service.InMemoryTaskManager;
import service.Managers;
import service.TaskManager;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager =  Managers.getDefault();
        Task task = new Task("Задача", "description", Status.NEW);
        taskManager.createTask(task);

        System.out.println(task);
        Task task1 = new  Task("Задача2", "description2", Status.NEW,3);
        taskManager.createTask(task1);
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

        SubTask subTask4 = new SubTask("2","3",Status.NEW, epic2.getId());
        taskManager.createSubTask(subTask4);

        System.out.println(epic2);
        System.out.println(taskManager.getAllSubTasks());

        SubTask subTask5 = new SubTask("обновленная подзадача", "Обновленное описание", Status.NEW, subTask4.getId(), epic2.getId() );
        taskManager.updateSubTask(subTask5);
        System.out.println(taskManager.getAllSubTasks());

        taskManager.getIdSubTasks(7);
        taskManager.getIdTask(2);
        epic.setDescription("Привет");

        Epic epic1 =new Epic("Новый","Новый",Status.NEW, epic.getId());
        taskManager.updateEpic(epic1);
        System.out.println(taskManager.getAllEpics());


    }
}
