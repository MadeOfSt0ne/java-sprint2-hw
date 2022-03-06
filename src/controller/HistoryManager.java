package controller;

import model.Task;

import java.util.List;

public interface HistoryManager {
    // добавляем задачу в историю просмотров
    void add(Task task);
    // удаляем задачу из истории просмотров
    void remove(Integer id);
    // получаем историю просмотров
    List<Integer> getHistory();

}
