package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTasksManagerTest {
    InMemoryTasksManager inMemory = new InMemoryTasksManager();
    // если переменные объявлять и присваивать им значение внутри метода с аннотацией BeforeEach, они будут недоступны.
    // если делать для них отдельный класс, то это ухудшит читаемость тестов: нужно обращаться к методу одного класса и
    // передавать в него значение другого класса. ошибки придется искать в двух классах.
    // пока остановился на таком варианте, хоть он и занимает больше строчек
    static Task task, task2, task3;
    static Epic epic, epic2;
    static Subtask subtask, subtask1, subtask2, subtask3, subtask4;

    @BeforeEach
    public void init() {
        task = new Task("correctTask", "thisIsTask1", 1, LocalTime.of(10, 0), 15);
        task2 = new Task("incorrectStartTimeTask", "thisIsTask2", 2, LocalTime.of(10, 0), 15);
        task3 = new Task("incorrectIdTask", "thisIsTask3", 1, LocalTime.of(11, 0), 15);

        epic = new Epic("correctEpic", "thisIsEpic1", 11, Status.NEW);
        epic2 = new Epic("incorrectIdEpic", "thisIsEpic2", 11, Status.IN_PROGRESS);

        subtask = new Subtask("correctSub", "description 1", 21, Status.DONE, 11, LocalTime.of(10, 20), 15);
        subtask1 = new Subtask("correctSub", "description 1", 29, Status.DONE, 11, LocalTime.of(12, 20), 22);
        subtask2 = new Subtask("incorrectTimeSub", "description 2", 22, Status.NEW, 11, LocalTime.of(10, 30), 25);
        subtask3 = new Subtask("incorrectIdSub", "description 3", 21, Status.NEW, 11, LocalTime.of(11, 0), 25);
        subtask4 = new Subtask("incorrectEpicIdSub", "description 4", 24, Status.NEW, 1111, LocalTime.of(11, 0), 25);
    }

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createTask() {
        inMemory.createTask(task);
        final Task savedTask = inMemory.findTaskById(1);
        assertNotNull(savedTask, "задача не найдена");
        assertEquals(savedTask, task, "задачи не совпадают");
        assertNull(inMemory.createTask(task2), "задача в занятом промежутке времени");
        assertNull(inMemory.createTask(task3), "две задачи с одним id");
        assertEquals(1, inMemory.history().size(), "пустая история просмотров");
        inMemory.deleteTask(task.getId());
        assertNull(inMemory.findTaskById(task.getId()), "задача не была удалена");
        assertEquals(0, inMemory.history().size(), "задача не удалена из истории");
    }

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createEpic() {
        inMemory.createEpic(epic);
        final Epic savedEpic = inMemory.findEpicById(11);
        assertNotNull(savedEpic, "эпик не найден");
        assertEquals(savedEpic, epic, "эпики не совпадают");
        assertNotNull(inMemory.findAllEpics(), "пустой список эпиков");
        assertNull(inMemory.createEpic(epic2), "два эпика с одним id");
        assertEquals(1, inMemory.history().size(), "пустая история просмотров");
        inMemory.deleteEpic(epic.getId());
        assertNull(inMemory.findEpicById(epic.getId()), "эпик не был удален");
        assertEquals(0, inMemory.history().size(), "эпик не удален из истории");
    }

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createSubtask() {
        inMemory.createEpic(epic);
        inMemory.createSubtask(subtask);
        final Subtask savedSubtask = inMemory.findSubtaskById(21);
        assertNotNull(savedSubtask, "подзадача не найдена");
        assertEquals(savedSubtask, subtask, "подзадачи не равны");
        assertNull(inMemory.createSubtask(subtask2), "подзадача в занятом промежутке времени");
        assertNull(inMemory.createSubtask(subtask3), "две подзадачи с одним id");
        assertNull(inMemory.createSubtask(subtask4), "создана подзадача без эпика");
        inMemory.findSubtaskById(21);   // проверка дублирования в истории задач
        assertEquals(1, inMemory.history().size(), "0: пустая история просмотров. 2: дублирование");
        inMemory.deleteSubtask(21);
        assertNull(inMemory.findSubtaskById(21), "подзадача не была удалена");
        assertEquals(0, inMemory.history().size(), "задача не удалена из истории");
    }

    // проверка метода поиска всех задач
    @Test
    void findAllTasks() {
        assertEquals(0, inMemory.findAllTasks().size(), "найдены задачи");
        inMemory.createTask(task);
        assertNotNull(inMemory.findAllTasks(), "задача не найдена");
    }

    // проверка метода поисква всех эпиков
    @Test
    void findAllEpics() {
        assertEquals(0, inMemory.findAllEpics().size(), "найдены эпики");
        inMemory.createEpic(epic);
        assertNotNull(inMemory.findAllEpics(), "эпик не найден");
    }

    // проверка метода поиска все подзадач от эпика
    @Test
    void findAllSubtasks() {
        inMemory.createEpic(epic);
        assertEquals(0, inMemory.findAllSubtasks(epic).size(), "найдены подзадачи");
        inMemory.createSubtask(subtask);
        assertEquals(1, inMemory.findAllSubtasks(epic).size(), "подзадачи не найдены");
    }

    @Test
    void updateTask() {
        inMemory.createTask(task);
        Task task2 = new Task("task 1.1", "new description", 1, Status.DONE, LocalTime.of(9, 0), 25);
        inMemory.updateTask(task2);
        assertEquals(inMemory.findTaskById(1), task2, "задача не обновлена");
    }

    @Test
    void updateSubtask() {
        inMemory.createEpic(epic);
        inMemory.createSubtask(subtask);
        Subtask subtask2 = new Subtask("sub2", "-", 21, Status.DONE, 11, LocalTime.of(19, 0), 43);
        inMemory.updateSubtask(subtask2);
        assertEquals(inMemory.findSubtaskById(21), subtask2, "подзадача не обновлена");
    }

    @Test
    void updateEpic() {
        inMemory.createEpic(epic);
        Epic epic22 = new Epic("epic22", "-", 11, Status.DONE);
        inMemory.updateEpic(epic22);
        assertEquals(inMemory.findEpicById(11), epic22, "эпик не обновлён");
        Epic epic2222 = new Epic("epic22", "-", 1111, Status.DONE);
        assertNull(inMemory.updateEpic(epic2222), "обновлён несуществующий эпик");
    }

    @Test
    void findEpicStatus() {
        inMemory.createEpic(epic);
        assertEquals(Status.NEW, inMemory.findEpicStatus(epic), "неверный расчет для пустого эпика");
        inMemory.createSubtask(new Subtask("1", "1", 21, Status.NEW, 11, LocalTime.of(10,0), 15));
        inMemory.createSubtask(new Subtask("2", "2", 22, Status.NEW, 11, LocalTime.of(10,30), 15));
        assertEquals(Status.NEW, inMemory.findEpicStatus(epic), "неверный расчет для всех NEW");
        inMemory.createEpic(new Epic("2", "2", 12, Status.NEW));
        inMemory.createSubtask(new Subtask("1", "1", 23, Status.DONE, 12, LocalTime.of(11,0), 15));
        inMemory.createSubtask(new Subtask("2", "2", 24, Status.DONE, 12, LocalTime.of(11,30), 15));
        assertEquals(Status.DONE, inMemory.findEpicStatus(inMemory.findEpicById(12)), "неверный расчет для всех DONE");
        inMemory.createEpic(new Epic("3", "3", 13, Status.NEW));
        inMemory.createSubtask(new Subtask("1", "1", 25, Status.DONE, 13, LocalTime.of(12,0), 15));
        inMemory.createSubtask(new Subtask("2", "2", 26, Status.NEW, 13, LocalTime.of(12,30), 15));
        assertEquals(Status.IN_PROGRESS, inMemory.findEpicStatus(inMemory.findEpicById(13)), "неверный расчет для NEW, DONE");
        inMemory.createEpic(new Epic("4", "4", 14, Status.NEW));
        inMemory.createSubtask(new Subtask("1", "1", 27, Status.IN_PROGRESS, 14, LocalTime.of(13,0), 15));
        inMemory.createSubtask(new Subtask("2", "2", 28, Status.IN_PROGRESS, 14, LocalTime.of(13,30), 15));
        assertEquals(Status.IN_PROGRESS, inMemory.findEpicStatus(inMemory.findEpicById(14)), "неверный расчет для IN_PROGRESS");
    }

    @Test
    void findEpicDuration() {
        inMemory.createEpic(epic);
        assertEquals(0, inMemory.findEpicDuration(11), "не работает вычисление для пустого эпика");
        inMemory.createSubtask(subtask);
        inMemory.createSubtask(subtask1);
        assertEquals(37, inMemory.findEpicDuration(11), "неправильный поиск продолжительности");
    }

    @Test
    void findEndTime() {
        inMemory.createTask(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        assertEquals(LocalTime.of(10, 50), inMemory.findEndTime(1), "неверное время окончания");
        assertNull(inMemory.findEndTime(11), "обрабатываются несуществующие id");
    }

    @Test
    void getPrioritizedTasks() {
        inMemory.createTask(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        inMemory.createTask(new Task("two", "-", 2, LocalTime.of(9, 30), 20));
        inMemory.createTask(new Task("three", "-", 3, LocalTime.of(10, 0), 20));
        TreeMap<LocalTime, Integer> treeMap = inMemory.getPrioritizedTasks();
        assertEquals(LocalTime.of(9, 30), treeMap.firstKey(), "неверная сортировка");
        assertEquals(LocalTime.of(10, 30), treeMap.lastKey(), "неверная сортировка");
    }

    @Test
    void clearAllTasks() {
        inMemory.createTask(task);
        inMemory.clearAllTasks();
        assertEquals(0, inMemory.tasks.size(), "список задач не пустой");
    }

    @Test
    void clearAllSubtasks() {
        inMemory.createEpic(epic);
        inMemory.createSubtask(subtask);
        inMemory.clearAllSubtasks();
        assertEquals(0, inMemory.subtasks.size(), "список подзадач не пустой");
    }

    @Test
    void clearAllEpics() {
        inMemory.createEpic(epic);
        inMemory.clearAllEpics();
        assertEquals(0, inMemory.epics.size(), "список эпиков не пустой");
    }
}