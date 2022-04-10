package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.List;

public interface TaskManager {
    // получение списка задач
    List<Task> findAllTasks();
    // получение списка эпиков
    List<Epic> findAllEpics();
    // получение списка подзадач
    List<Task> findAllSubtasks(Epic epic);
    // получение задачи по id
    Task findTaskById(Integer id);
    // получение подзадачи по id
    Subtask findSubtaskById(Integer id) throws IOException;
    // получение эпика по id
    Epic findEpicById(Integer id);
    // создание новой задачи
    void createTask(Task task);
    // создание новой подзадачи
    void createSubtask(Subtask task);
    // создание нового эпика
    void createEpic(Epic task);
    // обновление задачи
    void updateTask(Task changedTask);
    // обновление подзадачи
    void updateSubtask(Subtask changedSubtask);
    // обновление эпика
    void updateEpic(Epic changedEpic);
    // вычисление статуса
    Status findEpicStatus(Epic epic);
    // удаление по id
    void deleteTask(Integer id);
    void deleteSubtask(Integer id);
    void deleteEpic(Integer id);
    // очистка списка
    void clearAllTasks();
    void clearAllSubtasks();
    void clearAllEpics();
    // получение истории просмотров
    List<Integer> history();
}
