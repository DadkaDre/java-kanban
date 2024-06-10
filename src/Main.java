import model.Epic;
import model.Status;
import model.SubTask;
import service.Managers;
import service.TaskManager;

import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager1 = Managers.getDefault();

/*
        Task task = new Task("Задача","Описание",Status.NEW,LocalDateTime.of(2024,06,
                07,19,45,27), Duration.ofMinutes(15));
        taskManager1.createTask(task);

        Task task2 = new Task("задача2","Описание2",Status.NEW,LocalDateTime.of(2024,06,
                07,19,00,27),Duration.ofMinutes(15));
        taskManager1.createTask(task2);*/

        Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager1.createEpic(epic);
        System.out.println(epic.getStartTime());
        System.out.println(epic.getDuration());
        System.out.println(epic.getEndTime());

        System.out.println("После");

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 06, 10, 22, 38, 20), 15);
        taskManager1.createSubTask(subTask);

        SubTask subTask1 = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 06, 10, 23, 38, 20), 15);
        taskManager1.createSubTask(subTask1);

        System.out.println(epic.getStartTime());
        System.out.println(epic.getDuration());
        System.out.println(epic.getEndTime());
        /*SubTask subTask2 = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId(),LocalDateTime.now(),
                0);
          taskManager1.createSubTask(subTask2);
        System.out.println(epic.getStartTime());
        System.out.println(epic.getDuration());
        System.out.println(epic.getEndTime());

     /*   System.out.println(task);
        System.out.println(task2);*/



       /* List<Task> list = taskManager1.getPrioritizedTasks();
        System.out.println(list);

        TaskManager taskManager = FileBackedTaskManager.loadFromFile(new File("task.CSV"));
        Task task3 = taskManager.getIdTask(task.getId());
        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getPrioritizedTasks());*/
      /*  System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubTasks());
        System.out.println(taskManager.getEpics());*/
      /*  Epic epic = new Epic("Эпик", "Описание эпика");
        taskManager1.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId(),
                LocalDateTime.of(2024,06,06,22,38,20),15);
        taskManager1.createSubTask(subTask);

        SubTask subTask2 = new SubTask("Подзадача2", "Описание2", Status.NEW, epic.getId(),
                LocalDateTime.now().plusSeconds(3),15);
        taskManager1.createSubTask(subTask2);

        System.out.println(subTask);
        System.out.println(subTask2);
        System.out.println(epic);
      //  taskManager1.updateSubTask(subTask);

        TaskManager taskManager = FileBackedTaskManager.loadFromFile(new File("task.CSV"));
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getSubTasks());
*/

    }
}
