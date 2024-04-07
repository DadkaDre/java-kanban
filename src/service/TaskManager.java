package service;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class TaskManager {
    private HashMap<Integer, Task> tasks;
    private HashMap<Integer, SubTask> subTasks;
    private HashMap<Integer, Epic> epics;
    int counterId = 1;

    public TaskManager() {
        this.tasks = new HashMap<>();
        this.subTasks = new HashMap<>();
        this.epics = new HashMap<>();
    }

    public int generateId() {
        return counterId++;
    }

    public Task createTask(Task task) {
        task.setId(generateId());
        tasks.put(task.getId(), task);
        return task;
    }

    public SubTask createSubTask(SubTask subTask) {
        subTask.setId(generateId());
        subTasks.put(subTask.getId(), subTask);
        Epic epic = epics.get(subTask.getEpicId());
        epic.addSubTask(subTask);
        updateStatus(epic);
        return subTask;
    }

    public Epic createEpic(Epic epic) {
        epic.setId(generateId());
        epics.put(epic.getId(), epic);
        updateStatus(epic);
        return epic;
    }

    public ArrayList<Task> getAllTasks() {return new ArrayList<>(tasks.values());}

    public ArrayList<SubTask> getAllSubTasks() {return new ArrayList<>(subTasks.values());}

    public ArrayList<Epic> getAllEpics() {return new ArrayList<>(epics.values());}

    public void deleteAllTasks() {tasks.clear();}

    public void deleteAllSubTasks() {
        for (Epic epic : epics.values()){
            epic.deleteList();
            updateStatus(epic);
        }
        subTasks.clear();
        }

    public void deleteALLEpics() {
        subTasks.clear();
        epics.clear();}

    public Task getIdTask(int id) {return tasks.get(id);}

    public SubTask getIdSubTasks(int id) {return subTasks.get(id);}

    public Epic getIdEpics(int id) {return epics.get(id);}

    public void updateTask(Task task) {tasks.put(task.getId(), task);}

    public void updateSubTask(SubTask subTask) {
        int epicId = subTask.getEpicId();
        Epic epic = epics.get(epicId);
        epic.addSubTask(subTask);
        updateStatus(epic);
        subTasks.put(subTask.getId(), subTask);
    }

    public void updateEpic(Epic epic) {
        Epic saved = epics.get(epic.getId());
        if (saved == null) {
            return;
        }
        saved.setName(epic.getName());
        saved.setDescription(epic.getDescription());
    }
    public void removeIdTask(int id) {tasks.remove(id);}

    public void removeIdSubTasks(int id) {
       SubTask subTask = subTasks.get(id);
       int epicsId = subTask.getEpicId();
       Epic epic = epics.get(epicsId);
       epic.deleteTaskToId(subTask);
       updateStatus(epic);
       subTasks.remove(id);
    }

    public void removeIdEpics(int id) {epics.remove(id);}

    public ArrayList<SubTask> getAllSubTasksEpic(Epic epic){
        return epic.getListSubTasks();
    }

    public void updateStatus(Epic epic){
        boolean statusAllDone = true;
        boolean statusAllNew = true;
        ArrayList<SubTask> listSubTasksFromEpic = epic.getListSubTasks();

        if (listSubTasksFromEpic == null){
            epic.setStatus(Status.NEW);
            return;
        }
        for (SubTask subTask: listSubTasksFromEpic){
            Status status = subTask.getStatus();
            if (!status.equals(Status.NEW)){
                   statusAllNew = false;
            }
            if (!status.equals(Status.DONE)) {
                    statusAllDone = false;
            }
        }
        if(statusAllNew||epic.getStatus()==null){
            epic.setStatus(Status.NEW);
        }else if(statusAllDone){
            epic.setStatus(Status.DONE);
        }else{
            epic.setStatus(Status.IN_PROGRESS);
        }
    }

}
