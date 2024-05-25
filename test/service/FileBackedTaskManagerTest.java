package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {


    @DisplayName("Должен сохранить и загрузить задачи правильно")
    @Test
    public void saveAndLodTask() {
        TaskManager taskManager = Managers.getDefault();
        taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), Paths.get("resources/task.CSV"));
        Task task1 = taskManager.createTask(new Task("Задача", "Описание", Status.NEW));

        TaskManager taskManager1 = FileBackedTaskManager.loadFromFile(Paths.get("resources/task.CSV"));
        Task task2 = taskManager1.getIdTask(1);

        assertEquals(task1, task2, "Задача test2 после загрузки из файла не совпадают");
    }
}
