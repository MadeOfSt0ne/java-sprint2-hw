package model;

import java.util.ArrayList;

public class Epic extends Task {
    private TaskType taskType;
    private ArrayList<Task> subtasks = new ArrayList<>();

    // эпик создается без заданного времени начала, времени окончания и продолжительности: они вычисляются на основе подзадач
    public Epic(String name, String description, Integer id, Status status) {
        super(name, description, id, status);
    }

    public void addSubtask(Subtask task) {
        subtasks.add(task);
    }

    public ArrayList<Task> getSubtasks() {
        return subtasks;
    }

    @Override
    public TaskType getTaskType() {
        return taskType.EPIC;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks='" + subtasks.size() + '\'' +
                '}';
    }
}
