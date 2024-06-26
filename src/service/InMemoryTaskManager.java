package service;

import exception.ManagerIOException;
import exception.NotFoundException;
import exception.ValidationException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.TreeSet;


public class InMemoryTaskManager implements TaskManager {


    protected HashMap<Integer, Task> tasks;
    protected HashMap<Integer, SubTask> subTasks;
    protected HashMap<Integer, Epic> epics;
    protected HistoryManager historyManager;
    int counterId = 1;
    protected TreeSet<Task> prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    protected int seq;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = historyManager;
        seq = 0;
    }

    private int generateId() {
        return counterId++;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        checkTaskTime(task);
        prioritizedTasks.remove(task);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        checkTaskTime(subTask);
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            throw new NotFoundException("Нет такого эпика: " + subTask.getEpicId());
        }
        prioritizedTasks.remove(subTask);
        prioritizedTasks.add(subTask);
        epic.addSubTask(subTask);
        updateStatus(epic);
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        updateStatus(epic);
        return epic;
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<SubTask> getAllSubTasks() {
        ArrayList<SubTask> list = new ArrayList<>();
        for (SubTask value : subTasks.values()) {
            list.add(value);
        }
        return list;
    }


    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(tasks.get(id));
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subTasks.get(id));
        }
        for (Epic epic : epics.values()) {
            epic.deleteList();
            updateStatus(epic);
        }
        subTasks.clear();
    }

    @Override
    public void deleteALLEpics() {
        for (Integer id : epics.keySet()) {
            historyManager.remove(id);
        }
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
            prioritizedTasks.remove(subTasks.get(id));
        }
        subTasks.clear();
        epics.clear();
    }


    @Override
    public Task getIdTask(int id) {
        Task task = tasks.get(id);
        if (task == null) {
            throw new NotFoundException("Нет задачи по ID №: " + id);
        }

        historyManager.add(task);
        return tasks.get(id);
    }

    @Override
    public SubTask getIdSubTasks(int id) {
        SubTask subTask = subTasks.get(id);
        if (subTask == null) {
            throw new NotFoundException("Нет подзадачи по ID №:" + id);
        }
        historyManager.add(subTask);
        return subTasks.get(id);
    }

    @Override
    public Epic getIdEpics(int id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            throw new NotFoundException("Нет эпика gj ID №: " + id);
        }
        historyManager.add(epic);
        return epics.get(id);
    }

    @Override
    public void updateTask(Task task) {
        Task original = tasks.get(task.getId());
        if (original == null) {
            throw new NotFoundException("Эпик не найден по id: " + task.getId());
        }
        checkTaskTime(task);
        prioritizedTasks.remove(task);
        prioritizedTasks.add(task);
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            throw new ManagerIOException("Нет эпика по id: " + subTask.getEpicId());
        }
        checkTaskTime(subTask);
        prioritizedTasks.remove(subTask);
        prioritizedTasks.add(subTask);
        epic.removeSubTask(subTask);
        epic.addSubTask(subTask);

        updateStatus(epic);
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            throw new ManagerIOException("Нет такого эпика: " + epic.getId());
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void removeIdTask(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        tasks.remove(id);
    }

    @Override
    public void removeIdSubTasks(int id) {

        historyManager.remove(id);
        prioritizedTasks.remove(subTasks.get(id));
        SubTask subTask = subTasks.remove(id);
        Epic epic = epics.get(subTask.getEpicId());
        if (epic == null) {
            throw new ManagerIOException("Нет такого эпика: " + subTask.getEpicId());
        }
        epic.removeSubTask(subTask);
        updateStatus(epic);

    }

    @Override
    public void removeIdEpics(int id) {
        historyManager.remove(id);
        Epic epic = epics.remove(id);
        ArrayList<SubTask> listSubTasksThisEpic = epic.getListSubTasks();
        for (SubTask subTask : listSubTasksThisEpic) {
            int idSubTask = subTask.getId();
            subTasks.remove(idSubTask);
            historyManager.remove(subTask.getId());
            prioritizedTasks.remove(subTasks.get(idSubTask));
        }
    }

    @Override
    public ArrayList<SubTask> getAllSubTasksEpic(Epic epic) {

        return epic.getListSubTasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void updateStatus(Epic epic) {
        boolean statusAllDone = true;
        boolean statusAllNew = true;
        List<SubTask> listSubTasksFromEpic = epic.getListSubTasks();
        try {
            LocalDateTime start = listSubTasksFromEpic.getFirst().getStartTime();
            LocalDateTime end = listSubTasksFromEpic.getFirst().getEndTime();

            for (SubTask subTask : listSubTasksFromEpic) {

                if (!subTask.getStartTime().isAfter(start)) {
                    start = subTask.getStartTime();
                }

                epic.setDuration(epic.getDuration().plus(subTask.getDuration()));

                if (!subTask.getEndTime().isBefore(end)) {
                    end = subTask.getEndTime();
                }

                Status status = subTask.getStatus();

                if (!status.equals(Status.NEW)) {
                    statusAllNew = false;
                }
                if (!status.equals(Status.DONE)) {
                    statusAllDone = false;
                }
            }
            epic.setStartTime(start);
            epic.setEndTime(end);
        } catch (NoSuchElementException | IndexOutOfBoundsException e) {
            epic.setStartTime(LocalDateTime.now());
            epic.setStatus(Status.NEW);
        }

        if (statusAllNew || epic.getStatus() == null) {
            epic.setStatus(Status.NEW);
        } else if (statusAllDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
    }


    public void checkTaskTime(Task task) {
        List<Task> prioritizedTasks = getPrioritizedTasks();

        prioritizedTasks.stream()
                .filter(t -> t.getId() != task.getId())
                .filter(t -> !validateTasks(t, task))
                .findFirst()
                .ifPresent(t -> {
                    throw new ValidationException("Время занято. Пересечение задач " + t.getName()
                            + task.getName());
                });

    }

    public boolean validateTasks(Task t, Task task) {
        return (task.getEndTime().isBefore(t.getStartTime()) || (task.getStartTime().isAfter(t.getEndTime())));
    }


    @Override
    public ArrayList<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public HashMap<Integer, SubTask> getSubTasks() {
        return subTasks;
    }

    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

}
