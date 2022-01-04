package model;

import static model.Status.NEW;

public class Task {
    private String name;
    private String description;
    protected Integer id;
    private String status;

    public Task() {
        this("", "", 0, NEW);
    }

    // конструктор для создания задачи по названию и номеру
    public Task(String name, Integer id) {
        this(name, "", id, NEW);
    }

    // конструктор для создания задачи по названию, описанию и номеру
    public Task(String name, String description,Integer id) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = NEW;
    }

    // конструктор для создания задачи по названию, описанию, номеру и статусу
    public Task(String name, String description,Integer id, String status) {
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
