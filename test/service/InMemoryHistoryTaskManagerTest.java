package service;

import container.ObjectsContainerTest;
import model.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class InMemoryHistoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();
    ObjectsContainerTest container = new ObjectsContainerTest();
    InMemoryHistoryTaskManager inMemoryHistoryTaskManager = new InMemoryHistoryTaskManager();

    @DisplayName("Должен сравнить список из добавленных элементов")
    @Test
    void add() {
        Task task = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task);

        Task task2 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task2);


        assertEquals(inMemoryHistoryTaskManager.getHistory(), List.of(task, task2), "Кол-во задач не совпадает");

    }

    @DisplayName("Должен удалить первый элемент из списка истории и сравнить")
    @Test
    void removeFirst() {
        Task task = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task);

        Task task2 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task2);

        Task task3 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task3);

        inMemoryHistoryTaskManager.remove(task.getId());

        assertEquals(inMemoryHistoryTaskManager.getHistory(), List.of(task2, task3), "Кол-во задач не совпадает");

    }

    @DisplayName("должен сравнить список после удаления второго элемента")
    @Test
    void removeMiddle() {
        Task task = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task);

        Task task2 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task2);

        Task task3 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task3);

        inMemoryHistoryTaskManager.remove(task2.getId());

        assertEquals(inMemoryHistoryTaskManager.getHistory(), List.of(task, task3));
    }

    @DisplayName("Должен удалить последний элемент и сравнить список")
    @Test
    void removeLast() {
        Task task = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task);

        Task task2 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task2);

        Task task3 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task3);

        inMemoryHistoryTaskManager.remove(task3.getId());

        assertEquals(inMemoryHistoryTaskManager.getHistory(), List.of(task, task2));
    }

    @DisplayName("Должен сравнить кол-во элементов в списке истории")
    @Test
    void getTasks() {
        Task task = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task);

        Task task2 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task2);

        Task task3 = taskManager.createTask(container.getTask());
        inMemoryHistoryTaskManager.add(task3);

        assertEquals(inMemoryHistoryTaskManager.getHistory(), List.of(task, task2, task3));
    }
}