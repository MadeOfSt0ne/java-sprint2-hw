package model;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Task> subtasks = new ArrayList<>();

    public Epic(String name, String description, Integer id, Status status) {
        super(name, description, id, status);
    }

    public Epic(String name, String description) {
        super(name, description, null);
    }

    public void addSubtask(Subtask task) {
        subtasks.add(task);
    }

    public ArrayList<Task> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Task> subtasks) {
        this.subtasks = subtasks;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks='" + subtasks.size() + '\'' +
                '}' +
                "\n";
    }
}
