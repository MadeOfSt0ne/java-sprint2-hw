import controller.FileBackedTasksManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTasksManager;
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.time.LocalTime;

public class Main {
    public static void main(String[] args) {
    // Добрый день! Тесты для последнего задания находятся внизу.
        InMemoryTasksManager inMemory = new InMemoryTasksManager();
        InMemoryHistoryManager inMemoryHistoryManager = new InMemoryHistoryManager();
        Task task = new Task("task 1", "thisIsTask1", 1, LocalTime.of(10, 0), 15);
        inMemory.createTask(task);
        Epic epic = new Epic("epic 1", "thisIsEpic1", 11, Status.NEW);
        inMemory.createEpic(epic);
        Subtask subtask = new Subtask("subtask 1", "description 1", 21, Status.DONE, 11, LocalTime.of(10, 20), 15);
        Subtask subtask2 = new Subtask("subtask 2", "description 2", 22, Status.NEW, 11, LocalTime.of(11, 0), 25);
        inMemory.createSubtask(subtask);
        inMemory.createSubtask(subtask2);
        // проверка поиска
        System.out.println("Новая задача: " + inMemory.findTaskById(1));
        System.out.println("Новый эпик: " + inMemory.findEpicById(11));
        System.out.println("Новая подзадача: " + inMemory.findSubtaskById(21));
        System.out.println("Вторая подзадача: " + inMemory.findSubtaskById(22));
        // ввод неверных данных
        System.out.println(inMemory.findTaskById(77));
        System.out.println(inMemory.findEpicById(88));
        System.out.println(inMemory.findSubtaskById(99));
        Subtask subtask11 = new Subtask("subtask 44", "description 44", 21, Status.NEW, 44, LocalTime.of(11, 20), 15);
        inMemory.createSubtask(subtask11);
        Subtask subtask22 = new Subtask("subtask 55", "description 55", 44, Status.NEW, 44, LocalTime.of(11, 40), 15);
        inMemory.createSubtask(subtask22);
        // обновление задачи
        Task task2 = new Task("task 1.1", "new description", 1, Status.DONE, LocalTime.of(9, 0), 25);
        inMemory.updateTask(task2);
        System.out.println("Обновленная задача: " + inMemory.findTaskById(1));
        // вычисление статуса эпика
        inMemory.findEpicStatus(epic);
        // создание эпиков и сабтасков
        inMemory.createSubtask(new Subtask("subtask 3", "d 3", 23, Status.IN_PROGRESS, 11, LocalTime.of(12, 2), 17));
        // следующая подзадача не будет создана, так как время уже занято обновленной задачей (строка 38)
        inMemory.createSubtask(new Subtask("subtask 4", "d 4", 24, Status.IN_PROGRESS, 11, LocalTime.of(9, 7), 15));
        inMemory.createEpic(new Epic("epic 2", "ddd", 12, Status.NEW));
        inMemory.createSubtask(new Subtask("subtask 5", "d 5", 25, Status.NEW, 12, LocalTime.of(12, 20), 15));
        inMemory.createSubtask(new Subtask("subtask 6", "d 6", 26, Status.DONE, 12, LocalTime.of(12, 40), 15));
        inMemory.createSubtask(new Subtask("subtask 7", "d 7", 27, Status.NEW, 12, LocalTime.of(13, 0), 15));
        inMemory.createSubtask(new Subtask("subtask 8", "d 8", 28, Status.DONE, 12, LocalTime.of(14, 0), 15));
        inMemory.createEpic(new Epic("epic 3", "fff", 13, Status.NEW));
        inMemory.createSubtask(new Subtask("subtask 9", "d 9", 29, Status.NEW, 13, LocalTime.of(13, 25), 15));
        inMemory.createSubtask(new Subtask("subtask 10", "d 10", 30, Status.DONE, 13, LocalTime.of(14, 20), 15));
        inMemory.createSubtask(new Subtask("subtask 11", "d 11", 31, Status.NEW, 13, LocalTime.of(13, 43), 15));
        inMemory.createSubtask(new Subtask("subtask 12", "d 12", 32, Status.DONE, 13, LocalTime.of(18, 0), 15));
        // добавляем просмотры
        inMemory.findEpicById(12);
        inMemory.findSubtaskById(23);
        inMemory.findSubtaskById(25);
        inMemory.findSubtaskById(26);
        inMemory.findEpicById(13);
        inMemory.findEpicById(12);     // повторный просмотр эпика
        inMemory.findSubtaskById(23);  // повторный просмотр сабтаска
        inMemory.findSubtaskById(28);
        inMemory.findSubtaskById(29);
        // история без дублей
        System.out.println("История просмотров: " + inMemory.history());
        // удаление подзадачи происходит с удалением из истории просмотров
        inMemory.deleteSubtask(26);
        System.out.println("История просмотров: " + inMemory.history());
        // =================================== тесты для задач 5 спринта =============================================
        FileBackedTasksManager fileBacked = new FileBackedTasksManager();
        // создание задач и запись в csv включая время начала и продолжительность
        fileBacked.createTask(new Task("task", "task111", 111, Status.NEW, LocalTime.of(15, 30), 15));
        fileBacked.createEpic(new Epic("epic", "epic333", 333, Status.NEW));
        fileBacked.createSubtask(new Subtask("subtask", "subtask222", 222, Status.IN_PROGRESS, 333, LocalTime.of(16, 0), 15));
        // передаем строку и получаем задачу в изначальном виде
        System.out.print("Найденная подзадача: ");
        System.out.println(fileBacked.fromString("222,SUBTASK,subtask"));
        // поиск по id и добавление id в файл
        fileBacked.findSubtaskById(222);
        fileBacked.findEpicById(333);
        fileBacked.findTaskById(111);
        System.out.println("история: "+ fileBacked.history());
        // считывание из файла в строку
        System.out.print("Содержимое csv файла: ");
        System.out.println(fileBacked.fromFile("history.csv"));
        // ==================================== тесты для задач 6 спринта ============================================
        // задачи и подзадачи по времени начала
        System.out.println("Задачи и подзадачи по времени: " + inMemory.getPrioritizedTasks());
        // берем эпик с 4 подзадачами
        System.out.println("Все подзадачи эпика : " + inMemory.findAllSubtasks(epic));
        // вычисление времени начала и окончания эпика
        //inMemoryTasksManager.findEpicStartAndEndTime(11);
        inMemory.findEpicStartEndTime(11);
        // вычисление продолжительности эпика
        System.out.println("Продолжительность эпика: " + inMemory.findEpicDuration(11) + " минут.");
        // вычисление времени окончания задачи или подзадачи
        System.out.println("Время окончания задачи: " + inMemory.findEndTime(1));
        System.out.println("Время окончания подзадачи: " + inMemory.findEndTime(22));
        // проверка пересечения во времени
        inMemory.createTask(new Task("testTask", "doubleBooking", 7, LocalTime.of(10,10), 15));
        inMemory.updateSubtask(new Subtask("testSubtask", "doubleBooking", 21, Status.NEW, 11, LocalTime.of(10,10), 15));

    }
}
