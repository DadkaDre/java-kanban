package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    public void setListSubTasks(ArrayList<SubTask> listSubTasks) {
        this.listSubTasks = listSubTasks;
    }

    private transient ArrayList<SubTask> listSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW, LocalDateTime.now(), 0);
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
    }

    public Epic(String name, String description, Status status, LocalDateTime startTime, int duration) {
        super(name, description, status, startTime, duration);
    }

    public Epic(String name, String description, Status status, int id, TaskType type, LocalDateTime startTime, Duration duration) {
        super(name, description, status, id, type, startTime, duration);
    }

    public void addSubTask(SubTask subTask) {
        listSubTasks.add(subTask);
    }

    public void removeSubTask(SubTask subTask) {
        listSubTasks.remove(subTask);
    }

    public ArrayList<SubTask> getListSubTasks() {
        return listSubTasks;
    }


    public void deleteList() {
        listSubTasks.clear();
    }

    @Override
    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "Эпик-" +
                "название:' " + getName() + '\'' +
                ", описание:' " + getDescription() + '\'' +
                ", id: " + getId() + '\'' +
                ", cтатус: " + getStatus() + '\'' +
                ", старт: " + getStartTime() + '\'' +
                ", продолжительность: " + getDuration() +
                ", окончание эпика: " + getEndTime() +
                ')';
    }
}
