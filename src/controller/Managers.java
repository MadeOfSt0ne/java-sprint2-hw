package controller;

public class Managers {
    public static InMemoryTasksManager getDefault() {
        InMemoryTasksManager manager = new InMemoryTasksManager();
        return manager;
    }
}
