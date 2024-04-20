package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryTaskManager implements HistoryManager {
    ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task){
        if(history.size()<10){
            history.add(task);
        }else{
            history.removeFirst();
            history.add(task);
        }
    }

    @Override
    public ArrayList<Task>getHistory(){
        return history ;
    }

}
