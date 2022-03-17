package controller;

import exceptions.ManagerSaveException;
import model.*;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTasksManager {
    private File file;

    public FileBackedTasksManager(File file) {
       this.file = file;
    }
    //HistoryManager history = new InMemoryHistoryManager();
    InMemoryTasksManager tasksManager = new InMemoryTasksManager();
    public FileBackedTasksManager() {

    }

    // громоздкий, но рабочий метод для сохранения задач в файл csv
    public void save1() {
        try {
            StringBuilder tasksToSave = new StringBuilder();
            tasksToSave.append("id,type,name,status,description");
            tasksToSave.append("\n");
            // запись задач, подзадач и эпиков (хранятся в 3х мапах)
            for (Task task : tasks.values()) {
                tasksToSave.append(asString(task));
                tasksToSave.append("\n");
            }
            for (Task task : subtasks.values()) {
                tasksToSave.append(asString(task));
                tasksToSave.append("\n");
            }
            for (Task task : epics.values()) {
                tasksToSave.append(asString(task));
                tasksToSave.append("\n");
            }
            tasksToSave.append("\n");

            try (FileWriter writer = new FileWriter("history1.csv")) {
                writer.write(String.valueOf(tasksToSave));
            } catch (IOException e) {
                throw new ManagerSaveException("ошибка ввода/вывода: " + e.getMessage());
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    // метод для записи задач в csv
    public void save2() {
        try (PrintWriter writer = new PrintWriter("history.csv")) {
            StringBuilder sb = new StringBuilder();
            sb.append("id,type,name,status,description,startTime,duration\r\n");
            for (Task task : findAllTasks()) {
                sb.append(asString(task));
                sb.append("\n");
            }
            for (Epic epic : findAllEpics()) {
                sb.append(asString(epic));
                sb.append("\n");
                for (Task subtask : findAllSubtasks(epic)) {
                    sb.append(asString(subtask));
                    sb.append("\n");
                }
            }
            sb.append("\n");
            sb.append(toString(history));
            writer.write(sb.toString());
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    // метод для сохранения истории в csv
    public String toString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Integer id : manager.getHistory()) {
            sb.append(id);
            sb.append(",");
        }
        return sb.toString();
    }

    // метод добавляет id просмотренных задач в файл
    // в ТЗ про такой метод ничего нет, но раз уж он написан и работает, то пусть пока остается
    public void saveId(Integer id) throws ManagerSaveException {
        try (FileWriter writer = new FileWriter("history.csv", true)) {
             writer.write(String.valueOf(id));
             writer.write(",");
        } catch (IOException e) {
            throw new ManagerSaveException("ошибка ввода/вывода: " + e.getMessage());
        }
    }

    // метод для загрузки менеджера из файла
    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                manager.save2();
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return manager;
    }

    // метод для строкового отображения задачи в формате "ид,тип,имя,статус,описание,время начала,продолжительность"
    public String asString(Task task) {
        return String.format("%d,%s,%s,%s,%s,%tR,%d", task.getId(), task.getTaskType(), task.getName()
                , task.getStatus(), task.getDescription(), task.getStartTime(), task.getDuration());
    }

    // метод для получения задачи из ее строкового вида: получаем id и ищем в нужной hashmap
    public Task fromString(String value) {
        String[] split = value.split(",");
        int x = Integer.parseInt(split[0]);
        if (tasks.containsKey(x)) {
           return tasks.get(x);
        } else if (subtasks.containsKey(x)) {
           return subtasks.get(x);
        } else if (epics.containsKey(x)) {
           return epics.get(x);
        } else {
           System.out.println("task not found");
           return null;
        }
    }

    // чтение из файла в строку
    public static List<String> fromFile(String filename) {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            List<String> list = new ArrayList<>();
            while (br.ready()) {
                list.add(br.readLine());
            }
            return list;
        } catch (IOException e) {
            System.out.println("Произошла ошибка во время чтения файла.");
            return new ArrayList<>();
        }
    }

    // поиск задач
    @Override
    public Task findTaskById(Integer id) {
      super.findTaskById(id);
      save2();
      return null;
    }

    @Override
    public Subtask findSubtaskById(Integer id) {
      super.findSubtaskById(id);
      save2();
      return null;
    }

    @Override
    public Epic findEpicById(Integer id) {
      super.findEpicById(id);
      save2();
      return null;
    }

    // создание задач
    @Override
    public Task createTask(Task task) {
      super.createTask(task);
      save2();
      return null;
    }

    @Override
    public Subtask createSubtask(Subtask task) {
      super.createSubtask(task);
      save2();
      return null;
    }

    @Override
    public Epic createEpic(Epic epic) {
      super.createEpic(epic);
      save2();
      return null;
    }


}
