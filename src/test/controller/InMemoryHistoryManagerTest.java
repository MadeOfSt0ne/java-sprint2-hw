package controller;


import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {
    InMemoryHistoryManager history = new InMemoryHistoryManager();

    // методы add, remove применяются в составе методов класса InMemoryTasksManager, поэтому их тесты проходят в составе
    // тестов этого класса
    @Test
    void getHistory() {
        assertEquals(0, history.getHistory().size(), "история не пустая");
    }
}