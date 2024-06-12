package service;

import model.Epic;
import model.SubTask;
import model.Task;

import java.util.List;
import java.util.Map;

public interface TaskManager {
    Task createTask(Task task);

    SubTask createSubTask(SubTask subTask);

    Epic createEpic(Epic epic);

    List<Task> getAllTasks();

    List<SubTask> getAllSubTasks();

    List<Epic> getAllEpics();

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteALLEpics();

    Task getIdTask(int id);

    SubTask getIdSubTasks(int id);

    Epic getIdEpics(int id);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpic(Epic epic);

    void removeIdTask(int id);

    void removeIdSubTasks(int id);

    void removeIdEpics(int id);

    List<SubTask> getAllSubTasksEpic(Epic epic);

    List<Task> getPrioritizedTasks();

    List<Task> getHistory();

    Map<Integer, Task> getTasks();

    Map<Integer, SubTask> getSubTasks();

    Map<Integer, Epic> getEpics();

    boolean validateTasks(Task t, Task task);

    void updateStatus(Epic epic);
}