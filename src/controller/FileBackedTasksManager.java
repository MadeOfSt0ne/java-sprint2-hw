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

    public FileBackedTasksManager() {

    }

    // метод записи в файл
    public void save() {
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

            try (FileWriter writer = new FileWriter("history.csv")) {
                writer.write(String.valueOf(tasksToSave));
            } catch (IOException e) {
                throw new ManagerSaveException("ошибка ввода/вывода: " + e.getMessage());
            }
        } catch (ManagerSaveException e) {
            System.out.println(e.getMessage());
        }
    }

    // метод добавляет id просмотренных задач в файл
    public void saveId(Integer id) throws ManagerSaveException {
        try (FileWriter writer = new FileWriter("history.csv", true)) {
             writer.write(String.valueOf(id));
             writer.write(",");
        } catch (IOException e) {
            throw new ManagerSaveException("ошибка ввода/вывода: " + e.getMessage());
        }
    }

    // метод для загрузки менеджера из файла
    public static FileBackedTasksManager loadFromFile(File file) throws IOException {
        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                manager.save();
            }
        } catch (IOException e) {
            throw new IOException(e.getMessage());
        }
        return manager;
    }

    // метод для строкового отображения задачи в формате "ид,тип,имя,статус,описание"
    public String asString(Task task) {
        return String.format("%d,%s,%s,%s,%s", task.getId(), task.getTaskType(), task.getName()
                , task.getStatus(), task.getDescription());
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

    @Override
    public Task findTaskById(Integer id) {
      super.findTaskById(id);
        try {
            saveId(id);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return tasks.get(id);
    }

    @Override
    public Subtask findSubtaskById(Integer id) {
      super.findSubtaskById(id);
        try {
            saveId(id);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return subtasks.get(id);
    }

    @Override
    public Epic findEpicById(Integer id) {
      super.findEpicById(id);
        try {
            saveId(id);
        } catch (ManagerSaveException e) {
            e.printStackTrace();
        }
        return epics.get(id);
    }

    @Override
    public Task createTask(Task task) {
      super.createTask(task);
      save();
      return task;
    }

    @Override
    public Subtask createSubtask(Subtask task) {
      super.createSubtask(task);
      save();
      return task;
    }

    @Override
    public Epic createEpic(Epic epic) {
      super.createEpic(epic);
      save();
      return epic;
    }


}
