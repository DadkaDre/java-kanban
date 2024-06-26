package service;

import java.io.File;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    public FileBackedTaskManager createTaskManager() {
        File file = new File("D:\\Programs\\IntelliJ IDEA\\projects\\java-kanban\\resources\\task.CSV");
        InMemoryHistoryTaskManager historyManager = new InMemoryHistoryTaskManager();
        return new FileBackedTaskManager(historyManager, file);
    }

}
