package service;

import container.ObjectsContainerTest;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

abstract class TaskManagerTest<T extends TaskManager> {

    T manager;


    abstract T createTaskManager();

    @BeforeEach
    public void init() {
        manager = createTaskManager();

    }

    // TaskManager taskManager = Managers.getDefault();
    ObjectsContainerTest container = new ObjectsContainerTest();


    @DisplayName("При изменении полей подзадачи, эпика  изменятся аналогично ")
    @ParameterizedTest
    @CsvSource(value = {"15:20"}, delimiter = ':')
    public void shouldNotBeCross(int minutes, int duration) {


        Epic epic = new Epic("Эпик", "Описание эпика");
        manager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание",
                Status.NEW, epic.getId(),
                LocalDateTime.of(2024, 06, 06, 22, minutes, 20), duration);
        manager.createSubTask(subTask);

        manager.updateStatus(epic);
        Epic epic2 = epic;
        assertEquals(epic.getStartTime(), subTask.getStartTime(), "Время старта эпика и подзадачи не совпадает");
        assertEquals(epic2.getEndTime(), subTask.getEndTime(), "Поля окончания эпика и подзадачи не совпадают");
    }


    @DisplayName("Проверяем создание задачи, занесение в таблицу и возврат той же задачи")
    @Test
    void shouldCreateTask() {
        Task task = container.getTask();
        manager.createTask(task);
        int taskId = task.getId();

        Map<Integer, Task> tasks = manager.getTasks();
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
        manager.createEpic(epic);

        SubTask subTask = new SubTask("Подзадача", "Описание", Status.NEW, epic.getId());
        manager.createSubTask(subTask);

        Map<Integer, SubTask> subTasks = manager.getSubTasks();

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
        manager.createEpic(epic);
        Map<Integer, Epic> epics = manager.getEpics();
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
        manager.createTask(task);
        Task task1 = container.getTask();
        manager.createTask(task1);

        List<Task> allTasks = manager.getAllTasks();
        assertEquals(2, allTasks.size(), "Кол-во элементов в списке задач, после добавления в него 2 " +
                "элементов не совпадает с 2");


    }

    @DisplayName("Сравниваем кол-во задач, которое выводит метод")
    @Test
    void shouldGetAllSubTasks() {
        Epic epic = container.getEpic();
        manager.createEpic(epic);

        SubTask subTask = new SubTask("Тестовая подзадача", "Тестовое описание",
                Status.NEW, epic.getId());
        manager.createSubTask(subTask);
        SubTask subTask1 = new SubTask("Тестовая подзадача2", "Тестовое описание2",
                Status.NEW, epic.getId(), LocalDateTime.now().plusMinutes(25), 15);
        manager.createSubTask(subTask1);

        List<SubTask> allTasks = manager.getAllSubTasks();
        assertEquals(2, allTasks.size(), "Кол-во элементов не совпадает");
    }

    @DisplayName("Сравниваем кол-во эпиков, которое выводит метод")
    @Test
    void shouldGetAllEpics() {
        Epic epic = container.getEpic();
        manager.createEpic(epic);
        Epic epic1 = container.getEpic();
        manager.createEpic(epic1);

        List<Epic> epicsList = manager.getAllEpics();

        assertEquals(2, epicsList.size(), "Кол-во эпиков не совпадает");
    }

    @DisplayName("Должен вернуть пустой список")
    @Test
    void shouldDeleteAllTasks() {

        InMemoryHistoryTaskManager inMemoryHistoryTaskManager = new InMemoryHistoryTaskManager();
        inMemoryHistoryTaskManager.historyMap = new HashMap<>();

        Task task = new Task("Задача", "Описание1", Status.NEW);
        manager.createTask(task);

        manager.deleteAllTasks();
        List<Task> taskList = manager.getAllTasks();
        List<Task> newTaskList = new ArrayList<>();

        assertEquals(taskList, newTaskList, "После удаления задач, лист с задачами не совпадает " +
                "с пустым листом");

    }

    @DisplayName("Должен удалять все подзадачи")
    @Test
    void shouldDeleteAllSubTasks() {

        Epic epic = manager.createEpic(new Epic("Эпик1", "Описание"));
        SubTask subTask = manager.createSubTask(new SubTask("Подзадача", "Описание",
                Status.NEW, epic.getId()));

        manager.getIdSubTasks(subTask.getId());

        manager.deleteAllSubTasks();

        List<SubTask> subtasksList = epic.getListSubTasks();
        List<SubTask> newSubTasksList = new ArrayList<>();

        assertEquals(subtasksList, newSubTasksList, "Кол-во элементов в списке подзадач после удаления всех " +
                "элементов из него не совпадает с пустым листом");
    }

    @DisplayName("Должен удалять все эпики'")
    @Test
    void shouldDeleteALLEpics() {


        Epic epic = manager.createEpic(new Epic("Эпик", "Описание"));
        Epic epic1 = manager.createEpic(new Epic("эпик2", "Описание2"));

        manager.deleteALLEpics();

        List<Epic> epicsList = manager.getAllEpics();
        List<Epic> newEpicList = new ArrayList<>();

        assertEquals(epicsList, newEpicList, "Кол-во элементов в листе эпиков после удаления всех " +
                "элементов не совпадает с пустым списком");
    }

    @DisplayName("Должен сравнить поля одной задачи после добавления")
    @Test
    void shouldGetIdTask() {

        Task task = manager.createTask(container.getTask());

        Task task1 = manager.getIdTask(task.getId());

        shouldEqualsTasks(task, task1, "Задача до добавления в таблицу и после вызова из нее разная");
    }

    @DisplayName("Сравниваем подзадачу до и после метода")
    @Test
    void shouldGetIdSubTasks() {
        Epic epic = manager.createEpic(container.getEpic());

        SubTask subTask = manager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));

        SubTask subTask1 = manager.getIdSubTasks(subTask.getId());

        shouldEqualsSubTasks(subTask, subTask1, "Подзадач после добавление в таблицу подзадач отличается после " +
                "вызова из таблицы этой же подзадачи");
    }

    @DisplayName("Сравниваем поля эпика до и после метода создания эпика")
    @Test
    void shouldGetIdEpics() {
        Epic epic = manager.createEpic(container.getEpic());

        Epic epic1 = manager.getIdEpics(epic.getId());

        shouldEqualsEpics(epic, epic1, "Поля эпика разные после добавления его в таблицу и при вызове из нее");
    }

    @DisplayName("Сравниваем поля задачи после обновления")
    @Test
    void shouldUpdateTask() {
        Task task = manager.createTask(container.getTask());

        Task task2 = new Task("Обновленная подзадача", "Обновленное описание", Status.NEW, task.getId());

        manager.updateTask(task2);

        Task task3 = manager.getIdTask(task.getId());

        shouldEqualsTasks(task2, task3, "Поля задачи task2 до метода обновления отличаются от полей при вызове " +
                "ее после метода обновления");
    }

    @DisplayName("Сравниваем поля подзадачи после обновления и смотрим кол-во элементов в листе")
    @Test
    void shouldUpdateSubTask() {
        Epic epic = manager.createEpic(container.getEpic());

        SubTask subTask = manager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        SubTask subTask2 = new SubTask("Название2", "Описание2",
                Status.NEW, subTask.getId(), epic.getId());
        manager.updateSubTask(subTask2);

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

        Epic epic = manager.createEpic(container.getEpic());
        Epic epic1 = new Epic("Обновленное название", "Обновленное описание", Status.NEW, epic.getId());
        manager.updateEpic(epic1);
        List<Epic> epicsList = manager.getAllEpics();
        Epic epic2 = epicsList.getFirst();

        shouldEqualsEpics(epic1, epic2, "Поля эпика epic1 до метода обновления и после него разные");
    }

    @DisplayName("Сравниваем кол-во элементов в таблице после удаления")
    @Test
    void shouldRemoveIdTask() {
        Task task = manager.createTask(container.getTask());
        manager.getIdTask(task.getId());

        manager.removeIdTask(task.getId());

        List<Task> tasksList = manager.getAllTasks();
        assertEquals(0, tasksList.size(), "Кол-во элементов в таблице после удаления отличается " +
                "от нуля");
    }

    @DisplayName("Сравниваем таблицу и список эпика на кол-во элементов после удаления подзадачи ")
    @Test
    void shouldRemoveIdSubTasks() {
        Epic epic = manager.createEpic(new Epic("Эпик", "Описание"));

        SubTask subTask = manager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));

        manager.removeIdSubTasks(subTask.getId());

        List<SubTask> epicList2 = epic.getListSubTasks();
        List<SubTask> subTasksList = manager.getAllSubTasks();

        assertEquals(0, epicList2.size(), "Кол-во элементов в списке подзадач эпика после удаления " +
                "элементов отличается от нуля");

        assertEquals(0, subTasksList.size(), "Кол-во элементов в таблице подзадач после удаления " +
                " не совпадает");


    }

    @DisplayName("Сравниваем кол-во элементов в таблице после удаления эпика")
    @Test
    void shouldRemoveIdEpics() {

        Epic epic = manager.createEpic(container.getEpic());

        manager.getIdEpics(epic.getId());
        manager.removeIdEpics(epic.getId());

        List<Epic> epicsList = manager.getAllEpics();

        assertEquals(0, epicsList.size(), "Кол-во элементов в таблице эпиков после удаления эпика " +
                "не совпадает с нулем");
    }

    @DisplayName("Сравниваем кол-во подзадач в листе после метода вызова всех подзадач")
    @Test
    void getAllSubTasksEpic() {
        Epic epic = manager.createEpic(container.getEpic());
        SubTask subTask = manager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId()));
        SubTask subTask1 = manager.createSubTask(new SubTask("Название2", "Описание2",
                Status.NEW, epic.getId(), LocalDateTime.now().plusMinutes(45), 15));
        List<SubTask> subTaskList = manager.getAllSubTasks();

        assertEquals(2, subTaskList.size(), "Кол-во добавляемых элементов и кол-во элементов при " +
                "вызове списка подзадач не совпадает");
    }

    @DisplayName("Сравниваем кол-во элементов в листе после вызова метода истории")
    @Test
    void shouldGetHistory() {
        Epic epic = manager.createEpic(container.getEpic());
        SubTask subTask = manager.createSubTask(new SubTask("Название", "Описание",
                Status.NEW, epic.getId(), LocalDateTime.now(), 15));

        SubTask subTask1 = manager.getIdSubTasks(subTask.getId());
        Epic epic1 = manager.getIdEpics(epic.getId());

        List<Task> historyList = manager.getHistory();
        assertEquals(2, historyList.size(), "Кол-во кол-во добавленных элементов в список истории " +
                " не равно двум");
    }


    @DisplayName("Должен статус эпика должен рассчитаться правильно")
    @ParameterizedTest
    @EnumSource(Status.class)
    void shouldChangedStatusEpic(Status status) {
        Epic epic = manager.createEpic(container.getEpic());
        SubTask subTask = manager.createSubTask(new SubTask("Название", "Описание",
                status, epic.getId(), LocalDateTime.now(), 15));
        SubTask subTask1 = manager.createSubTask(new SubTask("Название", "Описание",
                status, epic.getId(), LocalDateTime.of(2024, 07, 10, 15, 58,
                24), 15));
        assertEquals(status, epic.getStatus(), "Cтатус эпика и подзадач не совпадают");
    }


}
