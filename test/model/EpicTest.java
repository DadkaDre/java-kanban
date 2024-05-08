package model;

import container.ObjectsContainerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("Эпик")
class EpicTest {


    ObjectsContainerTest container = new ObjectsContainerTest();
    TaskManager taskManager = Managers.getDefault();

    @DisplayName("Проверяем добавление подзадачи в лист эпика")
    @Test
    void shouldAddSubTaskToListEpicTest() {

        Epic epic = container.getEpic();
        taskManager.createEpic(epic);
        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        taskManager.createSubTask(subTask);

        epic.addSubTask(subTask);

        ArrayList<SubTask> epicSubTasksList = epic.getListSubTasks();
        SubTask subTask1 = epicSubTasksList.getFirst();

        assertEquals(subTask, subTask1, "Задачи разные");


    }

    @DisplayName("Проверяем удаление подзадачи из эпика, сверяем конечный лист с новым листом")
    @Test
    void shouldRemoveSubTaskFromList() {
        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        ArrayList<SubTask> epicSubTasksList = epic.getListSubTasks();
        assertEquals(1, epicSubTasksList.size(), "Кол-во элементов не совпадает");
        epic.removeSubTask(subTask);

        ArrayList<SubTask> newList = new ArrayList<>();

        assertEquals(newList, epicSubTasksList, "Не совпадает с пустым листом");
    }

    @Test
    void deleteList() {
        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        ArrayList<SubTask> epicSubTasksList = epic.getListSubTasks();
        assertEquals(1, epicSubTasksList.size(), "Кол-во элементов не совпадает");
        epic.deleteList();

        assertEquals(0, epicSubTasksList.size(), "Кол-во элементов не равно нулю");

    }

}