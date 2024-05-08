package model;
import java.util.ArrayList;
public class Epic extends Task {
    private final ArrayList<SubTask> listSubTasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description, null);
    }

    public Epic(String name, String description, Status Status, int id) {
        super(name, description, Status, id);
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
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                '}';
    }
}
