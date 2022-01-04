package model;

import java.util.ArrayList;

public class Epic extends Task {
    ArrayList<Task> subtasks = new ArrayList<>();

    public void addSubtask(Subtask task) {
        subtasks.add(task);
    }

    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Task task = (Task) obj;
        return id == task.id.intValue();
    }
}
