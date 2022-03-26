package controller;

import model.Task;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {
    InMemoryHistoryManager historyManager = new InMemoryHistoryManager();

    @Test
    void add() {
        historyManager.add(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        assertNotNull(historyManager.getHistory(), "пустая история задач");
        assertEquals(1, historyManager.getHistory().size(), "задача не добавлена в историю");
    }

    @Test
    void remove() {
        historyManager.add(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        historyManager.remove(1);
        assertEquals(0, historyManager.getHistory().size(), "задача не удалена из истории");
    }

    @Test
    void getHistory() {
        assertEquals(0, historyManager.getHistory().size(), "история задач должна быть пустой");
        historyManager.add(new Task("one", "-", 1, LocalTime.of(10, 30), 20));
        historyManager.add(new Task("two", "-", 1, LocalTime.of(11, 30), 20));
        assertEquals(1, historyManager.getHistory().size(), "задача продублирована в истории");
        historyManager.add(new Task("three", "-", 2, LocalTime.of(12, 30), 20));
        assertEquals(2, historyManager.getHistory().size(), "не все задачи добавлены в историю");
    }
}