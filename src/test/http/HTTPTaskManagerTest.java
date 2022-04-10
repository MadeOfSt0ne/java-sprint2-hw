package http;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalTime;
import java.util.TreeMap;

import static org.junit.jupiter.api.Assertions.*;

class HTTPTaskManagerTest {
    static HTTPTaskManager manager = new HTTPTaskManager();
    static KVServer server;
    static Task task, task2, task3;
    static Epic epic, epic2;
    static Subtask subtask, subtask1, subtask2, subtask3, subtask4;

    @BeforeAll
    static void startServer() throws IOException {
        server = new KVServer();
        server.start();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

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

    @Test
    void createTask() {
        manager.createTask(task);
        final Task savedTask = manager.findTaskById(1);
        assertNotNull(savedTask, "задача не найдена");
        assertEquals(savedTask, task, "задачи не совпадают");
        assertEquals(1, manager.history().size(), "пустая история просмотров");
        manager.deleteTask(task.getId());
        assertNull(manager.findTaskById(task.getId()), "задача не была удалена");
        assertEquals(0, manager.history().size(), "задача не удалена из истории");
    }

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createEpic() {
        manager.createEpic(epic);
        final Epic savedEpic = manager.findEpicById(11);
        assertNotNull(savedEpic, "эпик не найден");
        assertEquals(savedEpic, epic, "эпики не совпадают");
        assertNotNull(manager.findAllEpics(), "пустой список эпиков");
        assertEquals(1, manager.history().size(), "пустая история просмотров");
        manager.deleteEpic(epic.getId());
        assertNull(manager.findEpicById(epic.getId()), "эпик не был удален");
        assertEquals(0, manager.history().size(), "эпик не удален из истории");
    }

    // тест включает в себя проверку методов создания, поиска, удаления из списка и истории
    @Test
    void createSubtask() {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        final Subtask savedSubtask = manager.findSubtaskById(21);
        assertNotNull(savedSubtask, "подзадача не найдена");
        assertEquals(savedSubtask, subtask, "подзадачи не равны");
        manager.findSubtaskById(21);   // проверка дублирования в истории задач
        assertEquals(1, manager.history().size(), "0: пустая история просмотров. 2: дублирование");
        manager.deleteSubtask(21);
        assertNull(manager.findSubtaskById(21), "подзадача не была удалена");
        assertEquals(0, manager.history().size(), "задача не удалена из истории");
    }

    // проверка метода поиска всех задач
    @Test
    void findAllTasks() {
        assertEquals(0, manager.findAllTasks().size(), "найдены задачи");
        manager.createTask(task);
        assertNotNull(manager.findAllTasks(), "задача не найдена");
    }

    // проверка метода поиска всех эпиков
    @Test
    void findAllEpics() {
        assertEquals(0, manager.findAllEpics().size(), "найдены эпики");
        manager.createEpic(epic);
        assertNotNull(manager.findAllEpics(), "эпик не найден");
    }

    // проверка метода поиска все подзадач от эпика
    @Test
    void findAllSubtasks() {
        manager.createEpic(epic);
        assertEquals(0, manager.findAllSubtasks(epic).size(), "найдены подзадачи");
        manager.createSubtask(subtask);
        assertEquals(1, manager.findAllSubtasks(epic).size(), "подзадачи не найдены");
    }

    @Test
    void getPrioritizedTasks() {
        manager.createTask(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        manager.createTask(new Task("two", "-", 2, LocalTime.of(9, 30), 20));
        manager.createTask(new Task("three", "-", 3, LocalTime.of(10, 0), 20));
        TreeMap<LocalTime, Integer> treeMap = manager.getPrioritizedTasks();
        assertEquals(LocalTime.of(9, 30), treeMap.firstKey(), "неверная сортировка");
        assertEquals(LocalTime.of(10, 30), treeMap.lastKey(), "неверная сортировка");
    }

    @Test
    void clearAllTasks() {
        manager.createTask(task);
        manager.clearAllTasks();
        assertEquals(0, manager.tasks.size(), "список задач не пустой");
    }

    @Test
    void clearAllSubtasks() {
        manager.createEpic(epic);
        manager.createSubtask(subtask);
        manager.clearAllSubtasks();
        assertEquals(0, manager.subtasks.size(), "список подзадач не пустой");
    }

    @Test
    void clearAllEpics() {
        manager.createEpic(epic);
        manager.clearAllEpics();
        assertEquals(0, manager.epics.size(), "список эпиков не пустой");
    }
}