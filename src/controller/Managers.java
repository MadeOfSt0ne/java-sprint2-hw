package controller;

public class Managers {
    public static TaskManager getDefault() {
        InMemoryTasksManager manager = new InMemoryTasksManager();
        return manager;
    }
}
