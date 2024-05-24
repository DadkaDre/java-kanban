package model;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<SubTask> listSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW);
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
    }

    public Epic(String name, String description, Status status, int id, TaskType type) {
        super(name, description, status, id, type);
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
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}
