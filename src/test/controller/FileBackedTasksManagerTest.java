package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest {
    FileBackedTasksManager fileBacked = new FileBackedTasksManager();

    @Test
    void fromString() {
        fileBacked.createTask(new Task("task", "task111", 111, Status.NEW, LocalTime.of(15, 30), 15));
        fileBacked.createEpic(new Epic("epic", "epic333", 333, Status.NEW));
        fileBacked.createSubtask(new Subtask("subtask", "subtask222", 222, Status.IN_PROGRESS, 333, LocalTime.of(16, 00), 15));
        final Task task = fileBacked.fromString("222,SUBTASK,subtask");
        final Task sub = fileBacked.findSubtaskById(222);
        assertEquals(sub, task, "метод не получает id из строки");
    }

    @Test
    void loadFromFile() throws IOException {
        File file = new File("history.csv");
        fileBacked.createTask(new Task("task", "task111", 111, Status.NEW, LocalTime.of(15, 30), 15));
        fileBacked.createEpic(new Epic("epic", "epic333", 333, Status.NEW));
        fileBacked.createSubtask(new Subtask("subtask", "subtask222", 222, Status.IN_PROGRESS, 333, LocalTime.of(16, 00), 15));
        FileBackedTasksManager loadedManager = fileBacked.loadFromFile(file);
        assertNotNull(loadedManager);
    }
}