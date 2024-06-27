package converter;

import model.Task;
import model.TaskType;

public class TaskConverter {

    public TaskType getType() {
        return TaskType.TASK;
    }

    public static String toString(Task task) {
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDescription() + "," + task.getEpicId() + "," + task.getDuration() + "," + task.getStartTime();
    }
}