package service;

import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class InMemoryTaskManager implements TaskManager {


    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, SubTask> subTasks;
    private final HashMap<Integer, Epic> epics;
    private final HistoryManager historyManager;
    int counterId = 1;

    public InMemoryTaskManager(HistoryManager historyManager) {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
        this.historyManager = historyManager;
    }

    private int generateId() {
        return counterId++;
    }

    @Override
    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
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
        }
        tasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasks.keySet()) {
            historyManager.remove(id);
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
        }
        subTasks.clear();
        epics.clear();
    }

    @Override
    public Task getIdTask(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public SubTask getIdSubTasks(int id) {
        historyManager.add(subTasks.get(id));
        return subTasks.get(id);
    }

    @Override
    public Epic getIdEpics(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void updateTask(Task task) {
        tasks.put(task.getId(), task);
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.removeSubTask(subTask);
        epic.addSubTask(subTask);
        updateStatus(epic);
        subTasks.put(subTask.getId(), subTask);
    }

    @Override
    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }

    @Override
    public void removeIdTask(int id) {
        historyManager.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeIdSubTasks(int id) {

        historyManager.remove(id);
        SubTask subTask = subTasks.remove(id);
        int epicsId = subTask.getEpicId();
        Epic epic = epics.get(epicsId);
        epic.removeSubTask(subTask);
        updateStatus(epic);

    }

    @Override
    public void removeIdEpics(int id) {
        List<SubTask> subTasksEpic = epics.get(id).getListSubTasks();
        for (SubTask element : subTasksEpic) {
            historyManager.remove(element.getId());
        }
        historyManager.remove(id);
        Epic epic = epics.remove(id);
        ArrayList<SubTask> listSubTasksThisEpic = epic.getListSubTasks();
        for (SubTask subTask : listSubTasksThisEpic) {
            int idSubTask = subTask.getId();
            subTasks.remove(idSubTask);
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

    private void updateStatus(Epic epic) {
        boolean statusAllDone = true;
        boolean statusAllNew = true;
        ArrayList<SubTask> listSubTasksFromEpic = epic.getListSubTasks();

        if (listSubTasksFromEpic == null) {
            epic.setStatus(Status.NEW);
            return;
        }
        for (SubTask subTask : listSubTasksFromEpic) {
            Status status = subTask.getStatus();
            if (!status.equals(Status.NEW)) {
                statusAllNew = false;
            }
            if (!status.equals(Status.DONE)) {
                statusAllDone = false;
            }
        }
        if (statusAllNew || epic.getStatus() == null) {
            epic.setStatus(Status.NEW);
        } else if (statusAllDone) {
            epic.setStatus(Status.DONE);
        } else {
            epic.setStatus(Status.IN_PROGRESS);
        }
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
