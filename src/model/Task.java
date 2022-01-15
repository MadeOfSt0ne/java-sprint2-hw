package model;

public class Task {
    private String name;
    private String description;
    protected Integer id;
    private Status status;

    public Task() {
        this("", "", 0, Status.NEW);
    }

    // конструктор для создания задачи по названию и номеру
    public Task(String name, Integer id) {
        this(name, "", id, Status.NEW);
    }

    // конструктор для создания задачи по названию, описанию и номеру
    public Task(String name, String description, Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = Status.NEW;
    }

    // конструктор для создания задачи по названию, описанию, номеру и статусу
    public Task(String name, String description,Integer id, Status status) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    // конструктор для создания задачи путем копирования входящей задачи
    public Task(Task task) {
        this.name = task.name;
        this.description = task.description;
        this.id = task.id;
        this.status = task.status;
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
                '}';
    }
}
