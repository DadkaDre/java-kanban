package container;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.DisplayName;

@DisplayName("Заготовка ")
public class ObjectsContainerTest {

    private final Task task = new Task("Тестовая задача", "Тестовое описание", Status.NEW);
    private final Epic epic = new Epic("Тестовый эпик","Тестовое описание");
    private final SubTask subTask = new SubTask("Тестовая подзадача", "Тестовое описание",
            Status.NEW, epic.getId());


    public Task getTask() {
        return task;
    }

    public Epic getEpic() {
        return epic;
    }

    public SubTask getSubTask(){return subTask;}
}






