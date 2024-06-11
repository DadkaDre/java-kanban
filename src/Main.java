import model.Epic;
import model.Status;
import model.SubTask;
import service.Managers;
import service.TaskManager;

import java.time.LocalDateTime;


public class Main {

    public static void main(String[] args) {

        TaskManager taskManager1 = Managers.getDefault();

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

    }
}
