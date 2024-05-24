package service;

import model.Status;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FileBackedTaskManagerTest {


    @DisplayName("Должен сохранить и загрузить задачи правильно")
    @Test
    public void saveAndLodTask() {
        TaskManager taskManager = Managers.getDefault();
        taskManager = new FileBackedTaskManager(Managers.getDefaultHistory(), new File("test1.CSV"));
        Task task1 = taskManager.createTask(new Task("Задача", "Описание", Status.NEW));
        TaskManager taskManager1 = FileBackedTaskManager.loadFromFile(new File("test1.CSV"));
        Task task2 = taskManager1.getIdTask(1);

        assertEquals(task1, task2, "Задача test2 после загрузки из файла не совпадают");
    }
}
