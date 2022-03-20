package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest {
    InMemoryTasksManager inMemoryTasksManager = new InMemoryTasksManager();
    Task task = new Task("task 1", "thisIsTask1", 1, LocalTime.of(10, 00), 15);
    Task task2 = new Task("task 2", "thisIsTask2", 2, LocalTime.of(10, 00), 15);
    Task task3 = new Task("task 3", "thisIsTask3", 1, LocalTime.of(11, 00), 15);

    Epic epic = new Epic("epic 1", "thisIsEpic1", 11, Status.NEW);
    Epic epic2 = new Epic("epic 2", "thisIsEpic2", 11, Status.IN_PROGRESS);

    Subtask subtask = new Subtask("correctSub", "description 1", 21, Status.DONE, 11, LocalTime.of(10, 20), 15);
    Subtask subtask1 = new Subtask("correctSub", "description 1", 29, Status.DONE, 11, LocalTime.of(12, 20), 22);
    Subtask subtask2 = new Subtask("incorrectTimeSub", "description 2", 22, Status.NEW, 11, LocalTime.of(10, 30), 25);
    Subtask subtask3 = new Subtask("incorrectIdSub", "description 3", 21, Status.NEW, 11, LocalTime.of(11, 00), 25);
    Subtask subtask4 = new Subtask("incorrectEpicIdSub", "description 4", 24, Status.NEW, 1111, LocalTime.of(11, 00), 25);

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createTask() {
        inMemoryTasksManager.createTask(task);
        final Task savedTask = inMemoryTasksManager.findTaskById(1);
        assertNotNull(savedTask, "задача не найдена");
        assertEquals(savedTask, task, "задачи не совпадают");
        assertNull(inMemoryTasksManager.createTask(task2), "задача в занятом промежутке времени");
        assertNull(inMemoryTasksManager.createTask(task3), "две задачи с одним id");
        assertEquals(1, inMemoryTasksManager.history().size(), "пустая история просмотров");
        inMemoryTasksManager.deleteTask(task.getId());
        assertNull(inMemoryTasksManager.findTaskById(task.getId()), "задача не была удалена");
        assertEquals(0, inMemoryTasksManager.history().size(), "задача не удалена из истории");
    }

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createEpic() {
        inMemoryTasksManager.createEpic(epic);
        final Epic savedEpic = inMemoryTasksManager.findEpicById(11);
        assertNotNull(savedEpic, "эпик не найден");
        assertEquals(savedEpic, epic, "эпики не совпадают");
        assertNotNull(inMemoryTasksManager.findAllEpics(), "пустой список эпиков");
        assertNull(inMemoryTasksManager.createEpic(epic2), "два эпика с одним id");
        assertEquals(1, inMemoryTasksManager.history().size(), "пустая история просмотров");
        inMemoryTasksManager.deleteEpic(epic.getId());
        assertNull(inMemoryTasksManager.findEpicById(epic.getId()), "эпик не был удален");
        assertEquals(0, inMemoryTasksManager.history().size(), "эпик не удален из истории");
    }

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createSubtask() {
        inMemoryTasksManager.createEpic(epic);
        inMemoryTasksManager.createSubtask(subtask);
        final Subtask savedSubtask = inMemoryTasksManager.findSubtaskById(21);
        assertNotNull(savedSubtask, "подзадача не найдена");
        assertEquals(savedSubtask, subtask, "подзадачи не равны");
        assertNull(inMemoryTasksManager.createSubtask(subtask2), "подзадача в занятом промежутке времени");
        assertNull(inMemoryTasksManager.createSubtask(subtask3), "две подзадачи с одним id");
        assertNull(inMemoryTasksManager.createSubtask(subtask4), "создана подзадача без эпика");
        assertEquals(1, inMemoryTasksManager.history().size(), "пустая история просмотров");
        inMemoryTasksManager.deleteSubtask(21);
        assertNull(inMemoryTasksManager.findSubtaskById(21), "подзадача не была удалена");
        assertEquals(0, inMemoryTasksManager.history().size(), "задача не удалена из истории");
    }

    // проверка метода поиска всех задач
    @Test
    void findAllTasks() {
        assertEquals(0, inMemoryTasksManager.findAllTasks().size(), "найдены задачи");
        inMemoryTasksManager.createTask(task);
        assertNotNull(inMemoryTasksManager.findAllTasks(), "задача не найдена");
    }

    // проверка метода поисква всех эпиков
    @Test
    void findAllEpics() {
        assertEquals(0, inMemoryTasksManager.findAllEpics().size(), "найдены эпики");
        inMemoryTasksManager.createEpic(epic);
        assertNotNull(inMemoryTasksManager.findAllEpics(), "эпик не найден");
    }

    // проверка метода поиска все подзадач от эпика
    @Test
    void findAllSubtasks() {
        inMemoryTasksManager.createEpic(epic);
        assertEquals(0, inMemoryTasksManager.findAllSubtasks(epic).size(), "найдены подзадачи");
        inMemoryTasksManager.createSubtask(subtask);
        assertEquals(1, inMemoryTasksManager.findAllSubtasks(epic).size(), "подзадачи не найдены");
    }

    @Test
    void updateTask() {
        inMemoryTasksManager.createTask(task);
        Task task2 = new Task("task 1.1", "new description", 1, Status.DONE, LocalTime.of(9, 00), 25);
        inMemoryTasksManager.updateTask(task2);
        assertEquals(inMemoryTasksManager.findTaskById(1), task2, "задача не обновлена");
    }

    @Test
    void updateSubtask() {
        inMemoryTasksManager.createEpic(epic);
        inMemoryTasksManager.createSubtask(subtask);
        Subtask subtask2 = new Subtask("sub2", "-", 21, Status.DONE, 11, LocalTime.of(19, 00), 43);
        inMemoryTasksManager.updateSubtask(subtask2);
        assertEquals(inMemoryTasksManager.findSubtaskById(21), subtask2, "подзадача не обновлена");
    }

    @Test
    void updateEpic() {
        inMemoryTasksManager.createEpic(epic);
        Epic epic22 = new Epic("epic22", "-", 11, Status.DONE);
        inMemoryTasksManager.updateEpic(epic22);
        assertEquals(inMemoryTasksManager.findEpicById(11), epic22, "эпик не обновлён");
        Epic epic2222 = new Epic("epic22", "-", 1111, Status.DONE);
        assertNull(inMemoryTasksManager.updateEpic(epic2222), "обновлён несуществующий эпик");
    }

    @Test
    void findEpicStatus() {

    }

    @Test
    void findEpicDuration() {
        inMemoryTasksManager.createEpic(epic);
        assertEquals(0, inMemoryTasksManager.findEpicDuration(11), "не работает вычисление для пустого эпика");
        inMemoryTasksManager.createSubtask(subtask);
        inMemoryTasksManager.createSubtask(subtask1);
        assertEquals(37, inMemoryTasksManager.findEpicDuration(11), "неправильный поиск продолжительности");
    }

    @Test
    void findEndTime() {
        inMemoryTasksManager.createTask(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        assertEquals(LocalTime.of(10, 50),inMemoryTasksManager.findEndTime(1), "неверное время окончания");
        assertNull(inMemoryTasksManager.findEndTime(11), "обрабатываются несуществующие id");
    }

    @Test
    void getPrioritizedTasks() {
        inMemoryTasksManager.createTask(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        inMemoryTasksManager.createTask(new Task("two", "-", 2, LocalTime.of(9, 30), 20));
        inMemoryTasksManager.createTask(new Task("three", "-", 3, LocalTime.of(10, 00), 20));
        TreeMap<LocalTime, Integer> treeMap = inMemoryTasksManager.getPrioritizedTasks();
        assertEquals(LocalTime.of(9, 30), treeMap.firstKey(), "неверная сортировка");
        assertEquals(LocalTime.of(10, 30), treeMap.lastKey(), "неверная сортировка");
    }

    @Test
    void clearAllTasks() {
        inMemoryTasksManager.createTask(task);
        inMemoryTasksManager.clearAllTasks();
        assertEquals(0, inMemoryTasksManager.tasks.size(), "список задач не пустой");
    }

    @Test
    void clearAllSubtasks() {
        inMemoryTasksManager.createEpic(epic);
        inMemoryTasksManager.createSubtask(subtask);
        inMemoryTasksManager.clearAllSubtasks();
        assertEquals(0, inMemoryTasksManager.subtasks.size(), "список подзадач не пустой");
    }

    @Test
    void clearAllEpics() {
        inMemoryTasksManager.createEpic(epic);
        inMemoryTasksManager.clearAllEpics();
        assertEquals(0, inMemoryTasksManager.epics.size(), "список эпиков не пустой");
    }
}