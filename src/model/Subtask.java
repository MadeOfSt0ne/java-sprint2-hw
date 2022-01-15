package model;

public class Subtask extends Task {
    //private Epic epic;
    private Integer epicId;

    public Subtask(Integer epicId) {
        this.epicId = epicId;
    }

    public Subtask(String name, String description, Integer id, Status status, Integer epicId) {
        super(name, description, id, status);
        this.epicId = epicId;

    }

    //public void setEpic(Epic epic) {
   //     this.epic = epic;
    //}

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }
//public Epic getEpic() {
     //   return epic;
   // }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epicId=" + epicId +
                '}' +
                "\n";
    }
}
