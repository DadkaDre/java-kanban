package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {

    private final Integer epicId;

    public SubTask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int epicId, LocalDateTime startTime, int duration) {
        super(name, description, status, startTime, duration);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int id, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int id, int epicId, TaskType type) {
        super(name, description, status, id, type);
        this.epicId = epicId;
    }

    public SubTask(String name, String description, Status status, int id, int epicId, TaskType type,
                   LocalDateTime startTime, Duration duration) {
        super(name, description, status, id, type, startTime, duration);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    @Override
    public TaskType getType() {
        return TaskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Подзадача-" +
                "название:' " + getName() + '\'' +
                ", описание:' " + getDescription() + '\'' +
                ", id: " + getId() + '\'' +
                ", статус: " + getStatus() + '\'' +
                ", id эпика: " + epicId + '\'' +
                ", старт: " + getStartTime() + '\'' +
                ", продолжительность: " + getDuration() +
                ')';
    }
}
