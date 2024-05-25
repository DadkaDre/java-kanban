package service;


import java.io.File;

public class Managers {
    public static TaskManager getDefault() {
        return new FileBackedTaskManager(getDefaultHistory(),
                new File("task.CSV"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryTaskManager();
    }
}

