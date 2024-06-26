package service;

import converter.TaskConverter;
import exception.ManagerIOException;
import model.Epic;
import model.Status;
import model.SubTask;
import model.Task;
import model.TaskType;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;


public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file, Charset charset) {
        this(Managers.getDefaultHistory(), file);

    }

    public FileBackedTaskManager(HistoryManager historyManager, File file) {
        super(historyManager);
        this.file = file;

    }


    public FileBackedTaskManager(HistoryManager historyManager) {
        super(historyManager);
        file = Paths.get("task.CSV").toFile();

    }


    public void init() {
        loadFromFile();
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file, StandardCharsets.UTF_8);
        manager.init();
        return manager;

    }

    private void save() {

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8))) {
            writer.write("id,type,name,status,description,epicId,duration,startTime:");
            writer.newLine();
            for (Task task : tasks.values()) {

                writer.write(TaskConverter.toString(task));
                writer.newLine();
            }
            for (Epic epic : epics.values()) {

                writer.write(TaskConverter.toString(epic));
                writer.newLine();
            }
            for (SubTask subTask : subTasks.values()) {

                writer.write(TaskConverter.toString(subTask));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new ManagerIOException("Нет файла: task.CSV");
        }
    }

    private void loadFromFile() {
        int maxId = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            reader.readLine();
            while (reader.ready()) {
                String line = reader.readLine();
                Task task = fromString(line);
                int id = task.getId();
                switch (task.getType()) {
                    case TASK:

                        tasks.put(id, task);
                        checkTaskTime(task);
                        prioritizedTasks.add(task);
                        break;
                    case EPIC:

                        epics.put(id, (Epic) task);

                        break;
                    case SUBTASK:

                        subTasks.put(id, (SubTask) task);
                        checkTaskTime(task);
                        prioritizedTasks.add(task);
                        break;
                }
                if (maxId < id) {
                    maxId = id;
                }
            }
            for (SubTask subTask : subTasks.values()) {
                Epic epic = epics.get(subTask.getEpicId());
                epic.addSubTask(subTask);
                epic.setEndTime(LocalDateTime.of(1970, 1, 1, 1, 10));
                if (subTask.getEndTime().isAfter(epic.getEndTime())) {
                    epic.setEndTime(subTask.getEndTime());
                }

            }
        } catch (IOException e) {
            throw new ManagerIOException("Нет такого файла");
        }
        seq = maxId + 1;
    }

    private Task fromString(String value) {

        String[] taskArray = value.split(",");

        TaskType type = TaskType.valueOf(taskArray[1]);
        String name = taskArray[2];
        String description = taskArray[4];
        Status status = Status.valueOf(taskArray[3]);
        LocalDateTime startTime = LocalDateTime.parse(taskArray[7]);
        Duration duration = Duration.parse(taskArray[6]);
        int id = Integer.parseInt(taskArray[0]);


        return switch (type) {
            case TASK -> new Task(name, description, status, id, type, startTime, duration);
            case EPIC -> new Epic(name, description, status, id, type, startTime, duration);

            case SUBTASK ->
                    new SubTask(name, description, status, id, Integer.parseInt(taskArray[5]), type, startTime, duration);
        };
    }

    @Override
    public Task createTask(Task task) {
        super.createTask(task);
        save();
        return task;
    }

    @Override
    public SubTask createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask;
    }

    @Override
    public Epic createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic;
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteALLEpics() {
        super.deleteALLEpics();
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void removeIdTask(int id) {
        super.removeIdTask(id);
        save();
    }

    @Override
    public void removeIdSubTasks(int id) {
        super.removeIdSubTasks(id);
        save();
    }

    @Override
    public void removeIdEpics(int id) {
        super.removeIdEpics(id);
        save();
    }

}
