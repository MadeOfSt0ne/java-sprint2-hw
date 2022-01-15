package model;

public class Subtask extends Task {
    private Epic epic;
    Integer epicId;
    public Subtask(Integer epicId) {
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer id, Status status, Integer epicId) {
        super(name, description, id, status);
        this.epicId = epicId;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public Epic getEpic() {
        return epic;
    }
}
