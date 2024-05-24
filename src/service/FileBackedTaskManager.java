package service;

import converter.TaskConverter;
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
        file = new File("task.CSV");

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
            writer.write("id,type,name,status,description,epicId:");
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
                //  String string = String.format("%30s,%30s,%30s,%30s,%30s,%30s,",TaskConverter.toString(subTask));
                //  System.out.println(string);

                writer.write(TaskConverter.toString(subTask));
                writer.newLine();
            }

        } catch (IOException e) {
            throw new RuntimeException("Нет файла: task.CSV");
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
                        break;
                    case EPIC:
                        epics.put(id, (Epic) task);
                        break;
                    case SUBTASK:
                        subTasks.put(id, (SubTask) task);
                        break;
                }
                if (maxId < id) {
                    maxId = id;
                }
            }
            for (SubTask subTask : subTasks.values()) {
                Epic epic = epics.get(subTask.getEpicId());
                epic.addSubTask(subTask);


            }
        } catch (IOException e) {
            throw new RuntimeException("Нет такого файла");
        }
        seq = maxId;
    }

    private Task fromString(String value) {

        String[] taskArray = value.split(",");

        TaskType type = TaskType.valueOf(taskArray[1]);

        Task task = switch (type) {
            case TASK ->
                    new Task(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]), Integer.parseInt(taskArray[0]),
                            TaskType.valueOf(taskArray[1]));
            case EPIC ->
                    new Epic(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]), Integer.parseInt(taskArray[0]),
                            TaskType.valueOf(taskArray[1]));
            case SUBTASK -> new SubTask(taskArray[2], taskArray[4], Status.valueOf(taskArray[3]),
                    Integer.parseInt(taskArray[0]), Integer.parseInt(taskArray[5]), TaskType.valueOf(taskArray[1]));
        };

         /* Integer id = Integer.valueOf(taskArray[0]);
            TaskType type = TaskType.valueOf(taskArray[1]);
            String name = taskArray[2];
            Status status = Status.valueOf(taskArray[3]);
            String description = taskArray[4];
            Integer epicId = Integer.parseInt(taskArray[5]);


             switch (type) {
                case TASK -> new Task(name, description, status, id);
                case SUBTASK -> new SubTask(name, description, status, id, epicId);
                case EPIC -> new Epic(name, description, status, id);
                default -> throw new ManagerSaveException("Нет такого типа задач");*/


        return task;
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
