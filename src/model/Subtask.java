package model;

import java.time.LocalTime;

public class Subtask extends Task {
    private Integer epicId;
    private TaskType taskType;

    public Subtask(String name, String description, Integer id, Status status, Integer epicId, LocalTime startTime, int duration) {
        super(name, description, id, status, startTime, duration);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    // может пригодиться для переноса сабтаска от одного эпика к другому
    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    @Override
    public TaskType getTaskType() {
        return taskType.SUBTASK;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                ", startTime=" + startTime +
                ", duration=" + getDuration() +
                '}';
    }
}
