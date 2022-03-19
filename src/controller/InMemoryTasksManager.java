package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.LocalTime;
import java.util.*;

public class InMemoryTasksManager implements TaskManager {
    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    InMemoryHistoryManager history = new InMemoryHistoryManager();
    TreeMap<LocalTime, Integer> epicTimeTracker = new TreeMap<>();
    TreeMap<LocalTime, Integer> timeTracker = new TreeMap<>();

    // получение списка задач
    @Override
    public List<Task> findAllTasks() {
         return new ArrayList<>(tasks.values());
    }

    // получение списка эпиков
    @Override
    public List<Epic> findAllEpics() {
        return new ArrayList<>(epics.values());
    }

    // получение списка подзадач
    @Override
    public List<Task> findAllSubtasks(Epic epic) {
        return epics.get(epic.getId()).getSubtasks();
    }

    // получение задачи по id
    @Override
    public Task findTaskById(Integer id) {
        Task task = tasks.get(id);
        if (task == null) {
            System.out.print("task not found: ");
            return null;
        }
        history.add(task);
        return task;
    }

    // получение подзадачи по id
    @Override
    public Subtask findSubtaskById(Integer id) {
        Subtask subtask = subtasks.get(id);
        if (subtask == null) {
            System.out.print("subtask not found: ");
            return null;
        }
        history.add(subtask);
        return subtask;
    }

    // получение эпика по id
    @Override
    public Epic findEpicById(Integer id) {
        Epic epic = epics.get(id);
        if (epic == null) {
            System.out.print("epic not found: ");
            return null;
        }
        history.add(epic);
        return epic;
    }

    // создание новой задачи с проверкой доступности времени
    @Override
    public Task createTask(Task task) {
        Task value = new Task(task.getName(), task.getDescription(), task.getId(),
                task.getStatus(), task.getStartTime(), task.getDuration());
        if (!isVacantTime(task.getStartTime(), task.getDuration())) {
            System.out.println("Это время уже занято другой задачей!");
            return null;
        }
        if (tasks.containsKey(task.getId())) {
            System.out.println("Такая задача уже есть: " + task.getId());
            return null;
        }
        tasks.put(task.getId(), value);
        return value;
    }

    // создание нового эпика
    @Override
    public Epic createEpic(Epic epic) {
        Epic value = new Epic(epic.getName(), epic.getDescription(), epic.getId(),
                epic.getStatus());
        if (epics.containsKey(epic.getId())) {
            System.out.println("Такой эпик уже есть: " + epic.getId());
            return null;
        }
        epics.put(epic.getId(), value);
        return value;
    }

    // создание новой подзадачи с проверкой доступности времени
    @Override
    public Subtask createSubtask(Subtask task) {
        if (!epics.containsKey(task.getEpicId())) {
            System.out.println("Эпик не найден: " + (task.getEpicId()));
            return null;
        }
        if (!isVacantTime(task.getStartTime(), task.getDuration())) {
            System.out.println("Это время уже занято другой задачей!");
            return null;
        }
        if (subtasks.containsKey(task.getId())) {
            System.out.println("Такая подзадача уже есть: " + task.getId());
            return null;
        }
        Subtask value = new Subtask(task.getName(), task.getDescription(), task.getId(), task.getStatus(),
                    task.getEpicId(), task.getStartTime(), task.getDuration());
            subtasks.put(task.getId(), value);
            Epic epic = epics.get(task.getEpicId());
            epic.addSubtask(task);
            return value;
    }

    // обновление задачи с проверкой доступности времени
    @Override
    public Task updateTask(Task changedTask) {
        Task savedTask = tasks.get(changedTask.getId());
        if (!isVacantTime(changedTask.getStartTime(), changedTask.getDuration())) {
            System.out.println("Это время уже занято другой задачей!");
            return null;
        }
        if (savedTask == null) {
            return null;
        }
        savedTask.setName(changedTask.getName());
        savedTask.setDescription(changedTask.getDescription());
        savedTask.setStatus(changedTask.getStatus());
        savedTask.setStartTime(changedTask.getStartTime());
        savedTask.setDuration(changedTask.getDuration());
        return savedTask;
    }

    // обновление подзадачи с проверкой доступности времени
    @Override
    public Subtask updateSubtask(Subtask changedSubtask) {
        Subtask savedSubtask = subtasks.get(changedSubtask.getId());
        if (!isVacantTime(changedSubtask.getStartTime(), changedSubtask.getDuration())) {
            System.out.println("Это время уже занято другой задачей!");
            return null;
        }
        if (savedSubtask == null) {
            return null;
        }
        savedSubtask.setName(changedSubtask.getName());
        savedSubtask.setDescription(changedSubtask.getDescription());
        savedSubtask.setStatus(changedSubtask.getStatus());
        savedSubtask.setStartTime(changedSubtask.getStartTime());
        savedSubtask.setDuration(changedSubtask.getDuration());
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
        findEpicStatus(changedEpic);
        return savedEpic;
    }

    // метод для вычисления статуса
    @Override
    public Status findEpicStatus(Epic epic) {
        findAllSubtasks(epic);
        if (findAllSubtasks(epic) == null) {
            System.out.println("Статус эпика - NEW");
            epic.setStatus(Status.NEW);
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
            epic.setStatus(Status.IN_PROGRESS);
            return Status.IN_PROGRESS;
        } else if (counterNew > 0 && counterDone ==0) {
            System.out.println("Статус эпика - NEW");
            epic.setStatus(Status.NEW);
            return Status.NEW;
        } else if (counterNew == 0 && counterDone > 0) {
            System.out.println("Статус эпика - DONE");
            epic.setStatus(Status.DONE);
            return Status.DONE;
        } else {
            System.out.println("Статус эпика - IN_PROGRESS");
            epic.setStatus(Status.IN_PROGRESS);
            return Status.IN_PROGRESS;
        }
    }

