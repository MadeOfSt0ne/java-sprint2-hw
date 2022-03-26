package controller;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTasksManagerTest {
    FileBackedTasksManager fileBacked = new FileBackedTasksManager();
    // не проверил методы должным образом и тесты выявили неправильную работу. пришлось фиксить.

    @Test
    void fromString() {
        fileBacked.createTask(new Task("task", "task111", 19, Status.NEW, LocalTime.of(15, 30), 15));
        fileBacked.createEpic(new Epic("epic", "epic333", 33, Status.NEW));
        fileBacked.createSubtask(new Subtask("subtask", "subtask22", 22, Status.IN_PROGRESS, 33, LocalTime.of(16, 0), 15));
        Task task = fileBacked.fromString("22,SUBTASK,subtask");
        Task sub = fileBacked.findSubtaskById(22);
        assertEquals(sub, task, "метод не получает id из строки");
    }

    @Test
    void loadFromFile() throws IOException {
        File file = new File("history.csv");
        // удаляем файл перед каждым запуском теста, иначе старые результаты будут мешать
        Files.deleteIfExists(Path.of(file.getPath()));
        // считывание из пустого файла - пустая история
        FileBackedTasksManager zeroTasks = fileBacked.loadFromFile(file);
        assertEquals(0, zeroTasks.historyList.size());
        // создаем 3 задачи и восстанавливаем их из файла
        fileBacked.createTask(new Task("task", "task111", 111, Status.NEW, LocalTime.of(10, 0), 15));
        fileBacked.createEpic(new Epic("epic", "epic333", 333, Status.NEW));
        fileBacked.createSubtask(new Subtask("subtask", "subtask222", 222, Status.IN_PROGRESS, 333, LocalTime.of(11, 0), 15));
        FileBackedTasksManager loadedManager = fileBacked.loadFromFile(file);
        assertEquals(3, loadedManager.historyList.size());
    }
}