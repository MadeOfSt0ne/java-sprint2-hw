package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Task> subtasks = new ArrayList<>();

    public void addSubtask(Subtask task) {
        subtasks.add(task);
    }

    public ArrayList<Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Task> subtasks) {
        this.subtasks = subtasks;
    }
}
