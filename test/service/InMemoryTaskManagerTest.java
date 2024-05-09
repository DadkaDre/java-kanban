package service;

import container.ObjectsContainerTest;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

        assertEquals(1, tasks.size(), "Количество элементов в списке задач разное");

        shouldEqualsTasks(task, task1, "Задача до занесения в таблицу не совпадает после вызова из нее");
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

        assertEquals(1, subTasks.size(), "Кол-во подзадач в таблице после добавления элемента" +
                " не совпадает с единицей");
        shouldEqualsSubTasks(subTask, subTask1, "Поля в подзадачах после добавления в таблицу разные");
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
        assertEquals(1, epics.size(), "Кол-во элементов в таблице после добавление эпика" +
                " не совпадает с единицей");

        int epicId = epic.getId();
        Epic epic1 = epics.get(epicId);
        shouldEqualsEpics(epic, epic1, "Поля эпика после добавления в таблицу и после вызова из нее - разные ");

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
        assertEquals(2, allTasks.size(), "Кол-во элементов в списке задач, после добавления в него 2 " +
                "элементов не совпадает с 2");


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

        InMemoryHistoryTaskManager inMemoryHistoryTaskManager = new InMemoryHistoryTaskManager();
        inMemoryHistoryTaskManager.historyMap = new HashMap<>();

        Task task = new Task("Задача", "Описание1", Status.NEW);
        taskManager.createTask(task);
        Task task1 = new Task("Задача2", "Описание2", Status.NEW);
        taskManager.createTask(task1);

        taskManager.getIdTask(task.getId());
        taskManager.getIdTask(task1.getId());

        taskManager.deleteAllTasks();
        List<Task> taskList = taskManager.getAllTasks();
        List<Task> newTaskList = new ArrayList<>();

        assertEquals(taskList, newTaskList, "После удаления задач, лист с задачами не совпадает " +
                "с пустым листом");

    }

    @DisplayName("Должен удалять все подзадачи")
    @Test
    void shouldDeleteAllSubTasks() {

        Epic epic = taskManager.createEpic(new Epic("Эпик1", "Описание"));
        SubTask subTask = taskManager.createSubTask(new SubTask("Подзадача", "Описание",
                Status.NEW, epic.getId()));

        taskManager.getIdSubTasks(subTask.getId());

        taskManager.deleteAllSubTasks();

        List<SubTask> subtasksList = epic.getListSubTasks();
        List<SubTask> newSubTasksList = new ArrayList<>();

        assertEquals(subtasksList, newSubTasksList, "Кол-во элементов в списке подзадач после удаления всех " +
                "элементов из него не совпадает с пустым листом");
    }

    @DisplayName("Должен удалять все эпики'")
    @Test
    void shouldDeleteALLEpics() {


        Epic epic = taskManager.createEpic(new Epic("Эпик", "Описание"));
        Epic epic1 = taskManager.createEpic(new Epic("эпик2", "Описание2"));

        taskManager.deleteALLEpics();

        List<Epic> epicsList = taskManager.getAllEpics();
        List<Epic> newEpicList = new ArrayList<>();

        assertEquals(epicsList, newEpicList, "Кол-во элементов в листе эпиков после удаления всех " +
                "элементов не совпадает с пустым списком");
    }

    @DisplayName("Должен сравнить поля одной задачи после добавления")
    @Test
    void shouldGetIdTask() {

        Task task = taskManager.createTask(container.getTask());

        Task task1 = taskManager.getIdTask(task.getId());

        shouldEqualsTasks(task, task1, "Задача до добавления в таблицу и после вызова из нее разная");
    }

    @DisplayName("Сравниваем подзадачу до и после метода")
    @Test
    void shouldGetIdSubTasks() {
        Epic epic = taskManager.createEpic(container.getEpic());

        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));

        SubTask subTask1 = taskManager.getIdSubTasks(subTask.getId());

        shouldEqualsSubTasks(subTask, subTask1, "Подзадач после добавление в таблицу подзадач отличается после " +
                "вызова из таблицы этой же подзадачи");
    }

    @DisplayName("Сравниваем поля эпика до и после метода создания эпика")
    @Test
    void shouldGetIdEpics() {
        Epic epic = taskManager.createEpic(container.getEpic());

        Epic epic1 = taskManager.getIdEpics(epic.getId());

        shouldEqualsEpics(epic, epic1, "Поля эпика разные после добавления его в таблицу и при вызове из нее");
    }

    @DisplayName("Сравниваем поля задачи после обновления")
    @Test
    void shouldUpdateTask() {
        Task task = taskManager.createTask(container.getTask());

        Task task2 = new Task("Обновленная подзадача", "Обновленное описание", Status.NEW, task.getId());

        taskManager.updateTask(task);

        Task task3 = taskManager.getIdTask(task.getId());

        shouldEqualsTasks(task2, task3, "Поля задачи task2 до метода обновления отличаются от полей при вызове " +
                "ее после метода обновления");
    }

    @DisplayName("Сравниваем поля подзадачи после обновления и смотрим кол-во элементов в листе")
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

        shouldEqualsSubTasks(subTask2, subTask1, "Поля подзадачи subTask2 до метода обновления и после него" +
                " разные");

        assertEquals(1, epicList.size(), "Кол-во элементов в списке подзадач после метода обновления " +
                "изменилось");


    }

    @DisplayName("Сравниваем поля эпика до и после обновления")
    @Test
    void shouldUpdateEpic() {

        Epic epic = taskManager.createEpic(container.getEpic());
        Epic epic1 = new Epic("Обновленное название", "Обновленное описание", Status.NEW, epic.getId());
        taskManager.updateEpic(epic1);
        List<Epic> epicsList = taskManager.getAllEpics();
        Epic epic2 = epicsList.getFirst();

        shouldEqualsEpics(epic1, epic2, "Поля эпика epic1 до метода обновления и после него разные");
    }

    @DisplayName("Сравниваем кол-во элементов в таблице после удаления")
    @Test
    void shouldRemoveIdTask() {
        Task task = taskManager.createTask(container.getTask());
        taskManager.getIdTask(task.getId());

        taskManager.removeIdTask(task.getId());

        List<Task> tasksList = taskManager.getAllTasks();
        assertEquals(0, tasksList.size(), "Кол-во элементов в таблице после удаления отличается " +
                "от нуля");
    }

    @DisplayName("Сравниваем таблицу и список эпика на кол-во элементов после удаления подзадачи ")
    @Test
    void shouldRemoveIdSubTasks() {
        Epic epic = taskManager.createEpic(container.getEpic());

        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        taskManager.getIdSubTasks(subTask.getId());


        taskManager.removeIdSubTasks(subTask.getId());

        List<SubTask> epicList2 = epic.getListSubTasks();

        assertEquals(0, epicList2.size(), "Кол-во элементов в списке подзадач эпика после удаления " +
                "элементов отличается от нуля");

        List<SubTask> subTasksList = taskManager.getAllSubTasks();

        assertEquals(0, subTasksList.size(), "Кол-во элементов в таблице подзадач после удаления " +
                " не совпадает");


    }

    @DisplayName("Сравниваем кол-во элементов в таблице после удаления эпика")
    @Test
    void shouldRemoveIdEpics() {
        Epic epic = taskManager.createEpic(container.getEpic());

        taskManager.getIdEpics(epic.getId());
        taskManager.removeIdEpics(epic.getId());

        List<Epic> epicsList = taskManager.getAllEpics();

        assertEquals(0, epicsList.size(), "Кол-во элементов в таблице эпиков после удаления эпика " +
                "не совпадает с нулем");
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

        assertEquals(2, subTaskList.size(), "Кол-во добавляемых элементов и кол-во элементов при " +
                "вызове списка подзадач не совпадает");
    }

    @DisplayName("Сравниваем кол-во элементов в листе после вызова метода истории")
    @Test
    void shouldGetHistory() {
        Epic epic = taskManager.createEpic(container.getEpic());
        SubTask subTask = taskManager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));

        taskManager.getIdSubTasks(subTask.getId());
        taskManager.getIdEpics(epic.getId());

        Epic epic1 = new Epic("Новый", "Новый", Status.NEW, epic.getId());
        taskManager.updateEpic(epic1);

        taskManager.getIdEpics(epic.getId());

        List<Task> historyList = taskManager.getHistory();
        Task epic2 = historyList.get(1);


        assertEquals(2, historyList.size(), "Кол-во кол-во добавленных элементов в список истории " +
                " не равно двум");
    }
}