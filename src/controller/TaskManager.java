package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.List;

public interface TaskManager {
    // Вопрос: комментарии, описывающие методы, лучше перенести в интерфейс или оставить в классе,
    // который реализует этот интерфейс(InMemoryTaskManager)?
    // получение списка всех задач, подзадач, эпиков
    List<Task> findAllTasks();
    List<Epic> findAllEpics();
    List<Task> findAllSubtasks(Epic epic);
    // нахождение по id
    Task findTaskById(Integer id);
    Subtask findSubtaskById(Integer id);
    Epic findEpicById(Integer id);
    // создание
    Task createTask(Task task);
    Subtask createSubtask(Subtask task);
    Epic createEpic(Epic task);
    // обновление
    Task updateTask(Task changedTask);
    Subtask updateSubtask(Subtask changedSubtask);
    Epic updateEpic(Epic changedEpic);
    // вычисление статуса
    Status findEpicStatus(Epic epic);
    // удаление по id
    Task deleteTask(Integer id);
    Subtask deleteSubtask(Integer id);
    Epic deleteEpic(Integer id);
    // очистка списка
    void clearAllTasks();
    void clearAllSubtasks();
    void clearAllEpics();
    // получение истории
    List<Task> history();
}
