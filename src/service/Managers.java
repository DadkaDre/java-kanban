package service;


import java.nio.file.Paths;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(getDefaultHistory(),
                Paths.get("D:/Programs/IntelliJ IDEA/projects/java-kanban/resources/task.CSV"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryTaskManager();
    }
}