    // вычисление продолжительности эпика (сумма продолжительностей всех подзадач)
    public int findEpicDuration(int epicId) {
        int epicDuration = 0;
        if (epics.containsKey(epicId)) {
            for (Task subtask : epics.get(epicId).getSubtasks()) {
                epicDuration += subtask.getDuration();
            }
        } else {
            System.out.println("Эпик не найден: " + epicId);
        }
        return epicDuration;
    }

    // вычисление начала и окончания эпика
    // сначала обходим список подзадач и записываем время в TreeSet. потом выводим первое значение - время начала эпика.
    // далее берем последний элемент дерева и ищем совпадение в списке подзадач, достаем из подзадачи продолжительность
    // и прибавляем её к времени начала. так находим время окончания эпика.
    // минус метода заключается в больших затратах времени
    // плюс - в простоте и надежности =)
    /*public void findEpicStartAndEndTime(int epicId) {
        if (epics.containsKey(epicId)) {
            for (Task subtask : epics.get(epicId).getSubtasks()) {
                epicTimeTracker.add(subtask.getStartTime());
            }
            LocalTime epicStartTime = epicTimeTracker.first();
            System.out.println("Время начала эпика: " + epicStartTime);
            for (Task subtask: epics.get(epicId).getSubtasks()) {
                if (epicTimeTracker.last().equals(subtask.getStartTime())) {
                    LocalTime epicEndTime = epicTimeTracker.last().plusMinutes(subtask.getDuration());
                    System.out.println("Время окончания эпика: " + epicEndTime);
                }
            }
        } else {
            System.out.println("Эпик не найден: " + epicId);
        }
    }*/

    // вычисление времени начала и окончания эпика через TreeMap: время начала и продолжительность.
    // одним проходом по HashMap забираем время начала подзадачи и её продолжительность, записываем их в treemap
    // и получаем сортировку, а также быстрый доступ к первому и последнему элементам
    public void findEpicStartEndTime(int epicId) {
        if (epics.containsKey(epicId)) {
            for (Task subtask : epics.get(epicId).getSubtasks()) {
                epicTimeTracker.put(subtask.getStartTime(), subtask.getDuration());
            }
            LocalTime epicStartTime = epicTimeTracker.firstKey();
            System.out.println("Время начала эпика: " + epicStartTime);
            int lastSubTaskDuration = epicTimeTracker.get(epicTimeTracker.lastKey());
            LocalTime epicEndTime = epicTimeTracker.lastKey().plusMinutes(lastSubTaskDuration);
            System.out.println("Время окончания эпика: " + epicEndTime);
        } else {
            System.out.println("Эпик не найден: " + epicId);
        }
    }

    // вычисление времени окончания задачи или подзадачи
    public LocalTime findEndTime(int id) {
        if (tasks.containsKey(id)) {
            LocalTime endTime = tasks.get(id).getStartTime();
            return endTime.plusMinutes(tasks.get(id).getDuration());
        } else if (subtasks.containsKey(id)) {
            LocalTime endTime = subtasks.get(id).getStartTime();
            return endTime.plusMinutes(subtasks.get(id).getDuration());
        } else {
            System.out.println("Задача с таким id не найдена: " + id);
            return null;
        }
    }

    // заполняем TreeMap задачами и подзадачами: key - время начала, value - продолжительность задачи
    public TreeMap getPrioritizedTasks() {
        for (Task task : findAllTasks()) {
            timeTracker.put(task.getStartTime(), task.getDuration());
        }
        for (Epic epic : findAllEpics()) {
            for (Task subtask : findAllSubtasks(epic)) {
                timeTracker.put(subtask.getStartTime(), subtask.getDuration());
            }
        }
        return timeTracker;
    }

    // метод проверки свободного временного интервала для создания задачи
    public boolean isVacantTime(LocalTime taskStartTime, int taskDuration) {
        LocalTime taskEndTime = taskStartTime.plusMinutes(taskDuration);
        timeTracker = getPrioritizedTasks();
        LocalTime prevTime = timeTracker.floorKey(taskStartTime);
        LocalTime nextTime = timeTracker.ceilingKey(taskStartTime);
        // если время начала задачи позже времени окончания предыдущей задачи и время окончания задачи раньше времени
        // начала следующей задачи, то временной интервал свободен
        if ((prevTime == null || taskStartTime.isAfter(prevTime.plusMinutes(taskDuration)))
                && (nextTime == null || taskEndTime.isBefore(nextTime))) {
            return true;
        } else {
            return false;
        }
    }

    // удаление по id
    @Override
    public void deleteTask(Integer id) {
        tasks.remove(id);
        history.remove(id);
    }

    @Override
    public void deleteSubtask(Integer id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask == null) {
            return;
        }
        Epic epic = epics.get(subtask.getEpicId());
        epic.getSubtasks().remove(subtask);
        history.remove(id);
    }

    @Override
    public void deleteEpic(Integer id) {
         Epic epic = epics.remove(id);
         if (epic == null) {
             return;
         }
         for (Task subtask : epic.getSubtasks()) {
             subtasks.remove(subtask.getId());
             history.remove(id);
         }
         history.remove(id);
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

    // получение истории просмотров
    @Override
    public List<Integer> history() {
        return history.getHistory();
    }
}
