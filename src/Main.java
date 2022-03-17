import controller.FileBackedTasksManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTasksManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.io.File;
import java.io.IOException;
import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
    // Добрый день! Тесты для последнего задания находятся ниже - старые тесты не стал удалять.

        InMemoryTasksManager inMemoryTasksManager = new InMemoryTasksManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task = new Task("task 1", "thisIsTask1", 1, LocalTime.of(10, 00), 15);
        inMemoryTasksManager.createTask(task);
        Epic epic = new Epic("epic 1", "thisIsEpic1", 11, Status.NEW);
        inMemoryTasksManager.createEpic(epic);
        Subtask subtask = new Subtask("subtask 1", "description 1", 21, Status.DONE, 11, LocalTime.of(10, 20), 15);
        Subtask subtask2 = new Subtask("subtask 2", "description 2", 22, Status.NEW, 11, LocalTime.of(11, 00), 25);
        inMemoryTasksManager.createSubtask(subtask);
        inMemoryTasksManager.createSubtask(subtask2);
        // проверка поиска
        System.out.println("Новая задача: " + inMemoryTasksManager.findTaskById(1));
        System.out.println("Новый эпик: " + inMemoryTasksManager.findEpicById(11));
        System.out.println("Новая подзадача: " + inMemoryTasksManager.findSubtaskById(21));
        System.out.println("Вторая подзадача: " + inMemoryTasksManager.findSubtaskById(22));
        // ввод неверных данных
        System.out.println(inMemoryTasksManager.findTaskById(77));
        System.out.println(inMemoryTasksManager.findEpicById(88));
        System.out.println(inMemoryTasksManager.findSubtaskById(99));
        Subtask subtask11 = new Subtask("subtask 44", "description 44", 21, Status.NEW, 44, LocalTime.of(11, 20), 15);
        inMemoryTasksManager.createSubtask(subtask11);
        Subtask subtask22 = new Subtask("subtask 55", "description 55", 44, Status.NEW, 44, LocalTime.of(11, 40), 15);
        inMemoryTasksManager.createSubtask(subtask22);
        // обновление задачи
        Task task2 = new Task("task 1.1", "new description", 1, Status.DONE, LocalTime.of(9, 00), 15);
        inMemoryTasksManager.updateTask(task2);
        System.out.println("Обновленная задача: " + inMemoryTasksManager.findTaskById(1));
        // вычисление статуса эпика
        inMemoryTasksManager.findEpicStatus(epic);
        // создание эпиков и сабтасков
        inMemoryTasksManager.createSubtask(new Subtask("subtask 3", "d 3", 23, Status.IN_PROGRESS, 11, LocalTime.of(12, 00), 15));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 4", "d 4", 24, Status.IN_PROGRESS, 11, LocalTime.of(9, 25), 15));
        inMemoryTasksManager.createEpic(new Epic("epic 2", "ddd", 12, Status.NEW));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 5", "d 5", 25, Status.NEW, 12, LocalTime.of(12, 20), 15));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 6", "d 6", 26, Status.DONE, 12, LocalTime.of(12, 40), 15));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 7", "d 7", 27, Status.NEW, 12, LocalTime.of(13, 00), 15));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 8", "d 8", 28, Status.DONE, 12, LocalTime.of(14, 00), 15));
        inMemoryTasksManager.createEpic(new Epic("epic 3", "fff", 13, Status.NEW));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 9", "d 9", 29, Status.NEW, 13, LocalTime.of(13, 25), 15));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 10", "d 10", 30, Status.DONE, 13, LocalTime.of(14, 20), 15));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 11", "d 11", 31, Status.NEW, 13, LocalTime.of(13, 43), 15));
        inMemoryTasksManager.createSubtask(new Subtask("subtask 12", "d 12", 32, Status.DONE, 13, LocalTime.of(18, 00), 15));
        // добавляем просмотры
        inMemoryTasksManager.findEpicById(12);
        inMemoryTasksManager.findSubtaskById(24);
        inMemoryTasksManager.findSubtaskById(25);
        inMemoryTasksManager.findSubtaskById(26);
        inMemoryTasksManager.findEpicById(13);
        inMemoryTasksManager.findEpicById(12);     // повторный просмотр эпика
        inMemoryTasksManager.findSubtaskById(24);  // повторный просмотр сабтаска
        inMemoryTasksManager.findSubtaskById(28);
        inMemoryTasksManager.findSubtaskById(29);
        // история без дублей
        System.out.println("История просмотров: " + inMemoryTasksManager.history());
        // =================================== тесты для задач 5 спринта =============================================
        FileBackedTasksManager manager = new FileBackedTasksManager();
        // создание задач и запись в csv включая время начала и продолжительность
        manager.createTask(new Task("task", "task111", 111, Status.NEW, LocalTime.of(15, 30), 15));
        manager.createEpic(new Epic("epic", "epic333", 333, Status.NEW));
        manager.createSubtask(new Subtask("subtask", "subtask222", 222, Status.IN_PROGRESS, 333, LocalTime.of(16, 00), 15));
        // передаем строку и получаем задачу в изначальном виде
        System.out.print("Найденная подзадача: ");
        System.out.println(manager.fromString("222,SUBTASK,subtask"));
        // поиск по id и добавление id в файл
        manager.findSubtaskById(222);
        manager.findEpicById(333);
        manager.findTaskById(111);
        System.out.println("история: "+ manager.history());
        // считывание из файла в строку
        System.out.print("Содержимое csv файла: ");
        System.out.println(manager.fromFile("history.csv"));
        // ==================================== тесты для задач 6 спринта ============================================
        // задачи и подзадачи по времени начала
        System.out.println("Задачи и подзадачи по времени: " + inMemoryTasksManager.getPrioritizedTasks());
        // берем эпик с 4 подзадачами
        System.out.println("Все подзадачи эпика : " + inMemoryTasksManager.findAllSubtasks(epic));
        // вычисление времени начала и окончания эпика
        inMemoryTasksManager.findEpicStartAndEndTime(11);
        // вычисление продолжительности эпика
        System.out.println("Продолжительность эпика: " + inMemoryTasksManager.findEpicDuration(11) + " минут.");
        // вычисление времени окончания задачи или подзадачи
        System.out.println("Время окончания задачи: " + inMemoryTasksManager.findEndTime(1));
        System.out.println("Время окончания подзадачи: " + inMemoryTasksManager.findEndTime(22));
    }
}
