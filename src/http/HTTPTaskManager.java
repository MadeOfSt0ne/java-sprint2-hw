package http;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import controller.FileBackedTasksManager;
import model.Epic;
import model.Subtask;
import model.Task;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;

public class HTTPTaskManager extends FileBackedTasksManager {
    public KVTaskClient kvTaskClient;
    public final String TASKS_KEY = "TASKS";
    public final String HISTORY_KEY = "HISTORY";
    public final String SUBS_KEY = "SUBTASKS";
    public final String EPICS_KEY = "EPICS";
    private List<Task> allTasks = new ArrayList<>();
    private List<Task> allSubs = new ArrayList<>();
    private List<Epic> allEpics = new ArrayList<>();
    private List<Integer> httpHistory = new ArrayList<>();
    TreeMap<LocalTime, Integer> prioritizedTasks = new TreeMap<>();
    public HTTPTaskManager() {
        kvTaskClient = new KVTaskClient();
    }
    Gson gson = new Gson();

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        allTasks.add(task);
        kvTaskClient.put(TASKS_KEY, gson.toJson(allTasks));
        httpHistory.add(task.getId());
        kvTaskClient.put(HISTORY_KEY, gson.toJson(httpHistory));
    }

    @Override
    public void createSubtask(Subtask task) {
        super.createSubtask(task);
        try {

        allSubs.add(task);
        kvTaskClient.put(SUBS_KEY, gson.toJson(allSubs));
        httpHistory.add(task.getId());
        kvTaskClient.put(HISTORY_KEY, gson.toJson(httpHistory));
    } catch (IllegalArgumentException e) {
        e.printStackTrace();
    }
    }

    @Override
    public void createEpic(Epic epic) {

            super.createEpic(epic);
            allEpics.add(epic);
            httpHistory.add(epic.getId());
            kvTaskClient.put(EPICS_KEY, gson.toJson(allEpics));
            kvTaskClient.put(HISTORY_KEY, gson.toJson(httpHistory));

    }

    public TreeMap<LocalTime, Integer> getPrioritized() {
        try {
            List<Task> allT = gson.fromJson(kvTaskClient.load(TASKS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
            for (Task task : allT) {
                prioritizedTasks.put(task.getStartTime(), task.getId());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        try {
            List<Task> allS = gson.fromJson(kvTaskClient.load(SUBS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
            for (Task sub : allS) {
                prioritizedTasks.put(sub.getStartTime(), sub.getId());
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return prioritizedTasks;
    }

    @Override
    public Task findTaskById(Integer id) {
        try {
            super.findTaskById(id);
            List<Task> findTask = gson.fromJson(kvTaskClient.load(TASKS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
            for (Task task : findTask) {
                if (task.getId().equals(id)) {
                    httpHistory.add(id);
                    kvTaskClient.put(HISTORY_KEY, gson.toJson(httpHistory));
                    return task;
                } else {
                    System.out.println("Задача не найдена на сервере!");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Subtask findSubtaskById(Integer id) {
        try {
            super.findSubtaskById(id);
            List<Task> findSub = gson.fromJson(kvTaskClient.load(SUBS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
            for (Task task : findSub) {
                if (task.getId().equals(id)) {
                    httpHistory.add(id);
                    kvTaskClient.put(HISTORY_KEY, gson.toJson(httpHistory));
                    return (Subtask) task;
                } else {
                    System.out.println("Подзадача не найдена на сервере!");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Epic findEpicById(Integer id) {
        try {
            super.findEpicById(id);
            List<Epic> findEpic = gson.fromJson(kvTaskClient.load(EPICS_KEY)
                    , new TypeToken<ArrayList<Epic>>() {}.getType());
            for (Epic epic : findEpic) {
                if (epic.getId().equals(id)) {
                    httpHistory.add(id);
                    kvTaskClient.put(HISTORY_KEY, gson.toJson(httpHistory));
                    return epic;
                } else {
                    System.out.println("Эпик не найден на сервере!");
                }
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void deleteTask(Integer id) {
        super.deleteTask(id);
        try {
            List<Task> minusTask = gson.fromJson(kvTaskClient.load(TASKS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
            minusTask.removeIf(task -> (Objects.equals(task.getId(), id)));
            kvTaskClient.put(TASKS_KEY, gson.toJson(minusTask));
            removeFromHistory(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    // вспомогательный метод для удаления из истории
    public void removeFromHistory(Integer id) {
        try {
            List<Integer> h = gson.fromJson(kvTaskClient.load(HISTORY_KEY)
                    , new TypeToken<ArrayList<Integer>>() {}.getType());
            h.removeIf(h::contains);
            kvTaskClient.put(HISTORY_KEY, gson.toJson(h));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteSubtask(Integer id) {
        super.deleteSubtask(id);
        try {
            List<Task> minusSub = gson.fromJson(kvTaskClient.load(SUBS_KEY)
                    , new TypeToken<ArrayList<Task>>() {
                    }.getType());
            minusSub.removeIf(task -> (Objects.equals(task.getId(), id)));
            kvTaskClient.put(SUBS_KEY, gson.toJson(minusSub));
            removeFromHistory(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteEpic(Integer id) {
        super.deleteEpic(id);
        try {

            List<Epic> minusEpic = gson.fromJson(kvTaskClient.load(EPICS_KEY)
                    , new TypeToken<ArrayList<Epic>>() {}.getType());
            minusEpic.removeIf(epic -> (Objects.equals(epic.getId(), id)));
            kvTaskClient.put(EPICS_KEY, gson.toJson(minusEpic));
            removeFromHistory(id);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void clearAllTasks() {
        super.clearAllTasks();
        List<Task> emptyTasks = new ArrayList<>();
        kvTaskClient.put(TASKS_KEY, gson.toJson(emptyTasks));
    }

    @Override
    public void clearAllSubtasks() {
        super.clearAllSubtasks();
        List<Task> emptySubs = new ArrayList<>();
        kvTaskClient.put(SUBS_KEY, gson.toJson(emptySubs));
    }

    @Override
    public void clearAllEpics() {
        super.clearAllEpics();
        List<Epic> emptyEpics = new ArrayList<>();
        kvTaskClient.put(EPICS_KEY, gson.toJson(emptyEpics));
    }

    // Методы возвращают либо найденный список, либо пустой новый список. Возвращение null осложняет тестирование.
    @Override
    public List<Integer> history() {
        try {
            return gson.fromJson(kvTaskClient.load(HISTORY_KEY)
                    , new TypeToken<ArrayList<Integer>>() {}.getType());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Task> getAllTasks() {
        try {
            return gson.fromJson(kvTaskClient.load(TASKS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Epic> getAllEpics() {
        try {
            return gson.fromJson(kvTaskClient.load(EPICS_KEY)
                    , new TypeToken<ArrayList<Epic>>() {}.getType());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public List<Task> getSubsByEpicId(Integer id) {
        try {
            List<Task> all = gson.fromJson(kvTaskClient.load(SUBS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
            List<Task> result = new ArrayList<>();
            for (Task task : all) {
                if (task.getEpicId().equals(id)) {
                    result.add(task);
                }
            }
            return result;
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    @Override
    public List<Task> getAllSubtasks() {
        try {
            return gson.fromJson(kvTaskClient.load(SUBS_KEY)
                    , new TypeToken<ArrayList<Task>>() {}.getType());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    // Вспомогательный метод для тестов. Очищает все списки.
    public void fullClear() {
        httpHistory.clear();
        allSubs.clear();
        allTasks.clear();
        allEpics.clear();
    }
}
