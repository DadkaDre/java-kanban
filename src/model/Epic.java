package model;
import java.util.ArrayList;
public class Epic extends Task {
    private final ArrayList<SubTask> listSubTasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, null);
    }

    public void addSubTask(SubTask subTask) {listSubTasks.add(subTask);}
    public void removeSubTask(SubTask subTask) {listSubTasks.remove(subTask);}
    public ArrayList<SubTask> getListSubTasks() {return listSubTasks;}
    public void setListSubTasks(ArrayList<SubTask> listSubTasks) {listSubTasks = listSubTasks;}
    public void deleteTaskToId(SubTask subTask) {listSubTasks.remove(subTask);}
    public void deleteList() {listSubTasks.clear(); }

}