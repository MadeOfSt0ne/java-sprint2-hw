package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class InMemoryTasksManager implements TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    int taskId = 0;
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    int subtaskId = 0;
    HashMap<Integer, Epic> epics = new HashMap<>();
    int epicId = 0;
    List<Task> history = new LinkedList<>();

    // получение списка задач
    @Override
    public List<Task> findAllTasks() {
        List<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }

    // получение списка эпиков
    @Override
    public List<Epic> findAllEpics() {
        List<Epic> allEpics = new ArrayList<>(epics.values());
        return allEpics;
    }

    // получение списка подзадач
    @Override
    public List<Task> findAllSubtasks(Epic epic) {
        List<Task> allSubtasks = epic.getSubtasks();
        return allSubtasks;
    }

    // получение задачи по id
    @Override
    public Task findTaskById(Integer id) {
        return tasks.get(id);
    }

    // получение подзадачи по id
    @Override
    public Subtask findSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            return null;
        }
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(subtask);
        return subtask;
    }

    // получение эпика по id
    @Override
    public Epic findEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            return null;
        }
        if (history.size() == 10) {
            history.remove(0);
        }
        history.add(epic);
        return epic;
    }

    // создание новой задачи
    @Override
    public Task createTask(Task task) {
        int id = ++taskId;
        Task value = new Task(task.getName(), task.getDescription(), id, Status.NEW);
        if (tasks.containsKey(task.getId())) {
            System.out.println("Такая задача уже есть: " + task.getId());
            return null;
        }
        tasks.put(task.getId(), value);
        return value;
    }

    // создание новой подзадачи
    @Override
    public Subtask createSubtask(Subtask task) {
        int id = ++subtaskId;
        Subtask value = new Subtask(task.getName(), task.getDescription(), id, Status.NEW, task.getEpicId());
        if (subtasks.containsKey(task.getId())) {
            System.out.println("Такая подзадача уже есть: " + task.getId());
            return null;
        }
        if (!epics.containsKey(task.getEpic().getId())) {
            System.out.println("Эпик не найден: " + (task.getEpic().getId()));
            return null;
        }
        subtasks.put(task.getId(), value);
        Epic epic = epics.get(task.getEpic().getId());
        epic.addSubtask(task);
        return value;
    }

    // создание нового эпика
    @Override
    public Epic createEpic(Epic epic) {
        int id = ++epicId;
        Epic value = new Epic(epic.getName(), epic.getDescription(), id, Status.NEW);
        if (epics.containsKey(epic.getId())) {
            System.out.println("Такой эпик уже есть: " + epic.getId());
            return null;
        }
        epics.put(epic.getId(), value);
        return value;
    }

    // обновление задачи
    @Override
    public Task updateTask(Task changedTask) {
        Task savedTask = tasks.get(changedTask.getId());
        if (savedTask == null) {
            return null;
        }
        savedTask.setName(changedTask.getName());
        savedTask.setDescription(changedTask.getDescription());
        savedTask.setStatus(changedTask.getStatus());
        return savedTask;
    }

    // обновление подзадачи
    @Override
    public Subtask updateSubtask(Subtask changedSubtask) {
        Subtask savedSubtask = subtasks.get(changedSubtask.getId());
        if (savedSubtask == null) {
            return null;
        }
        savedSubtask.setName(changedSubtask.getName());
        savedSubtask.setDescription(changedSubtask.getDescription());
        savedSubtask.setStatus(changedSubtask.getStatus());
        return savedSubtask;
    }

    // обновление эпика
    @Override
    public Epic updateEpic(Epic changedEpic) {
        Epic savedEpic = epics.get(changedEpic.getId());
        if (savedEpic == null) {
            return null;
        }
        savedEpic.setName(changedEpic.getName());
        savedEpic.setDescription(changedEpic.getDescription());
        return savedEpic;
    }

    // метод для вычисления статуса
    @Override
    public Status findEpicStatus(Epic epic) {
        findAllSubtasks(epic);
        if (findAllSubtasks(epic) == null) {
            System.out.println("Статус эпика - NEW");
            return Status.NEW;
        }
        int counterNew = 0;
        int counterDone = 0;
        int counterInProgress = 0;
        for (Task task : findAllSubtasks(epic)) {
            if (task.getStatus().equals(Status.NEW)) {
                counterNew++;
            } else if (task.getStatus().equals(Status.DONE)) {
                counterDone++;
            } else if (task.getStatus().equals(Status.IN_PROGRESS)) {
                counterInProgress++;
            }
        }
        if (counterInProgress > 0) {
            System.out.println("Статус эпика - IN_PROGRESS");
            return Status.IN_PROGRESS;
        } else if (counterNew > 0 && counterDone ==0) {
            System.out.println("Статус эпика - NEW");
            return Status.NEW;
        } else if (counterNew == 0 && counterDone > 0) {
            System.out.println("Статус эпика - DONE");
            return Status.DONE;
        } else {
            return Status.IN_PROGRESS;
        }
    }

    // удаление по id
    @Override
    public Task deleteTask(Integer id) {
        return tasks.remove(id);
    }

    @Override
    public Subtask deleteSubtask(Integer id) {
        return subtasks.remove(id);
    }

    @Override
    public Epic deleteEpic(Integer id) {
        return epics.remove(id);
    }

    // очистка списка
    @Override
    public void clearAllTasks() {
        tasks.clear();
    }

    @Override
    public void clearAllSubtasks() {
        subtasks.clear();
    }

    @Override
    public void clearAllEpics() {
        epics.clear();
    }

    // метод для получения истории
    @Override
    public List<Task> history() {
        return history;
    }
}
