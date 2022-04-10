package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;
import model.TaskType;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class HTTPTaskManager extends FileBackedTasksManager {
    public KVTaskClient kvTaskClient;
    public final String TASKS_KEY = "ALLTASKS";
    public final String HISTORY_KEY = "HISTORY";
    List<String> allTasks = new ArrayList<>();
    List<Integer> history = new ArrayList<>();
    TreeMap<LocalTime, Integer> prioritizedTasks = new TreeMap<>();
    public HTTPTaskManager() {
        kvTaskClient = new KVTaskClient();
    }
    Gson gson;

    // Метод поштучно сериализует задачи, эпики, подзадачи в json, собирает эти json в список и загружает список на
    // сервер с выбранным ключом. Таким образом в таблице data хранятся два списка: один с задачами, другой с историей.
    public void saveToKv() {
        try {
            for (Task task : findAllTasks()) {
                allTasks.add(gson.toJson(task));
                history.add(task.getId());
            }
            for (Epic epic : findAllEpics()) {
                allTasks.add(gson.toJson(epic));
                history.add(epic.getId());
                for (Task sub : findAllSubtasks(epic)) {
                    allTasks.add(gson.toJson(sub));
                    history.add(sub.getId());
                }
            }
            kvTaskClient.put(TASKS_KEY, gson.toJson(allTasks));
            kvTaskClient.put(HISTORY_KEY, gson.toJson(history));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        saveToKv();
    }

    @Override
    public void createSubtask(Subtask task) {
        super.createSubtask(task);
        saveToKv();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        saveToKv();
    }


    public TreeMap<LocalTime, Integer> prioritizedTasks() {

        List<Task> allTasks = gson.fromJson(kvTaskClient.load(TASKS_KEY)
                , new TypeToken<ArrayList<Task>>() {}.getType());
        for (Task task : allTasks) {
            prioritizedTasks.put(task.getStartTime(), task.getId());
        }
        return prioritizedTasks;
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        saveToKv();
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        saveToKv();
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        saveToKv();
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        saveToKv();
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        saveToKv();
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        saveToKv();
    }

    @Override
    public List<Integer> history() {
        return gson.fromJson(kvTaskClient.load(HISTORY_KEY)
                , new TypeToken<ArrayList<Integer>>() {}.getType());
    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();
        try {
            List<Task> allTasks = gson.fromJson(kvTaskClient.load(TASKS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
            for (Task task : allTasks) {
                if (task.getTaskType().equals(TaskType.TASK)) {
                    tasks.add(task);
                }
            }
            return tasks;
        }catch (NullPointerException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    public List<Epic> getAllEpics() {
        List<Epic> epics = new ArrayList<>();
        List<Task> allTasks =  gson.fromJson(kvTaskClient.load(TASKS_KEY)
                , new TypeToken<ArrayList<Epic>>() {}.getType());
        for (Task task : allTasks) {
            if (task.getTaskType().equals(TaskType.EPIC)) {
                epics.add((Epic) task);
            }
        }
        return epics;
    }

    public List<Task> getSubsByEpicId(Integer id) {
        List<Task> allSubtasks = gson.fromJson(kvTaskClient.load(TASKS_KEY)
                , new TypeToken<ArrayList<Task>>() {}.getType());
        List<Task> result = new ArrayList<>();
        for (Task task : allSubtasks) {
            if (task.getEpicId().equals(id)) {
               result.add(task);
            }
        }
        return result;
    }

    @Override
    public List<Task> getAllSubtasks() {
        List<Task> subs = new ArrayList<>();
        List<Task> allTasks = gson.fromJson(kvTaskClient.load(TASKS_KEY)
                , new TypeToken<ArrayList<Task>>() {}.getType());
        for (Task task : allTasks) {
            if (task.getTaskType().equals(TaskType.SUBTASK)) {
                subs.add(task);
            }
        }
        return subs;
    }
}
