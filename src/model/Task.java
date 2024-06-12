package model;


import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;


public class Task {
    private String name;
    private String description;
    private int id;
    private Status status;
    private TaskType type;
    private LocalDateTime startTime;
    private Duration duration;
    private LocalDateTime endTime;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(15);
        this.endTime = startTime.plusMinutes(duration.toMinutes());

    }

    public Task(String name, String description, Status status, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public Task(String name, String description, Status status, int id) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(15);
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }


    public Task(String name, String description, Status status, int id, LocalDateTime startTime, Duration duration) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public Task(String name, String description, Status status, int id, TaskType type) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = type;
        this.startTime = LocalDateTime.now();
        this.duration = Duration.ofMinutes(15);
        this.endTime = startTime.plusMinutes(duration.toMinutes());


    }

    public Task(String name, String description, Status status, int id, TaskType type, LocalDateTime startTime,
                Duration duration) {

        this.name = name;
        this.description = description;
        this.status = status;
        this.id = id;
        this.type = type;
        this.startTime = startTime;
        this.duration = duration;
        this.endTime = startTime.plusMinutes(duration.toMinutes());
    }

    public Integer getEpicId() {
        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public TaskType getType() {
        return TaskType.TASK;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }


    @Override
    public String toString() {
        return "Задача-" +
                "название: '" + name + '\'' +
                ", Описание: '" + description + '\'' +
                ", id: " + id + '\'' +
                ", Статус: " + status + '\'' +
                ", старт: " + startTime + '\'' +
                ", продолжительность: " + duration +
                ')';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;

        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
