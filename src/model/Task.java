package model;

import java.net.InetSocketAddress;
import java.time.LocalTime;

public class Task {
    private String name;
    private String description;
    protected Integer id;
    private int epicId;
    private Status status;
    private TaskType taskType;
    private int duration;
    private int HH;
    private int mm;
    LocalTime startTime = LocalTime.of(HH, mm);

    // конструктор для создания задачи со всеми полями кроме статуса
    public Task(String name, String description, Integer id, LocalTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    // конструктор для создания задачи по названию, описанию, номеру, статусу, времени начала и продолжительности
    public Task(String name, String description, Integer id, Status status, LocalTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    // конструктор для создания задачи по названию, описанию, номеру, статусу, времени начала и продолжительности
    public Task(String name, String description, Integer id, Status status, LocalTime startTime, int duration, Integer epicId) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
        this.epicId = epicId;
    }
    // конструктор для создания задачи путем копирования входящей задачи
    public Task(Task task) {
        this.name = task.name;
        this.description = task.description;
        this.id = task.id;
        this.status = task.status;
        this.startTime = task.startTime;
        this.duration = task.duration;
    }

    // конструктор для создания эпика: у эпика свои значения startTime и duration
    public Task(String name, String description, Integer id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task() {

    }

    public Task(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
    }

    public TaskType getTaskType() {
        return taskType.TASK;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // для id нет сеттера, так как его не нужно менять
    public Integer getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime = startTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public Integer getEpicId() {
        return epicId;
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

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", id=" + id +
                ", status=" + status +
                ", startTime=" + startTime +
                ", duration=" + duration +
                '}';
    }
}
