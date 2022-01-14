package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> findAllTasks();
    ArrayList<Epic> findAllEpics();
    ArrayList<Task> findAllSubtasks(Epic epic);
    Task findById(Integer id);
    Task createTask(Task task);
    Subtask createSubtask(Subtask task);
    Epic createEpic(Epic task);
    Task updateTask(Task changedTask);
    Subtask updateSubtask(Subtask changedSubtask);
    Epic updateEpic(Epic changedEpic);
    Status findEpicStatus(Epic epic);
    Task deleteTask(Integer id);
    Subtask deleteSubtask(Integer id);
    Epic deleteEpic(Integer id);
    void clearAllTasks();
    void clearAllSubtasks();
    void clearAllEpics();
}
