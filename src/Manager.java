import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

import static model.Status.NEW;
import static model.Status.IN_PROGRESS;
import static model.Status.DONE;

public class Manager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    int taskId = 0;
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    int subtaskId = 0;
    HashMap<Integer, Epic> epics = new HashMap<>();
    int epicId = 0;

    // получение списка задач
    public ArrayList<Task> findAllTasks() {
        ArrayList<Task> allTasks = new ArrayList<>(tasks.values());
        return allTasks;
    }

    // получение списка эпиков
    public ArrayList<Task> findAllEpics() {
        ArrayList<Task> allEpics = new ArrayList<>(epics.values());
        return allEpics;
    }

    // получение списка подзадач
    public Epic findAllSubTasks(Integer epicId) {
        return epics.get(epicId);
    }

    // получение задачи по id
    public Task findById(Integer id) {
        return tasks.get(id);
    }

    // создание новой задачи
    public Task createTask(Task task) {
        int id = ++taskId;
        Task value = new Task(task.getName(), task.getDescription(), id, NEW);
        if (tasks.containsKey(task.getId())) {
            System.out.println("Такая задача уже есть: " + task.getId());
            return null;
        }
        tasks.put(task.getId(), value);
        return value;
    }

    // создание новой подзадачи
    public Subtask createSubtask(Subtask task) {
        int id = ++subtaskId;
        Subtask value = new Subtask();
        if (subtasks.containsKey(task.getId())) {
            System.out.println("Такая задача уже есть: " + task.getId());
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

    // создание нового эпика. если это измененный существующий, то перезаписываем
    public Epic createEpic(Epic newEpic) {
        int id = ++epicId;
        Epic oldEpic = epics.get(newEpic.getId());
        if (oldEpic == null) {
            return null;
        }
        oldEpic.setName(newEpic.getName());
        oldEpic.setDescription(newEpic.getDescription());
        return oldEpic;
    }

    // обновление задачи
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
    public Epic updateEpic(Epic changedEpic) {
        Epic savedEpic = epics.get(changedEpic.getId());
        if (savedEpic == null) {
            return null;
        }
        savedEpic.setName(changedEpic.getName());
        savedEpic.setDescription(changedEpic.getDescription());
        return savedEpic;
    }

    // метод для вычисления статуса ... in_progress
    private String findStatus(Integer epicId) {
        return "status";
    }

    // удаление по id
    public Task deleteTask(Integer id) {
        return tasks.remove(id);
    }

    public Subtask deleteSubtask(Integer id) {
        return subtasks.remove(id);
    }

    public Epic deleteEpic(Integer id) {
        return epics.remove(id);
    }

    // очистка списка
    public void clearAllTasks() {
        tasks.clear();
    }

    public void clearAllSubtasks() {
        subtasks.clear();
    }

    public void clearAllEpics() {
        epics.clear();
    }
}
