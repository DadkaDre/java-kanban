package service;


import model.Task;

import java.io.File;
import java.util.List;


class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    public FileBackedTaskManager createTaskManager() {
        File file = new File("D:\\Programs\\IntelliJ IDEA\\projects\\java-kanban\\resources\\task.CSV");
        InMemoryHistoryTaskManager historyManager = new InMemoryHistoryTaskManager();
        return new FileBackedTaskManager(historyManager, file);
    }

    private static class EmptyHistoryManager implements HistoryManager {
        @Override
        public void add(Task task) {
        }

        @Override
        public List<Task> getHistory() {
            return getHistory();
        }

        @Override
        public void remove(int id) {
        }
    }

}
