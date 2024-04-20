package service;

import model.Status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import container.ObjectsContainerTest;
import model.Task;
import model.SubTask;
import model.Epic;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;

@DisplayName("Тестируем Менеджер задач")

class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();
    ObjectsContainerTest container = new ObjectsContainerTest();

    @DisplayName("Проверяем создание задачи, занесение в таблицу и возврат той же задачи")
    @Test
    void shouldCreateTask() {
        Task task = container.getTask();
        taskManager.createTask(task);
        int taskId = task.getId();

        Map<Integer, Task> tasks = taskManager.getTasks();
        Task task1 = tasks.get(taskId);
        assertEquals(task, task1, "Задачи не совпадают");
        assertEquals(1, tasks.size(), "Количество элементов разное");
        shouldEqualsTasks(task, task1, "Задачи не совпадают");
    }

    @DisplayName("Сверяем поля в задачах")
    public void shouldEqualsTasks(Task taskExpected, Task taskActual, String text) {
        assertEquals(taskExpected.getName(), taskActual.getName(), "Названия не совпадают");
        assertEquals(taskExpected.getDescription(), taskActual.getDescription(), "Описание не совпадает");
        assertEquals(taskExpected.getStatus(), taskActual.getStatus(), "Статусы не совпадают");
        assertEquals(taskExpected.getId(), taskActual.getId(), "Id не совпадают");
    }


    @DisplayName("Проверяем создание подзадачи и внесение ее в таблицу")
    @Test
    void shouldCreateSubTask() {
        Epic epic = container.getEpic();
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        taskManager.createSubTask(subTask);

        Map<Integer, SubTask> subTasks = taskManager.getSubTasks();

        int subTaskId = subTask.getId();
        SubTask subTask1 = subTasks.get(subTaskId);

        assertEquals(1, subTasks.size(), "Кол-во элементов в таблице не совпадает");
        shouldEqualsSubTasks(subTask, subTask1, "Подзадачи не совпадают");
    }

    @DisplayName("Заготовка для сравнения полей в подзадачах")
    public void shouldEqualsSubTasks(SubTask subTaskExpected, SubTask subTaskActual, String text) {
        assertEquals(subTaskExpected.getName(), subTaskActual.getName(), "Название не совпадает");
        assertEquals(subTaskExpected.getDescription(), subTaskActual.getDescription(), "Описание разное");
        assertEquals(subTaskExpected.getEpicId(), subTaskActual.getEpicId(), "id Эпиков не совпадают");
        assertEquals(subTaskExpected.getId(), subTaskActual.getId(), "Id не совпадает");
        assertEquals(subTaskExpected.getStatus(), subTaskActual.getStatus(), "Статус не совпадает");
    }


    @DisplayName("Проверяем создание эпика и занесение его в таблицу")
    @Test
    void shouldCreateEpic() {
        Epic epic = container.getEpic();
        taskManager.createEpic(epic);
        Map<Integer, Epic> epics = taskManager.getEpics();
        assertEquals(1, epics.size(), "Кол-во элементов не совпадает");

        int epicId = epic.getId();
        Epic epic1 = epics.get(epicId);
        shouldEqualsEpics(epic, epic1, "Эпики не совпадают");

    }

    @DisplayName("Сверяет эпики по полям")
    public void shouldEqualsEpics(Epic epicExpect, Epic epicActual, String text) {
        assertEquals(epicExpect.getName(), epicActual.getName(), "Имена разные");
        assertEquals(epicExpect.getDescription(), epicActual.getDescription(), "Описание разное");
        assertEquals(epicExpect.getId(), epicActual.getId(), "Id разные");
        assertEquals(epicExpect.getStatus(), epicActual.getStatus(), "Статусы не совпадают");
    }

    @DisplayName("Сравниваем кол-во элементов при получении списка задач")
    @Test
    void shouldGetAllTasks() {
        Task task = container.getTask();
        taskManager.createTask(task);
        Task task1 = container.getTask();
        taskManager.createTask(task1);

        List<Task> allTasks = taskManager.getAllTasks();
        assertEquals(2, allTasks.size(), "Кол-во элементов не совпадает");


    }

    @DisplayName("Сравниваем кол-во задач, которое выводит метод")
    @Test
    void shouldGetAllSubTasks() {
        Epic epic = container.getEpic();
        taskManager.createEpic(epic);

        SubTask subTask = new SubTask("Тестовая подзадача", "Тестовое описание",
                Status.NEW, epic.getId());
        taskManager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Тестовая подзадача2", "Тестовое описание2",
                Status.NEW, epic.getId());
        taskManager.createSubTask(subTask1);

        List<SubTask> allTasks = taskManager.getAllSubTasks();
        assertEquals(2, allTasks.size(), "Кол-во элементов не совпадает");
    }

    @DisplayName("Сравниваем кол-во эпиков, которое выводит метод")
    @Test
    void shouldGetAllEpics() {
        Epic epic = container.getEpic();
        taskManager.createEpic(epic);
        Epic epic1 = container.getEpic();
        taskManager.createEpic(epic1);

        List<Epic> epicsList = taskManager.getAllEpics();

        assertEquals(2, epicsList.size(), "Кол-во эпиков не совпадает");
    }

    @DisplayName("Должен вернуть пустой список")
    @Test
    void shouldDeleteAllTasks() {
        Task task = container.getTask();
        taskManager.createTask(task);
        Task task1 = container.getTask();
        taskManager.createTask(task1);


        taskManager.deleteAllTasks();
        List<Task> taskList = taskManager.getAllTasks();
        List<Task> newTaskList = new ArrayList<>();

        assertEquals(taskList, newTaskList, "Кол-во элементов отличается");

    }

    @DisplayName("Должен удалять все подзадачи")
    @Test
    void shouldDeleteAllSubTasks() {

        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Подзадача", "Описание",
                Status.NEW, epic.getId()));

        taskManager.deleteAllSubTasks();
        List<SubTask> subtasksList = epic.getListSubTasks();
        List<SubTask> newSubTasksList = new ArrayList<>();
        assertEquals(subtasksList, newSubTasksList, "Кол-во элементов не совпадает");
    }

    @DisplayName("Должен удалять все эпики'")
    @Test
    void shouldDeleteALLEpics() {
        Epic epic = taskManager.createEpic(container.getEpic());
        Epic epic1 = taskManager.createEpic(container.getEpic());

        taskManager.deleteALLEpics();

        List<Epic> epicsList = taskManager.getAllEpics();
        List<Epic> newEpicList = new ArrayList<>();

        assertEquals(epicsList, newEpicList, "Кол-во элементов не совпадает");
    }

    @DisplayName("Должен сравнить задачу на входе с той, что после вызова")
    @Test
    void shouldGetIdTask() {

        Task task = taskManager.createTask(container.getTask());
        int taskId = task.getId();

        Task task1 = taskManager.getIdTask(taskId);

        shouldEqualsTasks(task, task1, "Две задачи разные");
    }

    @DisplayName("Сравниваем подзадачу до и после метода")
    @Test
    void shouldGetIdSubTasks() {
        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        int subTaskId = subTask.getId();

        SubTask subTask1 = taskManager.getIdSubTasks(subTaskId);

        shouldEqualsSubTasks(subTask, subTask1, "Подзадачи различаются");
    }

    @DisplayName("Сравниваем поля эпика до и после метода")
    @Test
    void shouldGetIdEpics() {
        Epic epic = taskManager.createEpic(container.getEpic());
        int epicId = epic.getId();

        Epic epic1 = taskManager.getIdEpics(epicId);

        shouldEqualsEpics(epic, epic1, "Эпики различаются");
    }

    @DisplayName("Сравниваем поля задачи после обновления")
    @Test
    void shouldUpdateTask() {
        Task task = taskManager.createTask(container.getTask());

        Task task2 = new Task("Обновленная подзадача", "Обновленное описание", Status.NEW, task.getId());

        taskManager.updateTask(task2);


        Task task3 = taskManager.getIdTask(task.getId());

        shouldEqualsTasks(task2, task3, "Задачи разные");
    }

    @DisplayName("Сравниваем поля задачи после обновления и смотрим кол-во элементов в листе")
    @Test
    void shouldUpdateSubTask() {
        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        SubTask subTask2 = new SubTask("Название2", "Описание2",
                Status.NEW, subTask.getId(), epic.getId());
        taskManager.updateSubTask(subTask2);
        List<SubTask> epicList = epic.getListSubTasks();
        SubTask subTask1 = epicList.getFirst();

        shouldEqualsSubTasks(subTask2, subTask1, "Задачи разные");

        assertEquals(1, epicList.size(), "Кол-во элементов не совпадает");


    }

    @DisplayName("Сравниваем поля эпика до и после обновления")
    @Test
    void shouldUpdateEpic() {

        Epic epic = taskManager.createEpic(container.getEpic());
        Epic epic1 = new Epic("Обновленное название", "Обновленное описание", Status.NEW, epic.getId());
        taskManager.updateEpic(epic1);
        List<Epic> epicsList = taskManager.getAllEpics();
        Epic epic2 = epicsList.getFirst();

        shouldEqualsEpics(epic1, epic2, "Эпики разные");
    }

    @DisplayName("Сравниваем кол-во элементов в таблице после удаления")
    @Test
    void shouldRemoveIdTask() {
        Task task = taskManager.createTask(container.getTask());
        taskManager.removeIdTask(task.getId());

        List<Task> tasksList = taskManager.getAllTasks();
        assertEquals(0, tasksList.size(), "Кол-во элементов отличается");
    }

    @DisplayName("Сравниваем таблицу и список эпика на кол-во элементов после удаления подзадачи ")
    @Test
    void shouldRemoveIdSubTasks() {
        Epic epic = taskManager.createEpic(container.getEpic());

        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание", Status.NEW, epic.getId()));


        taskManager.removeIdSubTasks(subTask.getId());

        List<SubTask> epicList2 = epic.getListSubTasks();

        assertEquals(0, epicList2.size(), "Кол-во элементов отличается");

        List<SubTask> subTasksList = taskManager.getAllSubTasks();

        assertEquals(0, subTasksList.size(), "Кол-во элементов не совпадает");


    }

    @DisplayName("Сравниваем кол-во элементов в таблице после удаления эпика")
    @Test
    void shouldRemoveIdEpics() {
        Epic epic = taskManager.createEpic(container.getEpic());
        taskManager.removeIdEpics(epic.getId());

        List<Epic> epicsList = taskManager.getAllEpics();

        assertEquals(0, epicsList.size(), "Кол-во элементов не совпадает");
    }

    @DisplayName("Сравниваем кол-во подзадач в листе после метода вызова всех подзадач")
    @Test
    void getAllSubTasksEpic() {
        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        SubTask subTask1 = taskManager.createSubTask(new SubTask("Название2", "Описание2",
                Status.NEW, epic.getId()));
        List<SubTask> subTaskList = taskManager.getAllSubTasks();

        assertEquals(2, subTaskList.size(), "Кол-во елементов не совпадает");
    }

    @DisplayName("Сравниваем кол-во элементов в листе после вызова метода истории")
    @Test
    void shouldGetHistory() {
        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        taskManager.getIdSubTasks(subTask.getId());
        taskManager.getIdEpics(epic.getId());

        List<Task> historyList = taskManager.getHistory();
        Task epic2 = historyList.get(1);


        assertEquals(2, historyList.size(), "Кол-во элементов отличается");


        assertEquals(epic.getName(), epic2.getName(), "Названия отличаются");
        assertEquals(epic.getDescription(), epic2.getDescription(), "Описание разное");
        assertEquals(epic.getId(), epic2.getId(), "id не равны");
        assertEquals(epic.getStatus(), epic2.getStatus(), "Статусы разные");

    }
}